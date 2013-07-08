package com.shs.liangdiaosi.Calc;
import com.shs.liangdiaosi.Access.*;
import java.sql.*;
import java.util.*;

public class ScoreCalculator {
	private int timeToInt(Time time){
		// TODO: what's the best way to add/subtract java.sql.time?
		return time.getHours()*3600 + time.getMinutes()*60 + time.getSeconds();
	}

	
	private double timeScore(rideInfoParameters myArgs, rideInfoParameters rsParamObj) {
		// TODO: also consider backTime for roundtrips
		// TODO: think of a non-boolean function
		// TODO: take into account time spent driving to pick-up location
		boolean retval = false;
		int time1 = timeToInt(myArgs.forwardTime);
		int time2 = timeToInt(rsParamObj.forwardTime);
		int flex1 = timeToInt(myArgs.forwardFlexibility);
		int flex2 = timeToInt(rsParamObj.forwardFlexibility);
		retval = (flex1 + flex2) > Math.abs(time1-time2);
		return (retval ? 1 : 0);
	}
	
	private double greatCircleDistance(double lat1, double lon1, double lat2, double lon2){
		double dLat_rad = (lat2-lat1)/180*Math.PI; // convert deg to rad
		double dLon_rad = (lon2-lon1)/180*Math.PI;
		double lat1_rad = lat1/180*Math.PI;
		double lat2_rad = lat2/180*Math.PI;
		double a = Math.sin(dLat_rad/2) * Math.sin(dLat_rad/2) +
		        Math.sin(dLon_rad/2) * Math.sin(dLon_rad/2) * Math.cos(lat1_rad) * Math.cos(lat2_rad); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		//double R = 6371; // km
		double R = 6371/1.6; // mile
		return R*c;
	}

	private double destScoreByCoordinates(rideInfoParameters myArgs, rideInfoParameters rsParamObj) {
		// same for single trip and round trips, but have to consider who is driver and who is passenger
		double score = 0;
		double origLat1 = myArgs.origLat;
		double origLon1 = myArgs.origLon;
		double destLat1 = myArgs.destLat;
		double destLon1 = myArgs.destLon;
		boolean userType1 = myArgs.userType;

		double origLat2 = rsParamObj.origLat;
		double origLon2 = rsParamObj.origLon;
		double destLat2 = rsParamObj.destLat;
		double destLon2 = rsParamObj.destLon;
		boolean userType2 = rsParamObj.userType;

		// define score to be driver's original driving distance divide by new driving distance with the pick-up
		// so if I take a 10% detour, score is about .9.  If I take a 20% detour, score is about .8.
		
		double pickUpDist1 = greatCircleDistance(origLat1, origLon1, origLat2, origLon2)
			+greatCircleDistance(origLat2, origLon2, destLat2, destLon2)
			+greatCircleDistance(destLat2, destLon2, destLat1, destLon1);
		double dist1 = greatCircleDistance(origLat1, origLon1, destLat1, destLon1);
		double pickUpDist2 = greatCircleDistance(origLat2, origLon2, origLat1, origLon1)
			+greatCircleDistance(origLat1, origLon1, destLat1, destLon1)
			+greatCircleDistance(destLat1, destLon1, destLat2, destLon2);
		double dist2 = greatCircleDistance(origLat2, origLon2, destLat2, destLon2);

		if(userType1) score = Math.max(score, dist1/pickUpDist1);
		if(userType2) score = Math.max(score, dist2/pickUpDist2);
		
		// The logic above will tell someone going from SF to LA to pick up someone going from Stanford to Menlo Park, which doesn't make sense
		// Therefore, we cap score at 2 times the ratio between dist1 and dist2. 2 is a magic number, we should change that.
		score = Math.min(score, 2*((dist1<dist2) ? dist1/dist2 : dist2/dist1));
		return score;
	}
	
/*	public double getScoreByAddress(ResultSet rs1, ResultSet rs2){
		double typeScore = typeScore(rs1, rs2);
		double destScore = destScore(rs1, rs2);
		double timeScore = timeScore(rs1, rs2);
		return typeScore * destScore * timeScore;
	}
*/	
	public List<rideInfoParameters> filterByCoordinates(rideInfoParameters myArgs, int numRecords){
		// find the top few matching records (and return recordID's) based on coordinates.
		List<rideInfoParameters> results = new ArrayList<rideInfoParameters>();
		try {
			Connection con= userDBAccess.getConnection(false);
			Statement sql=con.createStatement();
			// TODO add check for commute and customize for commute and travel
			String query = "Select * from carpooltb where roundtrip=" + myArgs.roundtrip.toString();
			if(!myArgs.userType)
				query += " AND userType";
			// TODO add dayOfWeek logic
			ResultSet rs = sql.executeQuery(query);
			while(rs.next()){
				rideInfoParameters rsParamObj = new rideInfoParameters(rs);
				rsParamObj.score = destScoreByCoordinates(myArgs, rsParamObj) * timeScore(myArgs, rsParamObj);
				results.add(rsParamObj);
			}
    	} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:"+e.getMessage());
		} catch (SQLException e) {
			System.err.println("SQLException:"+e.getMessage());
		}
		Collections.sort(results);
		return results.subList(0, numRecords-1);
	}
}
