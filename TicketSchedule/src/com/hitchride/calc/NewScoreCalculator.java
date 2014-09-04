package com.hitchride.calc;
import com.hitchride.GeoInfo;
import com.hitchride.CommuteOwnerRide;
import com.hitchride.CommuteRide;
import com.hitchride.CommuteTopic;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllTopics;
import com.hitchride.environ.SystemConfig;
import com.hitchride.util.DistanceHelper;
import com.hitchride.util.GeoUtil;
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
	
	private double timeScore(
			CommuteRide myRide,
			CommuteOwnerRide ownerRide 
	) {
		// TODO: also consider backTime for roundtrips
		// TODO: think of a non-boolean function
		// TODO: take into account time spent driving to pick-up location
		boolean retval = false;
		Time time1 = myRide.schedule.tripTime;
		Time flex1 = myRide.schedule.forwardFlexibility;
		Time time2 = ownerRide._rideInfo.schedule.returnTime;
		Time flex2 = ownerRide._rideInfo.schedule.forwardFlexibility;
		if(time2 == null || flex2 == null) return 0; // TODO: why should these guys be null in the first place? 
		retval = (timeToInt(flex1) + timeToInt(flex2)) > Math.abs(timeToInt(time1)-timeToInt(time2));
		return (retval ? 1 : 0);
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
			CommuteRide myRide,
			CommuteOwnerRide ownerRide, 
			boolean drivingDist // if false, calculate great circle distance only; if true, use google API to get driving distance
	) {
		// same for single trip and round trips, but have to consider who is driver and who is passenger
		GeoInfo origLoc1 = myRide.origLoc;
		GeoInfo destLoc1 = myRide.destLoc;
		boolean userType1 = myRide.userType;

		GeoInfo origLoc2 = ownerRide._rideInfo.origLoc;
		GeoInfo destLoc2 = ownerRide._rideInfo.destLoc;
		boolean userType2 = ownerRide._rideInfo.userType;

		// dist1 = driving distance for user 1 by himself
		// detourDist1 = total detour distance if user 1 is picking up user 2
		// detourDist1 == detourDist2 if drivingDist==false
		double dist1 = getDistance(origLoc1,destLoc1, drivingDist);
		double dist2 = getDistance(origLoc2, destLoc2, drivingDist);
		double detourDist1 = getDistance(origLoc1, origLoc2, drivingDist)
				   + getDistance(destLoc2, destLoc1, drivingDist);
		double detourDist2 = getDistance(origLoc2,origLoc1, drivingDist)
				   + getDistance(destLoc1, destLoc2,drivingDist);
				
		// define score to be 1-detour distance/smaller of the driver distance and passenger distance		
		double minDist = Math.max(Math.min(dist1, dist2), 1); // avoid divide-by-zeros 
		double score = Math.max(0, 1-Math.min(detourDist1, detourDist2)/minDist);
		return score;
	}
	
	public List<CommuteTopic> filterByCoordinates(CommuteRide myRide, int numRecords){
		// find the top few matching records (and return recordID's) based on coordinates.
		Enumeration<Integer> topicE = AllTopicRides.getTopicRides()._topicRides.keys();
		while(topicE.hasMoreElements()){
			CommuteOwnerRide topicRide = AllTopicRides.getTopicRides().getRide(topicE.nextElement());
			double score = destScoreByCoordinates(myRide, topicRide, false);
			insertResult(score,topicRide.id);
		}
		
		//return results.subList(0, numRecords-1);
		// turn off when exceeded API limit
		int sortNum = 5;
		return sortByDrivingDistance(myRide, sortNum);
	}
	
	// filter by coordinates first, then sort the filtered results by driving distance
	public List<CommuteTopic> sortByDrivingDistance(CommuteRide myRide, int sortNum){
		List<CommuteTopic> sortResults = new ArrayList<CommuteTopic>();
		sortNum = Math.min(sortNum,_scoreResults.size());
		for(int index = 0; index < sortNum; index++){
			ScoreResult scoreResult = _scoreResults.get(index);
			CommuteOwnerRide ownerRide = AllTopicRides.getTopicRides().getRide(scoreResult._topicId);
			if (ownerRide!=null)
			{	
				double destMultiplier = destScoreByCoordinates(myRide, ownerRide, true);
				double timeMultiplier = timeScore(myRide, ownerRide);
				scoreResult._score = destMultiplier;
			}
			else
			{
				scoreResult._score= -1.0;
			}
		}
		Collections.sort(_scoreResults);
		
		for(int index = 0; index < _scoreResults.size(); index++){
			ScoreResult scoreResult = _scoreResults.get(index);
			CommuteTopic topicRide =  AllTopics.getTopics().get_topic(scoreResult._topicId);
			if (topicRide!=null)
			{
				sortResults.add(topicRide);	
			}
		}
		return sortResults;
	}
}
