package com.hitchride.standardClass;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;

import com.mysql.jdbc.ResultSet;

//Typically load temporide directly. Not keeping persistent at memory.
public class TransientRide implements PersistentStorage{
	public int userId;
	public int transientRideId;
	
	public GeoInfo origLoc,destLoc;
	public int dist,dura;
	
	public Date rideData;
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
  	public TransientRide(RideInfo rideinfo)
  	{
  		
  	}
  	
  	//Initialize from ResultSet
  	public TransientRide(ResultSet rs)
  	{
  		try {
  			this.userId = rs.getInt("userId");
  			this.transientRideId = rs.getInt("transientRideId");
  			
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
			
			this.rideData = rs.getDate("rideDate");
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
  	
  	
	@Override
	public void insertToDB() {
		// TODO Auto-generated method stub
		
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
	
}
