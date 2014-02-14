package com.hitchride.standardClass;

import java.util.Date;

import com.hitchride.global.Environment;

public class Message implements MessageInfo,PersistentStorage{

	private User _from;
	private User _to;
	private String _messageContent;
	private Date _generateDate;
	private OwnerRideInfo _ownerRide;
	
	public Message(String content, User from, User to,OwnerRideInfo ownerRide)
	{
		this._from = from;
		this._to = to;
		this._ownerRide = ownerRide;
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
	}
	
	public Message(String content, int fromID, int toID,int ownerRideID)
	{
		User from = (User) Environment.getEnv().getUser(fromID);
		this._from = from;
		User to = (User) Environment.getEnv().getUser(toID);
		this._to = to;
		OwnerRideInfo ownerRide = Environment.getEnv().getOwnerRide(ownerRideID);
		this._ownerRide = ownerRide;
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
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
	public User getFrom() {
		return this._from;
	}

	@Override
	public User getTo() {
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


}
