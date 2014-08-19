package com.hitchride;

import com.hitchride.environ.AllUsers;

public class UserHistory {
	
	//HistoryInfo
	User _user;
	private int _userLevel;
	private int _totalMiles;
	private int _reputation;
	public int _judgeCount;// (1-50)
    public int _dealCount;

    public UserHistory(User user)
    {
    	this._user = user;
    }
    public UserHistory(int uid)
    {
    	User user = (User) AllUsers.getUsers().getUser(uid);
    	this._user = user;
    }
	
	public int get_totalMiles() {
		return this._totalMiles;
	}
	
	public void set_totalMiles(int totalMiles){
		this._totalMiles =totalMiles;
	}

	public int get_reputation() {
		return this._reputation;
	}
	
	public void set_reputation(int reputation) {
		this._reputation=reputation;
	}

	public int accumulateMiles(int newMile) {
		this._totalMiles = this._totalMiles + newMile;
		return this._totalMiles;
	}
	
	public int get_userId()
	{
		return this._user.get_uid();
	}
	
	public int get_level()
	{
		this._userLevel=0;
		//TO DO: level compute rules
		return this._userLevel;
	}
}
