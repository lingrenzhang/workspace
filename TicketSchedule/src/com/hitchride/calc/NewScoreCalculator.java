package com.hitchride.calc;
import com.hitchride.dbBatchLoad.GeoUtil;
import com.hitchride.global.AllRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllTopics;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Topic;
import com.hitchride.calc.rideInfoParameters;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.json.*;

//This calculator serves for runtime.
public class NewScoreCalculator {
	static final int maxResult =100;
	public Vector<ScoreResult> _scoreResults = new Vector<ScoreResult>(maxResult+1); //Do not let it allocate new buffer.
	double minScore = 0;
	int _resultCount = 1;
	
	class ScoreResult implements Comparable<ScoreResult>
	{
		Double _score;
		int _topicId; //ownerRideId
		public ScoreResult(double score,int topicId)
		{
			this._score = score;
			this._topicId =topicId;
		}
		@Override
		public int compareTo(ScoreResult that) {
			return -(this._score).compareTo(that._score);
		}
	}
	
	public NewScoreCalculator()
	{
		ScoreResult scoreResult = new ScoreResult(0,-1);
		_scoreResults.add(scoreResult); //The last result;
	}
	
	private void insertResult(double score,int topicId) //Insert to the searchResult
	{
		if (score<=minScore)
		{
			return;
		}
		for(int i=0;i<_resultCount;i++)
		{
			if (score>_scoreResults.elementAt(i)._score)
			{
				ScoreResult scoreResult = new ScoreResult(score,topicId);
				_scoreResults.add(i,scoreResult);
				break;
			}
		}
		if (_resultCount<maxResult)
		{	
			_resultCount++;
		}
		else
		{
			_scoreResults.remove(_resultCount-1);
		}
	}
	
	private int timeToInt(Time time){
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
	
	private double getDistance(double lat1, double lon1, double lat2, double lon2, boolean drivingDist) {
		if(drivingDist){
			GeoUtil geoUtilObj = new GeoUtil(lat1, lon1, lat2, lon2);
			try {
				geoUtilObj.getDisDua();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return geoUtilObj.distance;
		}
		return greatCircleDistance(lat1, lon1, lat2, lon2);
	}
	
	private double destScoreByCoordinates(
			RideInfo myRide,
			OwnerRideInfo ownerRide, 
			boolean drivingDist // if false, calculate great circle distance only; if true, use google API to get driving distance
	) {
		// same for single trip and round trips, but have to consider who is driver and who is passenger
		double score = 0; // avoid negative scores
		double origLat1 = myRide.origLoc.get_lat();
		double origLon1 = myRide.origLoc.get_lon();
		double destLat1 = myRide.destLoc.get_lat();
		double destLon1 = myRide.destLoc.get_lon();
		boolean userType1 = myRide.userType;

		double origLat2 = ownerRide._rideInfo.origLoc.get_lat();
		double origLon2 = ownerRide._rideInfo.origLoc.get_lon();
		double destLat2 = ownerRide._rideInfo.destLoc.get_lat();
		double destLon2 = ownerRide._rideInfo.destLoc.get_lon();
		boolean userType2 = ownerRide._rideInfo.userType;

		// dist1 = driving distance for user 1 by himself
		// pickUpDist1 = total driving distance for user 1 if user 1 has to pick up user 2
		double dist1 = getDistance(origLat1, origLon1, destLat1, destLon1, drivingDist);
		double dist2 = getDistance(origLat2, origLon2, destLat2, destLon2, drivingDist);
		double pickUpDist1 = getDistance(origLat1, origLon1, origLat2, origLon2, drivingDist)
				   + dist2 + getDistance(destLat2, destLon2, destLat1, destLon1, drivingDist);
		double pickUpDist2 = getDistance(origLat2, origLon2, origLat1, origLon1, drivingDist)
				   + dist1 + getDistance(destLat1, destLon1, destLat2, destLon2, drivingDist);
				
		// define score to be 1-detour distance/smaller of the driver distance and passenger distance		
		double minDist = Math.max(Math.min(dist1, dist2), 1);
		
		if(userType1) score = Math.max(score, 1-(pickUpDist1-dist1)/minDist);
		if(userType2) score = Math.max(score, 1-(pickUpDist2-dist2)/minDist);
		
		return score;
	}
	
	public List<Topic> filterByCoordinates(RideInfo myRide, int numRecords){
		// find the top few matching records (and return recordID's) based on coordinates.
		Enumeration<Integer> topicE = AllTopicRides.getTopicRides()._topicRides.keys();
		while(topicE.hasMoreElements()){
			OwnerRideInfo topicRide = AllTopicRides.getTopicRides().getRide(topicE.nextElement());
			double score = destScoreByCoordinates(myRide, topicRide, false);
			insertResult(score,topicRide._recordId);
		}
		
		//return results.subList(0, numRecords-1);
		// turn off when exceeded API limit
		int sortNum = 5;
		return sortByDrivingDistance(myRide, sortNum);
	}
	
	// filter by coordinates first, then sort the filtered results by driving distance
	public List<Topic> sortByDrivingDistance(RideInfo myRid, int sortNum){
		List<Topic> sortResults = new ArrayList<Topic>();
		for(int index = 0; index < sortNum; index++){
			ScoreResult scoreResult = _scoreResults.get(index);
			OwnerRideInfo ownerRide = AllTopicRides.getTopicRides().getRide(scoreResult._topicId);
			scoreResult._score = destScoreByCoordinates(myRid, ownerRide, true);
		}
		Collections.sort(_scoreResults);
		
		for(int index = 0; index < _scoreResults.size(); index++){
			ScoreResult scoreResult = _scoreResults.get(index);
			Topic topicRide =  AllTopics.getTopics().get_topic(scoreResult._topicId);
			if (topicRide!=null)
			{
				sortResults.add(topicRide);	
			}
		}
		return sortResults;
	}
}
