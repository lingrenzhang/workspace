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
	@Override
	public String statusExplain() {
		switch (_userStatus)
		{
			case 0:
				return "Not associated with ownerRide";
			case 1:
				return "Waiting owner response";
			case 2:
				return "Owner requires more info";
			case 3:
				return "Accepted wating your confirm";
			case 4:
				return "Deal";
		}
		return null;
	}

}
