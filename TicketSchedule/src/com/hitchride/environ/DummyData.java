package com.hitchride.environ;

import java.util.Hashtable;

import com.hitchride.MatchScore;
import com.hitchride.Message;
import com.hitchride.database.access.MessageTbAccess;


public class DummyData {
	private static DummyData dummy;
	
	public Hashtable<Integer, MatchScore> _dummyMatching;
	public Hashtable<Integer, Message> _dummyMessage; //Attach message to ownerRide (topic) on initialize in production.
	private int messageCount =0;
	
	private DummyData(){
        _dummyMessage = new Hashtable<Integer,Message>(); //Think about move out message in memory later
		initializeMessage();
	}
	

	private void initializeMessage() {
		_dummyMessage = MessageTbAccess.LoadAllMessage();
	}

	

	public static DummyData getDummyEnv(){
		if (null == dummy){
			dummy = new DummyData();

		}
		return dummy;
	}

	public void insert_message(Message message) {
		_dummyMessage.put(this.messageCount,message);
		messageCount++;
	}
	
	
}