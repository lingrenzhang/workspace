package com.hitchride.standardClass;

import java.util.Date;

import com.hitchride.access.UserTbAccess;

public class UserProfile implements PersistentStorage {
	private int _uid;
	public String _surename;
	public String _givenname;
	public String _address;
	public String _avatarID;
	private int _userLevel;
	public String _emailAddress;
	public String _password;
	public String _cellphone;
	
	public int get_uid() {
		return _uid;
	}

	public void set_uid(int _uid) {
		this._uid = _uid;
	}

	public int get_userLevel() {
		return _userLevel;
	}

	public void set_userLevel(int _userLevel) {
		this._userLevel = _userLevel;
	}
	
	
	//Persistent Storage Related
	boolean _isSaved = false;
	Date _lastCp;
		
	public void updateDB()
	{
		int rows = UserTbAccess.updateUserProfile(this);
		if (rows==0)
		{
			System.out.println("Update failed for user: "+ this.get_uid() + ". Check DB integrity please.");
		}
	}
		
	@Override
	public void insertToDB() {
		int rows = UserTbAccess.insertUser(this);
		if (rows==0)
		{
			System.out.println("Insert failed for user: "+ this.get_uid() + ". Check DB integrity please.");
				
		}
	}

	@Override
	public boolean isChanged() {
			// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isSaved() {
			// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date lastCheckpoint() {
			// TODO Auto-generated method stub
		return this._lastCp;
	}

	@Override
	public boolean storageMode() {
			// Instant storage mode now.
		return false;
	}
}
