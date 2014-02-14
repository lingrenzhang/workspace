package com.hitchride.global;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import java.util.Enumeration;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.User;

public class DummyData {
	private static DummyData dummy;
	
	private List<GeoInfo> _geoinfo;
	private List<User> _users;
	private List<OwnerRideInfo> _ownerRide;
	
	public Hashtable<Integer,ParticipantRide> _partRides;  //All available rides. Represent by RID.
	
	private DummyData(){
		
		_geoinfo = new ArrayList<GeoInfo>();
		_users = new ArrayList<User>();
		_ownerRide = new ArrayList<OwnerRideInfo>();
        _partRides = new Hashtable<Integer,ParticipantRide>();
		initializeGeo(10);
		initializeUser(); 
		initializeOwnerRide();
		initializeParticipantRide(4);
		initializeMessage();
	}
	
	private void initializeMessage() {
		
		
	}

	private void initializeParticipantRide(int numofRides) {
		_partRides.clear();
		Random rnt = new Random();
		//Use OwnerRide as fakeParticipantRide
		for (int i=0;i<numofRides;i++)
		{
			int key = rnt.nextInt(2000);
			RideInfo part = Environment.getEnv().getOwnerRide(key);
			ParticipantRide pRide = new ParticipantRide(part);
			_partRides.put(key,pRide);
			
		}
		
	}

	private void initializeOwnerRide() {

		
	}

	private void initializeUser() {
		// TODO Auto-generated method stub
		
	}

	private void initializeGeo(int numberofGeos) {
			Random rnt = new Random();
			this._geoinfo.clear();
			for (int i=0;i<numberofGeos;i++)
			{
				
				GeoInfo geo = new GeoInfo("SampleLocation"+i,37+rnt.nextDouble(),-122+rnt.nextDouble());
				geo.geoId = i;
				this._geoinfo.add(geo);
			}
	}
		
	

	public static DummyData getDummyEnv(){
		if (null == dummy){
			dummy = new DummyData();

		}
		return dummy;
	}

	public ParticipantRide get_participantRide(int Ride) {
		ParticipantRide partRide = _partRides.get(Ride);
		return partRide;
	}

	public Enumeration<Integer> getAllPartRide() {
		
		return _partRides.keys();
	}

	
}
