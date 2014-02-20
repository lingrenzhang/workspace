package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hitchride.access.UserTbAccess;
import com.hitchride.calc.rideInfoParameters;
import com.hitchride.global.Environment;

//RideInfo saves the ride information.
//Template design mode for ownerRide and participantRide
public class RideInfo{
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
	public int seatsAvailable;
	public int curSeatsAvail;
	public double detourFactor; // deprecate
	// for both drivers and passengers.  Total price for trip, per day price for commute
	public double price;
	
	
	public Schedule schedule;
	// for commute only

	// for travel only
	public Date tripDate;

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

	//Initialize from DB.
	public RideInfo(ResultSet rs, Boolean myArgsCommute) throws SQLException {
		// assign every field
		schedule = new Schedule();
		schedule.set_isCommute(myArgsCommute); // come from table name, not table content
		schedule.set_isRoundTrip(rs.getBoolean("roundtrip"));
		userType 	= rs.getBoolean("userType");
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

		// for drivers only
		seatsAvailable	= rs.getInt("seatsAvailable");
		detourFactor	= rs.getDouble("detourFactor"); // deprecate
		
		// for both drivers and passengers.  Total price for trip, per day price for commute
		price 		= rs.getDouble("price");
		
		if(schedule.isCommute()){
			// for commute only
			schedule.forwardTime = rs.getTime("forwardTime");
			schedule.backTime	= rs.getTime("backTime");
			schedule.forwardFlexibility 	= rs.getTime("forwardFlexibility");
			schedule.backFlexibility		= rs.getTime("backFlexibility");	// for round trip only
			int dayOfWeek = rs.getInt("dayofweek");
			schedule.set_dayOfWeek(dayOfWeek);
		} else {
 			schedule.tripDate	= rs.getDate("tripDate");
		}
	}
	
	public String getBarMessage()
	{
		String barMessage="";
		if (this.userType)
		{
			barMessage = "Provides " + this.seatsAvailable +" seats at price " +  this.price + " each.";
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
    		return (User) Environment.getEnv().getUser(userId);
    	}
    	
    	if (this.username!=null)
    	{
    		//UserTbAccess utb = new UserTbAccess();
    		//this.userId = utb.getIDbyName(username);
    		User user = (User) Environment.getEnv().getUser(username);
            this._user=user;
    		return user;
    	}
    	return null;
    }

}


