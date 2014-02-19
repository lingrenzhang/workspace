
package com.hitchride.standardClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.hitchride.access.CarpoolTbAccess;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
import com.mysql.jdbc.ResultSet;
//This class is to represent the standard format of dataStructure for Message Box.
//Shield the front end from direct access to DB. Using runtime object for performance increase
//and de-couple physical persistent record with runtime server.  
//Later will put the MessageInfo as list in the runtime. 
//Any fetch will first check the MessageInfo from memory. Use regular commit to persistent storage.
//Has relation to both carpoolTb and messageTb in terms of persistent storage. Easier the table relation in mySQL.
//Not sure the detailed mySQL multi-table relationship supporting level.
//Think about more complicate use like trigger, cascade, etc later when necessary. 
public class Topic {
	private int _topicId; 	//Same as owner ID
	public User owner;
	//public List<Participant> participants;
	public OwnerRideInfo ownerRide;
	public List<ParticipantRide> parRides;
	public List<ParticipantRide> _requestPride = new ArrayList<ParticipantRide>();

	public List<Message> messages;      

 
	public Topic(int rid) {
		try {
			ownerRide = Environment.getEnv()._topicRides.get(rid);
			this._topicId=rid;
			UserInfo ownerInfo = Environment.getEnv().getUser(ownerRide._rideInfo.username);
			owner =(User) ownerInfo;
					
			parRides = new ArrayList<ParticipantRide>();
			//parRides = ownerRide.get_prides();
			//TO DO: User dummy participant ride now
			Enumeration<Integer> e =DummyData.getDummyEnv().getAllPartRide();
			while (e.hasMoreElements())
			{
				Integer key = e.nextElement();
				ParticipantRide pRide = DummyData.getDummyEnv().get_participantRide(key);
				if (pRide.get_assoOwnerRideId()==ownerRide._rideInfo.recordId)
				{
					parRides.add(pRide);
				}
			}
			
			messages = new ArrayList<Message>();
			e =DummyData.getDummyEnv()._dummyMessage.keys();
			while (e.hasMoreElements())
			{
				Integer key = e.nextElement();
				Message message = DummyData.getDummyEnv()._dummyMessage.get(key);
				if (message.getOwnerRide()._rideInfo.recordId ==ownerRide._rideInfo.recordId)
				{
					messages.add(message);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	//Switch users in DB to users in MessageInfo class
	public void loadUsers(String users)
	{
		//Reference format: xiaoa,0;xiaob,1;xiaoc,0;...
		try
		{
			String[] _us = users.split(";");
			for (int i=0;i<_us.length;i++)
			{
				String[] ps = _us[i].split(",");
				String name = ps[0];
				Integer status = Integer.parseInt(ps[1]);
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.out.println("Warning: Malformat of user field in CarpoolDB.");
		}
		
	}


	public int get_topicId() {
		return _topicId;
	}


	public void set_topicId(int _topicId) {
		this._topicId = _topicId;
	}
	
	//TO DO: Update persistent storage if found status flag is 1 in finalizer.
	
}
