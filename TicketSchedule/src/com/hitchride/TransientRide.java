package com.hitchride;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;

import com.hitchride.database.access.CommuteOwnerRideAccess;
import com.hitchride.database.access.TransientRideAccess;
import com.hitchride.environ.AllUsers;
import com.hitchride.util.TimeFormatHelper;
import com.mysql.jdbc.ResultSetImpl;

//Typically load temporide directly. Not keeping persistent at memory.
public class TransientRide implements IPersistentStorage{
	public User owner;
	public int ownerId;
	public int id;
	
	public GeoInfo origLoc,destLoc;
	public int dist,dura;
	
	public Date rideDate;
	public Time rideTime,rideFlex;
	
	public boolean userType;
	public int totalSeats;
	public int availSeats;
	public double detourFactor; 
	// for both drivers and passengers.  Total price for trip, per day price for commute
	public double price;
	
	
	//Persistent storage related
	boolean _isChanged = false;
  	boolean _isSaved = false;
  	java.util.Date _lastCp;
  	
  	
  //Initialize from RideInfo
  	public TransientRide()
  	{
  		
  	}
  	//Initialize from RideInfo
  	public TransientRide(CommuteRide rideinfo)
  	{
  		//TODO: Think about a way for the transformation.
  	}
  	
  	//Initialize from ResultSetImpl
  	public TransientRide(ResultSetImpl rs)
  	{
  		try {
  			this.ownerId = rs.getInt("userId");
  			owner = (User) AllUsers.getUsers().getUser(ownerId);
  			this.id = rs.getInt("transientRideId");
  			
			String origFAddr=rs.getString("origFAddr");
			String origAddr=rs.getString("origAddr");
			String origState=rs.getString("origState");
			String origCity=rs.getString("origCity");
			double origLat=rs.getDouble("origLat");
			double origLon=rs.getDouble("origLon");
			this.origLoc = new GeoInfo(origFAddr, origAddr, origState, origCity, origLat, origLon);
			
			
			String destFAddr=rs.getString("destFAddr");
			String destAddr=rs.getString("destAddr");
			String destState=rs.getString("destState");
			String destCity=rs.getString("destCity");
			double destLat=rs.getDouble("destLat");
			double destLon=rs.getDouble("destLon");
			this.destLoc = new GeoInfo(destFAddr, destAddr, destState, destCity, destLat, destLon);
			
			this.dist = rs.getInt("distance");
			this.dura = rs.getInt("duration");
			
			this.rideDate = rs.getDate("rideDate");
			this.rideTime = rs.getTime("rideTime");
			this.rideFlex = rs.getTime("rideFlex");
			
			this.userType = rs.getBoolean("UserType");
			this.totalSeats = rs.getInt("TotalSeats");
			this.availSeats = rs.getInt("AvailSeats");
			this.price = rs.getDouble("PayperSeat");
			_lastCp = new java.util.Date();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
  	}
  	
  	public User getOwner()
  	{
  		if (owner==null)
  		{
  			this.owner = (User) AllUsers.getUsers().getUser(this.ownerId);
  		}
  		return owner;
  	}
  	
	@Override
	public void insertToDB() {
		int rows = TransientRideAccess.insertTransientRideInfo(this);
		if (rows == 0)
		{
			System.out.println("Insert failed for topicride: "+ this.id + " attempting update.");
			rows = TransientRideAccess.updateTransientRide(this);
			if (rows== 0)
			{
				System.out.println("Update also failed for topicride: "+this.id + " Please check DB integrity.");
			}
		}
	}

	@Override
	public boolean isChanged() {
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
		return null;
	}

	@Override
	public boolean storageMode() {
		// TODO Auto-generated method stub
		return false;
	}
	
    public String getGeoHTML()
    {
    	StringBuilder result = new StringBuilder();
    	result.append("<div class=\"geo\"> 出发地: ");
    	result.append(origLoc.get_formatedAddr()+"<br>");
    	result.append("目的地: "+destLoc.get_formatedAddr()+"</div>");
    	return result.toString();
    }
    
    public String getScheduleHTML()
    {  
    	StringBuilder result = new StringBuilder();
    	result.append("<div class=\"schedule\"> ");
    	
    	result.append("出发日期： ");
		result.append(this.rideDate);
		result.append("<br>");
		
		result.append("出发时间： ");
		result.append(this.rideTime);
		result.append("<br>");
		/*
		result.append("Flex: ");
		result.append(this.rideFlex);
		result.append("<br>");
		*/

    	result.append("</div>");
    	return result.toString();
    }
}
