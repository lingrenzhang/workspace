package com.hitchride.util;

import com.hitchride.User;

public class GsonWrapperForUser {
	private int _uid=0;
	private int _authLevel=-1;
	private int _groupId=-1;
	public String _surename;
	private String _givenname;
	private String _avatarID;
	private int _userLevel;
	private String _emailAddress;
	public String _cellphone;
	
	public GsonWrapperForUser(User user){
		this._uid =user._uid;
		this._authLevel=user._authLevel;
		this._groupId = user._groupId;
		this._surename = user._surename;
		this._givenname = user._givenname;
		this._avatarID = user._avatarID;
		this._userLevel = user._userLevel;
		this._emailAddress = user._emailAddress;
		this._cellphone = user._cellphone;
	}

}
