//Use google geoapi to generate dummy commute RideInfo and TopicRide
package com.hitchride.dbBatchLoad;

import java.io.IOException;
import java.util.Random;

import org.json.JSONException;

import com.hitchride.access.RideInfoAccess;
import com.hitchride.access.TopicRideAccess;
import com.hitchride.access.TopicTbAccess;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.Topic;

public class GenerateDummyCommuteTopic {
	static double radtodistance = 6371*1000/180*Math.PI;

	double _topleftlat,_topleftlng;
	double _bottomrightlat,_bottomrightlng;
	int _numberofUser=1000;
	int _numberofRides=1000;
	int _rideLengthRange = 10000;
	int _scaleFactor=2; //rideLengthRange/2 - rideLenthRange*2
	GeoUtil geoUtil;
	
	public GenerateDummyCommuteTopic(double topleftlat,double topleftlng,double bottomrightlat,double bottomrightlng )
	{
		this._topleftlat = topleftlat;
		this._topleftlng = topleftlng;
		this._bottomrightlat = bottomrightlat;
		this._bottomrightlng = bottomrightlng;
		geoUtil = new GeoUtil();
	}
	
	public void generateDummyRide(int number) throws IOException
	{
		Random rnd = new Random();
		for (int i=0;i<number;i++)
		{
			double origlat = _bottomrightlat+ (_topleftlat-_bottomrightlat)*rnd.nextDouble();
			double origlng = _topleftlng +(_bottomrightlng - _topleftlng)*rnd.nextDouble();
			double r = _rideLengthRange/_scaleFactor+(_rideLengthRange*_scaleFactor)*rnd.nextDouble();
			double angle = 2*Math.PI*rnd.nextDouble();
			double destlat = origlat+Math.sin(angle)*r/radtodistance;
			double destlng = origlng+Math.cos(angle)*r/radtodistance;
			
			//Register user:
			int userId = rnd.nextInt(_numberofUser);
			try
			{
			    //GeoInfo origLoc = geoUtil.reverseGeoCoding(origlat, origlng);
				GeoInfo origLoc = geoUtil.baiduGeoCoding(origlat, origlng);
				//GeoInfo destLoc = geoUtil.reverseGeoCoding(destlat, destlng);
				GeoInfo destLoc = geoUtil.baiduGeoCoding(destlat, destlng);
			    Schedule schedule=new Schedule(true);
			    RideInfo ride = new RideInfo();
			    ride.userId=userId;
			    ride.origLoc = origLoc;
			    ride.destLoc = destLoc;
			    ride.schedule= schedule;
			    ride.userType = rnd.nextBoolean();
			    ride.availSeats = rnd.nextInt(4);
			    ride.totalSeats = 4;
			    ride.price = r/2000;
			    RideInfoAccess.insertRideInfo(ride);
			    int rid = RideInfoAccess.getMaxRideId();
			    ride.recordId = rid;
			    OwnerRideInfo topicRide= new OwnerRideInfo(ride);
			    TopicRideAccess.insertTopicRide(topicRide);
			    Topic topic = new Topic(topicRide._recordId);
			    TopicTbAccess.insertTopic(topic);
			}
			catch (JSONException e)
			{
				System.out.println(e.getMessage());
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
   
	}
	
	public static void main(String[] args) throws IOException 
	{   //Bayarea
		//GenerateDummyCommuteTopic genDummy = new GenerateDummyCommuteTopic(37.511,-122.211,37.211,-121.911);
		//Shanghai
		GenerateDummyCommuteTopic genDummy = new GenerateDummyCommuteTopic(31.35,121.3,31.1,121.8);
		genDummy.generateDummyRide(1);
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
}
