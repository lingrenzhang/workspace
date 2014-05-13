package com.hitchride.standardClass;

import java.sql.Time;
import java.util.Date;

import com.mysql.jdbc.ResultSet;

//Typically load temporide directly. Not keeping persistent at memory.
public class TransientRide implements PersistentStorage{
	public int transientRideId;
	
	public GeoInfo origLoc,destLoc;
	public int dist,dura;
	
	public Time rideData;
	public Time rideTime,rideFlex;
	
	public int totalSeats;
	public int availSeats;
	public double detourFactor; 
	// for both drivers and passengers.  Total price for trip, per day price for commute
	public double price;
	
	
	//Persistent storage related
	boolean _isChanged = false;
  	boolean _isSaved = false;
  	Date _lastCp;
  	
  	//Initialize from RideInfo
  	public TransientRide(RideInfo rideinfo)
  	{
  		
  	}
  	
  	//Initialize from ResultSet
  	public TransientRide(ResultSet rs)
  	{
  		
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
