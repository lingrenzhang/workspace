package com.hitchride.standardClass;

public class Owner extends User implements RideListener,UserStatus {
	
	private int _userStatus;
	
	public Owner(UserInfo userInfo) {
		super(userInfo);
	}

	@Override
	public int get_userStatus() {
		return this._userStatus;
	}


	@Override
	public void set_userStatus(int userStatus) {
		this._userStatus =userStatus;
		
	}


	@Override
	public void updateRideInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String statusExplain() {
		// TODO Auto-generated method stub
		return null;
	}



}
