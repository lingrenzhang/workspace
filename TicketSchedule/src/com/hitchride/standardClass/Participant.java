package com.hitchride.standardClass;

public class Participant implements RideListener,UserInfo,UserStatus
{
	private int _uid;
	private String _name;
	private String _avatarID;
	private int _userStatus;
	
	public Participant(UserInfo userInfo) {
		this.set_avatarID(userInfo.get_avatarID());
		this.set_name(userInfo.get_name());
		this.set_uid(userInfo.get_uid());
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


	@Override
	public int get_userStatus() {
		// TODO Auto-generated method stub
		return this._userStatus;
	}


	@Override
	public void set_userStatus(int userStatus) {
		// TODO Auto-generated method stub
		this._userStatus = userStatus;
		
	}


}
