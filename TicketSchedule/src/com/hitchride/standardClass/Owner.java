package com.hitchride.standardClass;

public class Owner implements RideListener,UserInfo,UserStatus {
	private int _uid;
	private String _name;
	private String _avatarID;
	
	private int _status;
	
	public Owner(UserInfo userInfo) {
		this.set_avatarID(userInfo.get_avatarID());
		this.set_name(userInfo.get_name());
		this.set_uid(userInfo.get_uid());
	}



	@Override
	public int get_status() {
		return this._status;
	}


	@Override
	public void set_status(int status) {
		this._status =status;
		
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

	@Override
	public void updateRideInfo() {
		// TODO Auto-generated method stub
		
	}



}
