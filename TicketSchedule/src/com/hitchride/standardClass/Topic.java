
package com.hitchride.standardClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.hitchride.access.TopicTbAccess;
import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllUsers;
import com.hitchride.global.DummyData;
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
	public List<ParticipantRide> _requestPride;
	public List<Message> messages = new ArrayList<Message>();;      

    public Topic(){
    	//Used when load from DB.
    	parRides = new ArrayList<ParticipantRide>();
    	_requestPride = new ArrayList<ParticipantRide>();
    	
    }
	public Topic(int rid) {
		try {
				parRides = new ArrayList<ParticipantRide>();
		    	_requestPride = new ArrayList<ParticipantRide>();
				ownerRide = AllTopicRides.getTopicRides()._topicRides.get(rid);
				this._topicId=rid;
				UserInfo ownerInfo = AllUsers.getUsers().getUser(ownerRide._rideInfo.username);
				owner =(User) ownerInfo;
						

				//TO DO: User dummy participant ride now
				Enumeration<Integer> e = AllPartRides.getPartRides().getAllPartRide();
				while (e.hasMoreElements())
				{
					Integer key = e.nextElement();
					ParticipantRide pRide = AllPartRides.getPartRides().get_participantRide(key);
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
		StringBuilder result = new StringBuilder(2000);
		result.append("<a href=\"./RideCenter?topicId="+this._topicId +"&type=commute\">");
		result.append("<div class=\"entry\" origLat="+this.ownerRide._rideInfo.origLoc.get_lat()+" ");
		result.append("origLng=" +  this.ownerRide._rideInfo.origLoc.get_lon()+" ");
		result.append("destLat=" +  this.ownerRide._rideInfo.destLoc.get_lat()+" ");
		result.append("destLng=" +  this.ownerRide._rideInfo.destLoc.get_lon()+">");
		if (this.ownerRide._rideInfo.userType)
		{
			result.append("<div class=\"passenger_box\"><p>");
			result.append("<span class=\"icon\"></span>");
			result.append(this.owner.get_name()+" is a <strong>passenger</strong></p></div>");
		}
		else{
			result.append("<div class=\"price_box\"><div class=\"seats\">");
			result.append("<span class=\"count\">"+this.ownerRide._rideInfo.totalSeats+"</span></div>");
			result.append("<p><b>"+this.ownerRide._rideInfo.price + "</b> / seat</p></div>");
		}
		result.append("<div class=\"userpic\">");
		result.append("<div class=\"username\">"+this.owner.get_name()+"</div>");
		result.append("<img src= \"/TicketSchedule/UserProfile/"+this.owner.get_avatarID()+"\" alt=\"Profile Picture\"></img>");
		result.append("<span class=\"passenger\"></span></div>");
		result.append("<div class=\"inner_content\"><h3>");
		result.append("<span class=\"inner\">"+this.ownerRide._rideInfo.origLoc._addr);
		result.append("<span class=\"trip_type round_trip\"></span>");
		result.append(this.ownerRide._rideInfo.destLoc._addr+"</span></h3><h4>");
        result.append("From: "+this.ownerRide._rideInfo.origLoc.get_formatedAddr());
        result.append("To: "+this.ownerRide._rideInfo.destLoc.get_formatedAddr());
        result.append("</h4></div></div></a>");
		return result.toString();
	}


	public String displayOnWebRideCenter()
	{
		StringBuilder result = new StringBuilder(2000);
		result.append("<div class=\"inner_content\"><h4>");
		result.append("From: "+ this.ownerRide._rideInfo.origLoc.get_formatedAddr()+"<br>");
		result.append("To: " + this.ownerRide._rideInfo.destLoc.get_formatedAddr()+"</h4>");
        result.append("<h4>"+this.ownerRide._rideInfo.getBarMessage()+"</h4></div>");
        return result.toString();
	}
	
	//Persistent Storage Related
	boolean _isSaved = false;
	Date _lastCp;
	
	public void updateDB()
	{
		TopicTbAccess.updateTopic(this);
	}
	
	@Override
	public void insertToDB() {
		TopicTbAccess.insertTopic(this);
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
	public Date lastCheckpoint() {
		// TODO Auto-generated method stub
		return this._lastCp;
	}


	@Override
	public boolean storageMode() {
		// Instant storage mode now.
		return false;
	}
}
