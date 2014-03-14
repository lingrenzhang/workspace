package com.hitchride.global;

import java.util.Hashtable;
import java.util.Random;
import java.util.Enumeration;

import com.hitchride.access.MessageTbAccess;
import com.hitchride.standardClass.MatchScore;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.ParticipantRide;


public class DummyData {
	private static DummyData dummy;
	
	public Hashtable<Integer,ParticipantRide> _freeRides;  //All rides. Represent by RID.
	public Hashtable<Integer,ParticipantRide> _partRides;  //All available rides. Represent by RID.
	public Hashtable<Integer, MatchScore> _dummyMatching;
	public Hashtable<Integer, Message> _dummyMessage; //Attach message to ownerRide (topic) on initialize in production.
	public int _partRidesCount = 1;
	private int messageCount =0;
	
	private DummyData(){
        _partRides = new Hashtable<Integer,ParticipantRide>();
        _dummyMessage = new Hashtable<Integer,Message>(); //Think about move out message in memory later
        

		initializeParticipantRide();
		initializeMessage();
	}
	

	private void initializeMessage() {
		_dummyMessage = MessageTbAccess.LoadAllMessage();
	}

	private void initializeParticipantRide() {
		_partRides.clear();
		Random rnd = new Random();
		//Use OwnerRide as fakeParticipantRide
		/*
		for (int i=0;i<numofRides;i++)
		{
			
			int key = rnd.nextInt(2000);
			OwnerRideInfo ownerRide = AllRides.getRides().getOwnerRide(key);
			ParticipantRide pRide = new ParticipantRide(ownerRide._rideInfo);
			pRide.set_assoOwnerRideId(838);
			pRide.set_status(rnd.nextInt(4)+1);
			pRide.get_user().rides.add(pRide);
			_partRides.put(key,pRide);
			AllRides.getRides()._topicRides.remove(key);
			
		}
		*/
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
