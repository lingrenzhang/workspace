
package com.hitchride.standardClass;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


import com.hitchride.global.AllRides;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
//This class is to represent the standard format of dataStructure for Message Box.
//Shield the front end from direct access to DB. Using runtime object for performance increase
//and de-couple physical persistent record with runtime server.  
//Later will put the MessageInfo as list in the runtime. 
//Any fetch will first check the MessageInfo from memory. Use regular commit to persistent storage.
//Has relation to both carpoolTb and messageTb in terms of persistent storage. Easier the table relation in mySQL.
//Not sure the detailed mySQL multi-table relationship supporting level.
//Think about more complicate use like trigger, cascade, etc later when necessary. 
public class Topic implements PersistentStorage{
	private int _topicId; 	//Same as owner ID
	public User owner;
	//public List<Participant> participants;
	public OwnerRideInfo ownerRide;
	public List<ParticipantRide> parRides;
	public List<ParticipantRide> _requestPride = new ArrayList<ParticipantRide>();

	public List<Message> messages;      

	//Persistent Storage related
	boolean _isChanged;
	boolean _isSaved;
 
	public Topic(int rid) {
		try {
				ownerRide = AllRides.getRides()._topicRides.get(rid);
				this._topicId=rid;
				UserInfo ownerInfo = Environment.getEnv().getUser(ownerRide._rideInfo.username);
				owner =(User) ownerInfo;
						
				parRides = new ArrayList<ParticipantRide>();
				//TO DO: User dummy participant ride now
				Enumeration<Integer> e =DummyData.getDummyEnv().getAllPartRide();
				while (e.hasMoreElements())
				{
					Integer key = e.nextElement();
					ParticipantRide pRide = DummyData.getDummyEnv().get_participantRide(key);
					if (pRide.get_assoOwnerRideId()==ownerRide._rideInfo.recordId)
					{
						if (pRide.get_status()==1)
						{
							_requestPride.add(pRide);
						}
						else
						{
							parRides.add(pRide);
						}
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
			System.out.println("Illegal rid: " + rid);
			System.out.println("Please verify database integrity or topic load routine.");
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
	
	public ParticipantRide getpRideByuserId(int userId)
	{
		ParticipantRide result;
		for (Iterator<ParticipantRide> pRideI = _requestPride.iterator(); pRideI.hasNext();)
		{
			result = pRideI.next();
			if (result.get_userId() == userId)
			{
				return result;
			}
		}
		for (Iterator<ParticipantRide> pRideI = parRides.iterator(); pRideI.hasNext();)
		{
			result = pRideI.next();
			if (result.get_userId() == userId)
			{
				return result;
			}
		}
		return null;
	}
	//TO DO: Update persistent storage if found status flag is 1 in finalizer.
	
	public String getHTML()
	{
		StringBuilder result = new StringBuilder();
		result.append("<a href=\"./RideCenter?topicId="+this._topicId +"&type=commute\">");
		result.append("<div class=\"entry\" origLat="+this.ownerRide.origLoc.get_lat()+" ");
		result.append("origLng=" +  this.ownerRide.origLoc.get_lon()+" ");
		result.append("destLat=" +  this.ownerRide.destLoc.get_lat()+" ");
		result.append("destLng=" +  this.ownerRide.destLoc.get_lon()+">");
		if (this.ownerRide.userType)
		{
			result.append("<div class=\"passenger_box\"><p>");
			result.append("<span class=\"icon\"></span>");
			result.append(this.owner.get_name()+" is a <strong>passenger</strong></p></div>");
		}
		else{
			result.append("<div class=\"price_box\"><div class=\"seats\">");
			result.append("<span class=\"count\">"+this.ownerRide.seatsAvailable+"</span></div>");
			result.append("<p><b>"+this.ownerRide.price + "</b> / seat</p></div>");
		}
		result.append("<div class=\"userpic\">");
		result.append("<div class=\"username\">"+this.owner.get_name()+"</div>");
		result.append("<img src= \"/TicketSchedule/UserProfile/"+this.owner.get_avatarID()+"\" alt=\"Profile Picture\"></img>");
		result.append("<span class=\"passenger\"></span></div>");
		result.append("<div class=\"inner_content\"><h3>");
		result.append("<span class=\"inner\">"+this.ownerRide.origLoc._addr);
		result.append("<span class=\"trip_type round_trip\"></span>");
		result.append(this.ownerRide.destLoc._addr+"</span></h3><h4>");
        result.append("From: "+this.ownerRide.origLoc.get_formatedAddr());
        result.append("To: "+this.ownerRide.destLoc.get_formatedAddr());
        result.append("</h4></div></div></a>");
		return result.toString();
	}


	@Override
	public void insertToDB() {
		
		
	}


	@Override
	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isSaved() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean lastCheckpoint() {
		// TODO Auto-generated method stub
		return false;
	}
}
