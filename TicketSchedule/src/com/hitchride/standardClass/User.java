package com.hitchride.standardClass;

import java.util.Vector;


//User passively updates i
public class User implements RideListener{
	private int _uid;
	private String _name;
	
	public Vector<RideInfo> rides= new Vector<RideInfo>();
	@Override
	public void updateRideInfo() {
		// TODO Auto-generated method stub
		
	}
	
	public int get_uid() {
		return _uid;
	}
	public void set_uid(int _uid) {
		this._uid = _uid;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}
	
	
}
