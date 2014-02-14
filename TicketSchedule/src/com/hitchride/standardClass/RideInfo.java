package com.hitchride.standardClass;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hitchride.calc.rideInfoParameters;

//RideInfo saves the ride information.
public abstract class RideInfo{
	// assign all not-applicable fields to null
	public boolean commute, roundtrip, userType;
	public GeoInfo origLoc,destLoc;
	
	// for output only
	public String username; //owner name
	public int ownerId; //Require to compute now... Will reorg DB structure.
	public List<Integer> participant;
	
	public int recordId;

	// for drivers only
	public int seatsAvailable;
	public double detourFactor; // deprecate
	
	// for both drivers and passengers.  Total price for trip, per day price for commute
	public double price;
	
	// for commute only
	public Time forwardTime, forwardFlexibility;
	public Time backTime, backFlexibility;	// for round trip only
	public Boolean[] dayOfWeek;
	
	// for travel only
	public Date tripDate;

	//Normally used when initialize from client input.
	public RideInfo() {
		
	}
	
	//Normally used when initialize from client input.
	public RideInfo(RideInfo r) {
		
		this.commute = r.commute;
		this.roundtrip = r.roundtrip;
		this.userType = r.userType;
		this.origLoc =  r.origLoc.clone();
		this.destLoc = r.destLoc.clone();
		
		// for output only
		this.username = r.username;
		this.ownerId = r.ownerId;

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
		commute		= myArgsCommute; // come from table name, not table content
		roundtrip 	= rs.getBoolean("roundtrip");
		userType 	= rs.getBoolean("userType");
		double origLat = rs.getDouble("origLat");
		double origLon = rs.getDouble("origLon");
		double destLat 	= rs.getDouble("destLat");
		double destLon 	= rs.getDouble("destLon");
		
		// for output only
		username	= rs.getString("username");
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
		
		if(commute){
			// for commute only
			forwardTime	= rs.getTime("forwardTime");
			backTime	= rs.getTime("backTime");
			forwardFlexibility 	= rs.getTime("forwardFlexibility");
			backFlexibility		= rs.getTime("backFlexibility");	// for round trip only
			dayOfWeek	= new Boolean[7];
			//TODO change to getting results from the dayOfWeek String
		} else {
			// for travel only
			tripDate	= rs.getDate("tripDate");
		}
	}

}


