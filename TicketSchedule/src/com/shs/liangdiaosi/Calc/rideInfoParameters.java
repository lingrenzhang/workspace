package com.shs.liangdiaosi.Calc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;

public class rideInfoParameters implements Comparable<rideInfoParameters>{
	// assign all not-applicable fields to null
	public Boolean commute, roundtrip, userType;
	public Double origLat, origLon, destLat, destLon;
	
	// for output only
	public String username;
	public Integer recordId;
	public String origState, origCity, origNbhd, origAddr, destState, destCity, destNbhd, destAddr;
	public Double score; // we rank results based on match score

	// for drivers only
	public Integer seatsAvailable;
	
	// for both drivers and passengers.  Total price for trip, per day price for commute
	public Double price;
	
	// for commute only
	public Time forwardTime, forwardFlexibility;
	public Time backTime, backFlexibility;	// for round trip only
	public Boolean[] dayOfWeek;
	
	// for travel only
	public Date tripDate;

	public rideInfoParameters() {
	}

	public rideInfoParameters(ResultSet rs) throws SQLException {
//		commute 	= rs.getBoolean("commute");
		roundtrip 	= rs.getBoolean("roundtrip");
		userType 	= rs.getBoolean("userType");
		origLat 	= rs.getDouble("origLat");
		origLon 	= rs.getDouble("origLon");
		destLat 	= rs.getDouble("destLat");
		destLon 	= rs.getDouble("destLon");
		
		// for output only
		username	= rs.getString("username");
		recordId	= rs.getInt("recordId");
		origState	= rs.getString("origState");
		origCity	= rs.getString("origCity");
		origNbhd	= rs.getString("origNbhd");
		origAddr	= rs.getString("origAddr");
		destState	= rs.getString("destState");
		destCity	= rs.getString("destCity");
		destNbhd	= rs.getString("destNbhd");
		destAddr	= rs.getString("destAddr");
		// score will be determined by the ScoreCalculator

		// for drivers only
		//seatsAvailable	= rs.getInt("seatsAvailable");
		
		// for both drivers and passengers.  Total price for trip, per day price for commute
		//price 		= rs.getDouble("price");
		
		// for commute only
		forwardTime	= rs.getTime("forwardTime");
		backTime	= rs.getTime("backTime");
		forwardFlexibility 	= rs.getTime("forwardFlexibility");
		backFlexibility		= rs.getTime("backFlexibility");	// for round trip only
		//public Boolean[] dayOfWeek;
		
		// for travel only
//		tripDate	= rs.getDate("tripDate");
	}

	@Override
	public int compareTo(rideInfoParameters that) {
			return -(this.score).compareTo(that.score); // from high to low
	}
}
