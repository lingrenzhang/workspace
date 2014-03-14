package com.hitchride.standardClass;

import java.io.IOException;
import java.util.Random;

import org.json.JSONException;

import com.hitchride.dbBatchLoad.GeoUtil;

public class MatchScore implements Matching {

	private int _LocationMatching;
	private int _ScheduleingMatching;
	private int _BargingMatching;
	
	private RideInfo _ownerRide;
	private RideInfo _partRide;
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
	public void ComputeMatching(OwnerRideInfo ownerRide,
			ParticipantRide partRide) {
		this._LocationMatching = (int) (destScoreByCoordinates(ownerRide, partRide, false)*100.0);
		//TODO: calculate scheduleingMatching and BargingMatching
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
			OwnerRideInfo ownerRide, 
			ParticipantRide partRide, 
			boolean drivingDist // if false, calculate great circle distance only; if true, use google API to get driving distance
	) 
	{
		// same for single trip and round trips, but have to consider who is driver and who is passenger
		double score = 0; // avoid negative scores
		double origLat1 = ownerRide.origLoc.get_lat();
		double origLon1 = ownerRide.origLoc.get_lon();
		double destLat1 = ownerRide.destLoc.get_lat();
		double destLon1 = ownerRide.destLoc.get_lon();
		//boolean userType1 = myArgs.userType;

		double origLat2 = partRide.origLoc.get_lat();
		double origLon2 = partRide.origLoc.get_lon();
		double destLat2 = partRide.destLoc.get_lat();
		double destLon2 = partRide.destLoc.get_lon();
		//boolean userType2 = rsParamObj.userType;

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
//		if(userType1) 
		score = Math.max(score, 1-(pickUpDist1-dist1)/minDist);
//		if(userType2) 
		score = Math.max(score, 1-(pickUpDist2-dist2)/minDist);
		
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
	public void QuickGeoCompute(RideInfo orig, RideInfo partRide) {

		
	}

}
