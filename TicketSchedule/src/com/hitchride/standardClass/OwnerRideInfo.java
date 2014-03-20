package com.hitchride.standardClass;

import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.hitchride.access.TopicRideAccess;
import com.hitchride.global.AllUsers;


//Each OwnerRideInfo represents a travel topic
//It will push its status change to desired users through observant design pattern
public class OwnerRideInfo implements PersistentStorage,RideStatusChange{
	private Vector<RideListener> _rideListeners = new Vector<RideListener>();
	private int _ownerId;
	public int _recordId;
	public RideInfo _rideInfo;
	public int middlePointCount = 0;
	public Vector<GeoInfo> middlepoint = new Vector<GeoInfo>(); //Persistent storage in ownerRide table
	//private Vector<ParticipantRide> _prides = new Vector<ParticipantRide>(); //Treated at topic

	@Override
	public void attach(RideListener rideListener) {
		_rideListeners.addElement(rideListener);
	}

	@Override
	public void detach(RideListener rideListener) {
		_rideListeners.removeElement(rideListener);
	}

	@Override
	public void notifyUserRideChange() {
		Enumeration<RideListener> enumeration = _rideListeners.elements();
		while (enumeration.hasMoreElements())
		{
			enumeration.nextElement().updateRideInfo(); //Matching information requires recaculate.
		}
	}

	public void attachUser(int UID)
	{
		User user = (User) AllUsers.getUsers().getUser(UID);
		attach(user);
	}
	
	public void detachUser(int UID)
	{
		User user = (User) AllUsers.getUsers().getUser(UID);
		detach(user);
	}
	
	
    public OwnerRideInfo(RideInfo rideInfo)
    {
    	this._rideInfo = rideInfo;
    	this._recordId = rideInfo.recordId;
    	this._ownerId = rideInfo.get_user().get_uid();
    }
    
    public OwnerRideInfo() {
		// TODO Auto-generated constructor stub
	}

	public int get_ownerId() {
    	if (_ownerId==0)
    	{
    		this._ownerId = this._rideInfo.get_user().get_uid();
    	}
		return _ownerId;
	}

	public void set_ownerId(int _ownerId) {
		this._ownerId = _ownerId;
	}

	public String getGeoHTML() {
		return _rideInfo.getGeoHTML();
	}

	public String getScheduleHTML() {
		// TODO Auto-generated method stub
		return _rideInfo.getScheduleHTML();
	}
	
	//Persistent Storage Related
	boolean _isSaved = false;
	Date _lastCp;
		
		
	public void updateDB()
	{
		int rows = TopicRideAccess.updateTopicRide(this);
		if (rows == 0)
		{
			System.out.println("Update failed for topicride: "+this._recordId + " attempting insert.");
			rows = TopicRideAccess.insertTopicRide(this);
			if (rows== 0)
			{
				System.out.println("Insert also failed for topicride: "+this._recordId + " Please check DB integrity.");
			}
		}
	}
	
	@Override
	public void insertToDB() {
		int rows = TopicRideAccess.insertTopicRide(this);
		if (rows == 0)
		{
			System.out.println("Insert failed for topicride: "+ this._recordId + " attempting update.");
			rows = TopicRideAccess.insertTopicRide(this);
			if (rows== 0)
			{
				System.out.println("Update also failed for topicride: "+this._recordId + " Please check DB integrity.");
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
}
