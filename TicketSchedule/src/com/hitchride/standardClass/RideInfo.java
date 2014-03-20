package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import com.hitchride.access.RideInfoAccess;
import com.hitchride.access.TopicTbAccess;
import com.hitchride.global.AllUsers;


//RideInfo saves the ride information.
//Template design mode for ownerRide and participantRide
public class RideInfo implements PersistentStorage{
	// assign all not-applicable fields to null
	
	public int recordId;
	
	public boolean userType;
	public GeoInfo origLoc,destLoc;
	public int dist,dura;
	
	// for output only
	private User _user;
	public int userId; //Require to compute now... Will reorg DB structure.
    public String username;
	
	// for drivers only
	public int totalSeats;
	public int availSeats;
	public double detourFactor; // deprecate
	// for both drivers and passengers.  Total price for trip, per day price for commute
	public double price;
	
	
	public Schedule schedule;
	// for commute only
	//Normally used when initialize from client input.
	public RideInfo() {
		
	}
	
	//Normally used when initialize from client input.
	//Used when we want to deriver participantRide or ownerRide from the ride info.
	public RideInfo(RideInfo r) {
		//share the same schedule,origLoc,destLoc object. 
		//Original RideInfo r is mostly a temporarily generated form associating with Post Ride page before
		//attach it to real topic. Life cycle can be terminated after register to participantRide.
		//this.schedule = r.schedule.clone();
		//this.origLoc =  r.origLoc.clone();
		//this.destLoc = r.destLoc.clone();
		this.schedule = r.schedule;
		this.userType = r.userType;
		this.origLoc = r.origLoc;
		this.destLoc = r.destLoc;

		this.userId = r.userId;
		this.username = r.username;
		this._user = r.get_user();  //participantRide and ownerRide share the same user object. 
		                     //Used to process the ride associating with participant ride.


		/*
		this.participant = new ArrayList<Integer>(r.participant.size());
        for (Iterator<Integer> parI = r.participant.iterator();parI.hasNext();)
        {
        	this.participant.add(parI.next());
        }
		*/
        this.recordId = r.recordId;
	}

	//Initialize from DB. Used to generate our dummy data pool from carpoolTb
	public RideInfo(ResultSet rs, Boolean myArgsCommute) throws SQLException {
		// assign every field
		schedule = new Schedule();
		schedule.set_isCommute(myArgsCommute); // come from table name, not table content
		schedule.set_isRoundTrip(rs.getBoolean("roundtrip"));
		double origLat = rs.getDouble("origLat");
		double origLon = rs.getDouble("origLon");
		double destLat 	= rs.getDouble("destLat");
		double destLon 	= rs.getDouble("destLon");
		
		// for output only
		username = rs.getString("username");
		//user = (User) Environment.getEnv().getUser(username); For this routine, fully initialized after username table ready.
		recordId	= rs.getInt("recordId");
		String origState = rs.getString("origState");
		String origCity	 = rs.getString("origCity");
		String origNbhd	 = rs.getString("origNbhd");
		String origAddr	 = rs.getString("origAddr");
		String destState = rs.getString("destState");
		String destCity	= rs.getString("destCity");
		String destNbhd	= rs.getString("destNbhd");
		String destAddr	= rs.getString("destAddr");
		
		origLoc = new GeoInfo(origAddr+origNbhd,origCity+origState,origLat,origLon);
		destLoc = new GeoInfo(destAddr+destNbhd,destCity+destState,destLat,destLon);
		// score will be determined by the ScoreCalculator

		// User Info
		userType 	= rs.getBoolean("userType");
		totalSeats	= rs.getInt("seatsAvailable");
		availSeats = totalSeats;
		// for both drivers and passengers.  Total price for trip, per day price for commute
		price 		= rs.getDouble("price");
		
		if(schedule.isCommute()){
			// for commute only
			//	Time forwardTime = rs.getTime("forwardTime");
			//  Time backTime	= rs.getTime("backTime");
			Time forwardTime = new Time(8*3600000);
			Time backTime = new Time(18*3600000);
			schedule.forwardFlexibility = rs.getTime("forwardFlexibility");
			schedule.backFlexibility	= rs.getTime("backFlexibility");	// for round trip only
			int dayOfWeek = rs.getInt("dayofweek");
			schedule.set_dayOfWeek(dayOfWeek);
			for(int i=0;i<7;i++)
			{
				//Manually specify here
				schedule.cftime[i] = forwardTime;
				schedule.cbtime[i] = backTime;
			}
			//Initialize tripDateAnyway for the dummy DB.
			schedule.tripDate = new Date(114,5,1);
			schedule.tripTime = new Time(12,0,0);
		} else {
 			schedule.tripDate = rs.getDate("tripDate");
		}
	}
	
