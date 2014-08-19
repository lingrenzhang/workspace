package com.hitchride.calc;
import com.hitchride.GeoInfo;
import com.hitchride.util.DistanceHelper;
import com.hitchride.util.GeoUtil;
import com.hitchride.calc.rideInfoParameters;
import com.hitchride.database.access.CarpoolTbAccess;
import com.hitchride.environ.SystemConfig;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.json.*;

@Deprecated
public class ScoreCalculator {
	private int timeToInt(Time time){
		// TODO: what's the best way to add/subtract java.sql.time?
		return time.getHours()*3600 + time.getMinutes()*60 + time.getSeconds();
	}
	
	private double timeScore(rideInfoParameters myArgs, rideInfoParameters rsParamObj) {
//		return 1; // only consider destinations, like zimride
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
		//double R = 6371/1.6; // mile
		double R = 6371*1000; // meters
		return R*c;
	}
	
	private double getDistance(GeoInfo origLoc, GeoInfo destLoc, boolean drivingDist) {
		DistanceHelper distCompute = null;
		if (drivingDist && SystemConfig.geoApiSet == 0 )
		{
			distCompute = new DistanceHelper(origLoc,destLoc,1);
		}
		if (drivingDist && SystemConfig.geoApiSet == 1)
		{
			distCompute = new DistanceHelper(origLoc,destLoc,2);
		}
		if (!drivingDist || distCompute == null){
			distCompute = new DistanceHelper(origLoc,destLoc,0);
		}
		distCompute.computeResult();
		return distCompute.getDistance();
	}
	
	private double destScoreByCoordinates(
			rideInfoParameters myArgs, 
			rideInfoParameters rsParamObj, 
			boolean drivingDist // if false, calculate great circle distance only; if true, use google API to get driving distance
	) {
		// same for single trip and round trips, but have to consider who is driver and who is passenger
		double score = 0; // avoid negative scores
		GeoInfo origLoc1 = new GeoInfo(myArgs.origLat,myArgs.origLon);
		GeoInfo destLoc1 = new GeoInfo(myArgs.destLat,myArgs.destLon);
		boolean userType1 = myArgs.userType;

		GeoInfo origLoc2 = new GeoInfo(rsParamObj.origLat,rsParamObj.origLon);
		GeoInfo destLoc2 = new GeoInfo(rsParamObj.destLat,rsParamObj.destLon);
		boolean userType2 = rsParamObj.userType;

		// dist1 = driving distance for user 1 by himself
		// pickUpDist1 = total driving distance for user 1 if user 1 has to pick up user 2
		double dist1 = getDistance(origLoc1,destLoc1, drivingDist);
		double dist2 = getDistance(origLoc2, destLoc2, drivingDist);
		double pickUpDist1 = getDistance(origLoc1, origLoc2, drivingDist)
				   + dist2 + getDistance(destLoc2, destLoc1, drivingDist);
		double pickUpDist2 = getDistance(origLoc2,origLoc1, drivingDist)
				   + dist1 + getDistance(destLoc1, destLoc2,drivingDist);
				
		// define score to be 1-detour distance/smaller of the driver distance and passenger distance		
		double minDist = Math.max(Math.min(dist1, dist2), 1);
		if(userType1) score = Math.max(score, 1-(pickUpDist1-dist1)/minDist);
		if(userType2) score = Math.max(score, 1-(pickUpDist2-dist2)/minDist);
		
		return score;
	}
	
	public List<rideInfoParameters> filterByCoordinates(rideInfoParameters myArgs, int numRecords){
		// find the top few matching records (and return recordID's) based on coordinates.
		List<rideInfoParameters> results = new ArrayList<rideInfoParameters>();
		try {
			Connection con= CarpoolTbAccess.getConnection();
			Statement sql=con.createStatement();
			String tbName = (myArgs.commute) ? "carpoolTb" : "travelTb"; // traveltb still under construction
			String query = "Select * from " + tbName + " where roundtrip=" + myArgs.roundtrip.toString();
			if(!myArgs.userType)
				query += " AND userType";
			// TODO add dayOfWeek logic
			ResultSet rs = sql.executeQuery(query);
			while(rs.next()){
				rideInfoParameters rsParamObj = new rideInfoParameters(rs, myArgs.commute);
				rsParamObj.score = destScoreByCoordinates(myArgs, rsParamObj, false) * timeScore(myArgs, rsParamObj);
//				System.out.println(rsParamObj.score);
				results.add(rsParamObj);
			}
    	} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:"+e.getMessage());
		} catch (SQLException e) {
            Error err=new Error("SQLException:"+e.getMessage());
            throw err;
			//System.err.println("SQLException:"+e.getMessage());
		}
		Collections.sort(results);
		
		//return results.subList(0, numRecords-1);
		// turn off when exceeded API limit
		int sortNum = 5;
		return sortByDrivingDistance(results.subList(0, numRecords-1), myArgs, sortNum);
	}
	
	// filter by coordinates first, then sort the filtered results by driving distance
	public List<rideInfoParameters> sortByDrivingDistance(List<rideInfoParameters> results, rideInfoParameters myArgs, int sortNum){
		List<rideInfoParameters> sortResults = new ArrayList<rideInfoParameters>();
		for(int index = 0; index < sortNum; index++){
			rideInfoParameters rsParamObj = results.get(index);
			rsParamObj.score = destScoreByCoordinates(myArgs, rsParamObj, true) * timeScore(myArgs, rsParamObj);
			sortResults.add(rsParamObj);
		}
		Collections.sort(sortResults);
		for(int index = sortNum; index < results.size(); index++){
			rideInfoParameters rsParamObj = results.get(index);
			sortResults.add(rsParamObj);		
		}
		return sortResults;
	}
}
