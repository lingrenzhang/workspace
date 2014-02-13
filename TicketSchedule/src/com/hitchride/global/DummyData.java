package com.hitchride.global;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.User;

public class DummyData {
	private static DummyData dummy;
	
	private List<GeoInfo> _geoinfo = new ArrayList<GeoInfo>();
	private List<User> _users = new ArrayList<User>();
	private List<OwnerRideInfo> _ownerRide = new ArrayList<OwnerRideInfo>();
	private List<ParticipantRide> _participantRide = new ArrayList<ParticipantRide>();
	
	private DummyData(){
		initializeGeo(10);
		initializeUser(); 
		initializeOwnerRide();
		initializeParticipantRide();
		initializeMessage();
	}
	
	private void initializeMessage() {
		
		
	}

	private void initializeParticipantRide() {
		// TODO Auto-generated method stub
		
	}

	private void initializeOwnerRide() {
		// TODO Auto-generated method stub
		
	}

	private void initializeUser() {
		// TODO Auto-generated method stub
		
	}

	private void initializeGeo(int numberofGeos) {
			Random rnt = new Random();
			List<GeoInfo> results = new ArrayList<GeoInfo>();
			for (int i=0;i<numberofGeos;i++)
			{
				
				GeoInfo geo = new GeoInfo("SampleLocation"+i,37+rnt.nextDouble(),-122+rnt.nextDouble());
				geo.geoId = i;
				results.add(geo);
			}
	}
		
	

	public static DummyData getDummyEnv(){
		if (null == dummy){
			dummy = new DummyData();
		}
		return dummy;
	}
	
	
	
	
}