	public void set_user(User user)
	{
		this._user = user;
		this.userId = user.get_uid();
		this.username = user.get_name();
	}
	
	public String getBarMessage()
	{
		String barMessage="";
		if (this.userType)
		{
			barMessage = "Provides " + this.totalSeats +" seats at price " +  this.price + " each.";
		}
		else
		{
			barMessage = "Willing to contribute " +  this.price+ " for the ride."  ;
		}
		return barMessage;
	}
	
	
	
    public User get_user()
    {
    	if (this._user != null)
    	{
    		return this._user;
    	}
    	
    	if (this.userId>0)
    	{
    		return (User) AllUsers.getUsers().getUser(userId);
    	}
    	
    	if (this.username!=null)
    	{
    		User user = (User) AllUsers.getUsers().getUser(username);
            this._user=user;
    		return user;
    	}
    	return null;
    }
    
    public String get_username()
    {
    	if (this.username==null)
    	{
    		this.username = this.get_user().get_name();
    	}
    	return this.username;
    }

    
    public String getGeoHTML()
    {
    	StringBuilder result = new StringBuilder();
    	result.append("<div class=\"geo\"> From: ");
    	result.append(origLoc.get_formatedAddr()+"<br>");
    	result.append("To: "+destLoc.get_formatedAddr()+"</div>");
    	return result.toString();
    }
    public String getScheduleHTML()
    {
    	StringBuilder result = new StringBuilder();
    	result.append("<div class=\"schedule\"> ");
    	result.append(schedule.getSchedule(true));
    	result.append("</div>");
    	return result.toString();
    }

    
    //Persistent Storage related
  //Persistent Storage Related
  	boolean _isSaved = false;
  	Date _lastCp;
  	
  	public void updateDB()
  	{
  		
  		int rows = RideInfoAccess.updateRideInfo(this);
  		if (rows==0)
  		{
 			System.out.println("Update failed for rideinfo: "+ this.recordId + " attempting insert.");
  			rows = RideInfoAccess.insertRideInfo(this);
  			if (rows==0)
  			{
  	  			System.out.println("Insert also failed for rideinfo: "+ this.recordId + " Check DB integrity.");
  			}
  		}
  	}
  	
  	@Override
  	public void insertToDB() {
  		int rows  = RideInfoAccess.insertRideInfo(this);
  		if (rows==0)
  		{
  			System.out.println("Insert failed for rideinfo: "+ this.recordId + " attempting update.");
  			rows = RideInfoAccess.updateRideInfo(this);
  			if (rows==0)
  			{
  				System.out.println("Update also failed for rideinfo: "+ this.recordId + " Check DB integrity.");
  			}
  		}
  	}

  	@Override
  	public boolean isChanged() {
  		// TODO Auto-generated method stub
  		return false;
  	}


  	@Override
  	public boolean isSaved() {
  		// TODO Auto-generated method stub
  		return false;
  	}


  	@Override
  	public Date lastCheckpoint() {
  		// TODO Auto-generated method stub
  		return this._lastCp;
  	}


  	@Override
  	public boolean storageMode() {
  		// Instant storage mode now.
  		return false;
  	}
}


