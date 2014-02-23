package com.hitchride.standardClass;

import java.util.Date;

import com.hitchride.global.AllRides;
import com.hitchride.global.Environment;

public class Message implements MessageInfo,PersistentStorage{

	private UserInfo _from;
	private UserInfo _to;
	private String _messageContent;
	private Date _generateDate;
	private OwnerRideInfo _ownerRide;
	private boolean _isSystemMessage;
	
	
	
	public Message(String content, UserInfo from, UserInfo to,OwnerRideInfo ownerRide)
	{
		this._from = from;
		this._to = to;
		this._ownerRide = ownerRide;
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
		this._isSystemMessage = false;
	}
	
	public Message(String content, int fromID, int toID,int ownerRideID)
	{
		User from = (User) Environment.getEnv().getUser(fromID);
		this._from = from;
		User to = (User) Environment.getEnv().getUser(toID);
		this._to = to;
		OwnerRideInfo ownerRide = AllRides.getRides().getOwnerRide(ownerRideID);
		this._ownerRide = ownerRide;
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
	}
	
	
	//System generated message
	public Message(int fstatus,int astatus, UserInfo from, UserInfo to,OwnerRideInfo ownerRide)
	{
		this._from = from;
		this._to = to;
		this._ownerRide = ownerRide;
		String content="";
		 //0 for not associating with Pride -> 1 (drive by participant)
		 //1 for waiting owner response ->0,2,3 (drive by owner)
         //2 for owner add additional requirement ->0,1 (drive by participant)
		 //3 for owner commit -> 0,1,4 (drive by participant, log)
         //4 for participant Confirm -> (Deal done.)
		int status = 10*fstatus +astatus;
		switch (status)
		{
		    case 1:
		    	content = from.get_name()+" wants to join the ride";
		    	break;
			case 10:
				content = "Owner "+from.get_name()+" declined " + to.get_name()+"'s ride request";
				break;
			case 12:
				content = "Owner "+from.get_name()+" wants more information from " + to.get_name();
				break;
			case 13:
				content = "Owner "+from.get_name()+" accepted " + to.get_name()+"'s ride request.";
				break;
			case 20:
				content = "Participant: " + from.get_name() + " quit the ride.";
				break;
			case 21:
				content = "Participant: " + from.get_name() + " updated the request.";
				break;
			case 23:
				content = "Owner: " + from.get_name() + " confirmed the request.";
				break;
			case 30:
				content = "Participant: " + from.get_name() + " quit the ride.";
				break;
			case 32:
				content = "Participant: " + from.get_name() + " updated the request.";
				break;
			case 34:
				content = "Participant: " + from.get_name() + " confirmed the ride. Officially join";
				//Log, persistent storage
				break;
			default:
				content = "Not leagal status change. " + status;
				break;
		}	
		
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
		this._isSystemMessage= true;
	}
	

	
	
	@Override
	public String getMessageContent() {
		return this._messageContent;
	}

	@Override
	public Date getMessageGenerateDate() {
		return this._generateDate;
	}

	@Override
	public UserInfo getFrom() {
		return this._from;
	}

	@Override
	public UserInfo getTo() {
		return this._to;
	}

	@Override
	public OwnerRideInfo getOwnerRide() {
		return this._ownerRide;
	}

	@Override
	public void insertToDB() {
		// TODO Auto-generated method stub
		
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

    public boolean isSystemMessage()
    {
    	return this._isSystemMessage;
    }
}
