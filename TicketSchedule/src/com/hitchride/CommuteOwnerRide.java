package com.hitchride;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.hitchride.database.access.CommuteOwnerRideAccess;
import com.hitchride.environ.AllUsers;


//Each OwnerRideInfo represents a travel topic
//It will push its status change to desired users through observant design pattern
public class CommuteOwnerRide implements IPersistentStorage,IRideStatusChange{
	private Vector<IRideListener> _rideListeners = new Vector<IRideListener>();
	private int _ownerId;
	public int id;
	public CommuteRide _rideInfo;
	public int middlePointCount = 0;
	public Vector<GeoInfo> middlepoint = new Vector<GeoInfo>(); //Persistent storage in ownerRide table
	//private Vector<ParticipantRide> _prides = new Vector<ParticipantRide>(); //Treated at topic

	@Override
	public void attach(IRideListener rideListener) {
		_rideListeners.addElement(rideListener);
	}

	@Override
	public void detach(IRideListener rideListener) {
		_rideListeners.removeElement(rideListener);
	}

	@Override
	public void notifyUserRideChange() {
		Enumeration<IRideListener> enumeration = _rideListeners.elements();
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
	
	
    public CommuteOwnerRide(CommuteRide rideInfo)
    {
    	this._rideInfo = rideInfo;
    	this.id = rideInfo.id;
    	this._ownerId = rideInfo.get_user().get_uid();
    }
    
    public CommuteOwnerRide() {
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
		int rows = CommuteOwnerRideAccess.updateTopicRide(this);
		if (rows == 0)
		{
			System.out.println("Update failed for topicride: "+this.id + " attempting insert.");
			rows = CommuteOwnerRideAccess.insertTopicRide(this);
			if (rows== 0)
			{
				System.out.println("Insert also failed for topicride: "+this.id + " Please check DB integrity.");
			}
		}
	}
	
	@Override
	public void insertToDB() {
		int rows = CommuteOwnerRideAccess.insertTopicRide(this);
		if (rows == 0)
		{
			System.out.println("Insert failed for topicride: "+ this.id + " attempting update.");
			rows = CommuteOwnerRideAccess.updateTopicRide(this);
			if (rows== 0)
			{
				System.out.println("Update also failed for topicride: "+this.id + " Please check DB integrity.");
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
