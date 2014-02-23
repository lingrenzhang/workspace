package com.hitchride.global;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Enumeration;

import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.MatchScore;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.User;
import com.hitchride.standardClass.UserInfo;

public class DummyData {
	private static DummyData dummy;
	
	private List<GeoInfo> _geoinfo;
	private List<User> _users;
	private List<OwnerRideInfo> _ownerRide;
	
	
	public Hashtable<Integer,ParticipantRide> _freeRides;  //All rides. Represent by RID.
	public Hashtable<Integer,ParticipantRide> _partRides;  //All available rides. Represent by RID.
	public Hashtable<Integer, MatchScore> _dummyMatching;
	public Hashtable<Integer, Message> _dummyMessage; //Attach message to ownerRide (topic) on initialize in production.
	public int _partRidesCount = 1;
	private int messageCount =0;
	
	private DummyData(){
		
		_geoinfo = new ArrayList<GeoInfo>();
		_users = new ArrayList<User>();
		_ownerRide = new ArrayList<OwnerRideInfo>();
        _partRides = new Hashtable<Integer,ParticipantRide>();
        _dummyMessage = new Hashtable<Integer,Message>();
        

		initializeGeo(10);
		initializeUser(); 
		initializeOwnerRide();
		initializeParticipantRide(5);
		initializeMessage(6);
	}
	

	private void initializeMessage(int numofMessages) {
		Random rnd = new Random();
		for (int i=0;i<numofMessages;i++)
		{
			Message msg = new Message("This is for test. I want to join your ride"
									,Environment.getEnv().getUser(rnd.nextInt(50))
									,Environment.getEnv().getUser(rnd.nextInt(50))
									,AllRides.getRides().getOwnerRide(838)
					);
			_dummyMessage.put(messageCount, msg);
			messageCount++;
			System.out.println("Msg "+ messageCount + ": initialized.");
			
			i++;
			msg = new Message(rnd.nextInt(4),rnd.nextInt(4)
					,Environment.getEnv().getUser(rnd.nextInt(50))
					,Environment.getEnv().getUser(rnd.nextInt(50))
					,AllRides.getRides().getOwnerRide(838)
					);
			_dummyMessage.put(messageCount, msg);
			messageCount++;
			System.out.println("Msg "+ messageCount + ": initialized.");
		}
	}

	private void initializeParticipantRide(int numofRides) {
		_partRides.clear();
		Random rnd = new Random();
		//Use OwnerRide as fakeParticipantRide
		for (int i=0;i<numofRides;i++)
		{
			int key = rnd.nextInt(2000);
			OwnerRideInfo ownerRide = AllRides.getRides().getOwnerRide(key);
			ParticipantRide pRide = new ParticipantRide(ownerRide._rideInfo);
			pRide.set_assoOwnerRideId(838);
			pRide.set_status(rnd.nextInt(4)+1);
			_partRides.put(key,pRide);
			
			i++;
			key = rnd.nextInt(2000);
			ownerRide = AllRides.getRides().getOwnerRide(key);
			pRide = new ParticipantRide(ownerRide._rideInfo);
			pRide.set_assoOwnerRideId(1838);
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


	public void updatePartRideStat(int prid, int pstatus) {
		ParticipantRide pride = _partRides.get(prid);
		pride.set_status(pstatus);
		
	}

	public void insert_message(Message message) {
		_dummyMessage.put(this.messageCount,message);
		messageCount++;
	}
	
	public void inser_pride(ParticipantRide part)
	{
		this._partRides.put(this._partRidesCount, part);
		
		part._rideinfo.recordId=this._partRidesCount;
		part._pid = this._partRidesCount;
		
		this._partRidesCount++;
	}
}
