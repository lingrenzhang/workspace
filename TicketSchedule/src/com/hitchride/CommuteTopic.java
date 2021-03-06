
package com.hitchride;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.hitchride.database.access.CommuteTopicAccess;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllUsers;
import com.hitchride.environ.RecentMessages;
//This class is to represent the standard format of dataStructure for Message Box.
//Shield the front end from direct access to DB. Using runtime object for performance increase
//and de-couple physical persistent record with runtime server.  
//Later will put the MessageInfo as list in the runtime. 
//Any fetch will first check the MessageInfo from memory. Use regular commit to persistent storage.
//Has relation to both carpoolTb and messageTb in terms of persistent storage. Easier the table relation in mySQL.
//Not sure the detailed mySQL multi-table relationship supporting level.
//Think about more complicate use like trigger, cascade, etc later when necessary. 
public class CommuteTopic implements IPersistentStorage,ISerializetoJson,IMailList{
	private int id; 	//Same as owner ID
	public User owner;
	//public List<Participant> participants;
	public CommuteOwnerRide ownerRide;
	public List<CommutePartiRide> parRides;
	public List<CommutePartiRide> _requestPride;
	public List<Message> messages = new ArrayList<Message>();
	
    public CommuteTopic(){
    	//Used when load from DB.
    	parRides = new ArrayList<CommutePartiRide>();
    	_requestPride = new ArrayList<CommutePartiRide>();
    	
    }
	public CommuteTopic(int rid) {
		try {
				parRides = new ArrayList<CommutePartiRide>();
		    	_requestPride = new ArrayList<CommutePartiRide>();
				ownerRide = AllTopicRides.getTopicRides()._topicRides.get(rid);
				this.id=rid;
				IUserInfo ownerInfo = AllUsers.getUsers().getUser(ownerRide._rideInfo.get_username());
				owner =(User) ownerInfo;
						

				//TO DO: User dummy participant ride now
				Enumeration<Integer> e = AllPartRides.getPartRides().getAllPartRide();
				while (e.hasMoreElements())
				{
					Integer key = e.nextElement();
					CommutePartiRide pRide = AllPartRides.getPartRides().get_participantRide(key);
					if (pRide.get_assoOwnerRideId()==ownerRide._rideInfo.id)
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
				
				e = RecentMessages.getRecMessage()._recMessages.keys();
				while (e.hasMoreElements())
				{
					Integer key = e.nextElement();
					Message message = RecentMessages.getRecMessage()._recMessages.get(key);
					if (message.getOwnerRide()._rideInfo.id ==ownerRide._rideInfo.id)
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
		return id;
	}


	public void set_topicId(int _topicId) {
		this.id = _topicId;
	}
	
	public CommutePartiRide getpRideByuserId(int userId)
	{
		CommutePartiRide result;
		for (Iterator<CommutePartiRide> pRideI = _requestPride.iterator(); pRideI.hasNext();)
		{
			result = pRideI.next();
			if (result.get_userId() == userId)
			{
				return result;
			}
		}
		for (Iterator<CommutePartiRide> pRideI = parRides.iterator(); pRideI.hasNext();)
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
		result.append("<a href=\"/TicketSchedule/CommuteTopicCenter?topicId="+this.id +"&type=commute\">");
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
		result.append("<img src= \""+this.owner.get_head_portrait_path()+"\" alt=\"Profile Picture\"></img>");
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
	
	public User getOwner()
	{
		if (this.owner==null)
		{
			this.owner = (User) AllUsers.getUsers().getUser(this.ownerRide.get_ownerId());
		}
		return this.owner;
	}
	
	//This list is mostly for message service to deriver proper user list.
	@Override
	public List<User> getAllRelevantUser()
	{
		List<User> users = new ArrayList<User>();
		//Owner
		users.add(this.getOwner());
		for(Iterator<CommutePartiRide> iparti = this.parRides.iterator();iparti.hasNext();)
		{
			User user = (User) AllUsers.getUsers().getUser(iparti.next().get_userId());
			users.add(user);
		}
		return users;
	}
	
	@Override
	public List<User> getUsersExcept(int uid)
	{
		List<User> users = new ArrayList<User>();
		//Owner
		if (this.getOwner().get_uid()!=uid)
		{
			users.add(this.getOwner());
		}
		for(Iterator<CommutePartiRide> iparti = this.parRides.iterator();iparti.hasNext();)
		{
			User user = (User) AllUsers.getUsers().getUser(iparti.next().get_userId());
			if (user.get_uid()!=uid)
			{
				users.add(user);
			}
		}
		return users;
	}
	
	@Override
	public List<User> getUsersExcept(User euser)
	{
		List<User> users = new ArrayList<User>();
		//Owner
		if (this.getOwner() != euser)
		{
			users.add(this.getOwner());
		}
		for(Iterator<CommutePartiRide> iparti = this.parRides.iterator();iparti.hasNext();)
		{
			User user = (User) AllUsers.getUsers().getUser(iparti.next().get_userId());
			if (user!=euser)
			{
				users.add(user);
			}
		}
		return users;
	}
	
	//Persistent Storage Related
	boolean _isSaved = false;
	Date _lastCp;
	
	public void updateDB()
	{
		int rows = CommuteTopicAccess.updateTopic(this);
		if (rows == 0)
		{
			System.out.println("Update failed for topic: "+ this.id + " attempting insert.");
			rows = CommuteTopicAccess.insertTopic(this);
			if (rows== 0)
			{
				System.out.println("Insert also failed for topic: "+this.id + " Please check DB integrity.");
			}
		}
	}
	
	@Override
	public void insertToDB() {
		int rows = CommuteTopicAccess.insertTopic(this);
		if (rows == 0)
		{
			System.out.println("Insert failed for topic: "+ this.id + " attempting update.");
			rows = CommuteTopicAccess.updateTopic(this);
			if (rows== 0)
			{
				System.out.println("Update also failed for topic: "+this.id + " Please check DB integrity.");
			}
		}
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
	

	@Override
	public String getJson() {
		//Use GSON for function use now. Hard code here to avoid other issue.
		return null;
	}
}
