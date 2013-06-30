package com.shs.liangdiaosi.Calc;
import com.shs.liangdiaosi.Access.*;
import java.sql.*;

public class ScoreCalculator {
	
	private int timeToInt(Time time){
		// TODO: what's the best way to add/subtract java.sql.time?
		return time.getHours()*3600 + time.getMinutes()*60 + time.getSeconds();
	}
	
	private double timeScore(ResultSet rs1, ResultSet rs2) {
		// TODO: also consider backTime for roundtrips
		// TODO: think of a non-boolean function
		// TODO: take into account time spent driving to pick-up location
		boolean retval = false;
		try {
			int time1 = timeToInt(rs1.getTime("forwardTime"));
			int time2 = timeToInt(rs2.getTime("forwardTime"));
			int flex1 = timeToInt(rs1.getTime("forwardFlexibility"));
			int flex2 = timeToInt(rs2.getTime("forwardFlexibility"));
			retval = (flex1 + flex2) > Math.abs(time1-time2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (retval ? 1 : 0);
	}

	private double getDistance(ResultSet rs1, ResultSet rs2, String destType1, String destType2) throws SQLException{
		// destType is either "orig" or "dest"
		// TODO: replace with google API
		String city1 = rs1.getString(destType1+"City");
		String city2 = rs2.getString(destType2+"City");
		if(city1 == city2)
			return 0;
		else
			return 50;
	}
	
	private double destScore(ResultSet rs1, ResultSet rs2) {
		double score = 0;
		try {
			// distance for user 1 to pick up user 2
			double pickUpDist1 = getDistance(rs1, rs2, "orig", "orig")+getDistance(rs2, rs2, "orig", "dest")+getDistance(rs2, rs2, "dest", "dest");
			// distance for user 1
			double dist1 = getDistance(rs1, rs1, "orig", "dest");
			score = Math.max(0,1- (pickUpDist1/dist1-1)/rs1.getDouble("detourFactor"));
		} catch (SQLException e) {
			System.err.println("SQLException:"+e.getMessage());
		}
		return score;
	}

	private double typeScore(ResultSet rs1, ResultSet rs2) {
		boolean retval = true;
		try {
			retval = retval && (rs1.getBoolean("commute")==rs1.getBoolean("commute"));
			retval = retval && (rs1.getBoolean("roundtrip")==rs2.getBoolean("roundtrip"));
			retval = retval && (rs1.getString("userType")=="driver");
			if(rs1.getBoolean("commute")){
				retval = retval && (rs1.getString("dayOfWeek")==rs2.getString("dayOfWeek"));
			} else {
				retval = retval && (rs1.getDate("tripDate")==rs2.getDate("tripDate"));
			}
		} catch (SQLException e) {
			System.err.println("SQLException:"+e.getMessage());
		}
		return (retval ? 1.0 : 0.0);
	}    

	public double getScore(ResultSet rs1, ResultSet rs2){
		// always assume user 1 picks up user 2
		// call with input arguments swapped if we want user 2 pick up user 1
		double typeScore = typeScore(rs1, rs2);
		double destScore = destScore(rs1, rs2);
		double timeScore = timeScore(rs1, rs2);
		return typeScore * destScore * timeScore;
	}


}
