package com.hitchride.standardClass;

import java.util.Vector;


//User passively updates i
public class User implements RideListener,UserInfo{
	private int _uid;
	private String _name;
	private String _avatarID;
	
	public Vector<RideInfo> rides= new Vector<RideInfo>();
	@Override
	public void updateRideInfo() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int get_uid() {
		return _uid;
	}
	public void set_uid(int _uid) {
		this._uid = _uid;
	}

	@Override
	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	@Override
	public String get_avatarID() {
		return _avatarID;
	}

	public void set_avatarID(String _avatarID) {
		this._avatarID = _avatarID;
	}

	

	
}
