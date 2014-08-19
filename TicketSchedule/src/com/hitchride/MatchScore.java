package com.hitchride;

import java.util.Random;

import com.hitchride.environ.SystemConfig;
import com.hitchride.util.DistanceHelper;


public class MatchScore implements IMatching {

	private int _LocationMatching;
	private int _ScheduleingMatching;
	private int _BargingMatching;
	
	private CommuteRide _ownerRide;
	private CommuteRide _partRide;
	public MatchScore()
	{
		Random rnd = new Random();
		this._LocationMatching = rnd.nextInt(100);
		this._ScheduleingMatching = rnd.nextInt(100);
		this._BargingMatching = rnd.nextInt(100);
	}
	
	public MatchScore(int GeoMatching,int ScheduleMatching, int BarginMatching)
	{
		this._LocationMatching = GeoMatching;
		this._ScheduleingMatching = ScheduleMatching;
		this._BargingMatching = BarginMatching;
	}	
	@Override
	public void ComputeMatching(CommuteOwnerRide ownerRide,
			CommuteParticipantRide partRide) {
		this._LocationMatching = (int) (destScoreByCoordinates(ownerRide, partRide, false)*100.0);
		//TODO: calculate scheduleingMatching and BargingMatching
	}
	
	private double getDistance(GeoInfo origLoc,GeoInfo destLoc, boolean drivingDist) {
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
			CommuteOwnerRide ownerRide, 
			CommuteParticipantRide partRide, 
			boolean drivingDist // if false, calculate great circle distance only; if true, use google API to get driving distance
	) 
	{
		// same for single trip and round trips, but have to consider who is driver and who is passenger
		GeoInfo origLoc1 = ownerRide._rideInfo.origLoc;
		GeoInfo destLoc1 = ownerRide._rideInfo.destLoc;
		//boolean userType1 = myArgs.userType;

		GeoInfo origLoc2 = partRide._rideInfo.origLoc;
		GeoInfo destLoc2 = partRide._rideInfo.destLoc;
		//boolean userType2 = rsParamObj.userType;

		// dist1 = driving distance for user 1 by himself
		// detourDist1 = total detour distance if user 1 is picking up user 2
		// detourDist1 == detourDist2 if drivingDist==false
		double dist1 = getDistance(origLoc1,destLoc1, drivingDist);
		double dist2 = getDistance(origLoc2, destLoc2, drivingDist);
		double detourDist1 = getDistance(origLoc1, origLoc2, drivingDist)
				   + getDistance(destLoc2, destLoc1, drivingDist);
		double detourDist2 = getDistance(origLoc2,origLoc1, drivingDist)
				   + getDistance(destLoc1, destLoc2,drivingDist);

		// dist1 = driving distance for user 1 by himself
		// detourDist1 = total detour distance if user 1 is picking up user 2
		// detourDist1 == detourDist2 if drivingDist==false
		
		
		
		// define score to be 1-detour distance/smaller of the driver distance and passenger distance		
		double minDist = Math.max(Math.min(dist1, dist2), 1); // avoid divide-by-zeros 
		double score = Math.max(0, 1-Math.min(detourDist1, detourDist2)/minDist);
		return score;
	}

	@Override
	public int getLocationMatching() {
		return this._LocationMatching;
	}

	@Override
	public int getSchedulingMatching() {
		return this._ScheduleingMatching;
	}

	@Override
	public int getBarginMatching() {
		return this._BargingMatching;
	}


	@Override
	public void QuickGeoCompute(CommuteRide orig, CommuteRide partRide) {

		
	}

}
