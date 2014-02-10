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
	private Vector<ParticipantRide> _prides = new Vector<ParticipantRide>();

	
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
			enumeration.nextElement().updateRideInfo();
		}
	}

	public void attachUser(int UID)
	{
		User user = Environment.getEnv().getUser(UID);
		attach(user);
	}
	
	public void detachUser(int UID)
	{
		User user = Environment.getEnv().getUser(UID);
		detach(user);
	}
	
	
    public OwnerRideInfo(ResultSet rs, Boolean myArgsCommute) throws SQLException
    {
    	super(rs,myArgsCommute);
    }
    
    public int get_ownerId() {
		return _ownerId;
	}

	public void set_ownerId(int _ownerId) {
		this._ownerId = _ownerId;
	}



    
    
}
