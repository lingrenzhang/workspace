package com.hitchride.standardClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import com.hitchride.global.Environment;

//Each OwnerRideInfo represents a travel topic
//It will push its status change to desired users through observant design pattern
public class OwnerRideInfo extends RideInfo implements RideStatusChange{
	private Vector<RideListener> _rideListeners = new Vector<RideListener>();
	private int _ownerId;
	public RideInfo _rideInfo;
	private Vector<GeoInfo> middlepoint = new Vector<GeoInfo>();
	private Vector<ParticipantRide> _prides = new Vector<ParticipantRide>();
	//

	

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
		User user = (User) Environment.getEnv().getUser(UID);
		attach(user);
	}
	
	public void detachUser(int UID)
	{
		User user = (User) Environment.getEnv().getUser(UID);
		detach(user);
	}
	
	
    public OwnerRideInfo(RideInfo rideInfo) throws SQLException
    {
    	this._rideInfo = rideInfo;
    	//this._ownerId = rideInfo.get_user().get_uid();
    }
    
    public int get_ownerId() {
		return _ownerId;
	}

	public void set_ownerId(int _ownerId) {
		this._ownerId = _ownerId;
	}

	public Vector<ParticipantRide> get_prides() {
		return _prides;
	}

	public void set_prides(Vector<ParticipantRide> _prides) {
		this._prides = _prides;
	}



    
    
}
