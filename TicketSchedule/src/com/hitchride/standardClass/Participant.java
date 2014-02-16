package com.hitchride.standardClass;

public class Participant extends User implements RideListener,UserStatus
{
	private int _userStatus;
	
	
	public Participant()
	{
		
	}
	public Participant(UserInfo userInfo) {
		super(userInfo);
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
