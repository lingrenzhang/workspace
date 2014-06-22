package com.hitchride.standardClass;

import java.util.Vector;

import com.hitchride.access.PredefinedQuery;


//User passively updates i
public class User implements RideListener,UserInfo{
	//UserInfo
	private int _uid=0;
	private int _authLevel=-1;
	private int _groupId=-1;
	public String _surename;
	private String _givenname;
	private String _avatarID;
	private int _userLevel;
	private String _emailAddress;
	public String _cellphone;
	
	public Vector<ParticipantRide> pRides= new Vector<ParticipantRide>();
	public Vector<OwnerRideInfo> tRides= new Vector<OwnerRideInfo>();
	public Vector<Message> message = new Vector<Message>(); //Load the message in memory. 
	//Switch to message unique ID when persistent storage involved.
	public int numofrides;
	public int numofMessage;
	public int numofnewMessage;
	
	public User()
	{
	}
	
	public User(UserInfo user)
	{
		this.set_authLevel(user.get_userLevel());
		this.set_uid(user.get_uid());
		this.set_name(user.get_name());
		this.set_avatarID(user.get_avatarID());
		this.set_emailAddress(user.get_avatarID());
		this.set_cellphone(user.get_cellphone());
	}
	
	@Override
	public void updateRideInfo() {
		// TODO Auto-generated method stub
	}
	
	public int get_groupid()
	{
		return this._groupId;
	}
	
	public void set_groupId(int groupId)
	{
		//Only allow for admin.
		this._groupId = groupId;
	}
	
	public int get_authLevel()
	{ 
		if (_authLevel==-1)
		{
			_authLevel = PredefinedQuery.getUserAuthByID(this._groupId);
		}
		return _authLevel;
	}
	
	public void set_authLevel(int authLevel)
	{
		this._authLevel=authLevel;
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
		return _givenname;
	}
	public void set_name(String name) {
		this._givenname = name;
	}
	
	public String get_surename() {
		return _givenname;
	}
	public void set_surename(String surename) {
		this._givenname = surename;
	}


	@Override
	public String get_avatarID() {
		return _avatarID;
	}

	public void set_avatarID(String _avatarID) {
		this._avatarID = _avatarID;
	}

	public void set_userLevel(String userLevel) {
		int userlevel = Integer.parseInt(userLevel);
		this._userLevel = userlevel;
	}
	public void set_userLevel(int userLevel) {
		
		this._userLevel = userLevel;
		
	}

	@Override
	public int get_userLevel() {
		return this._userLevel;
	}

	@Override
	public String get_emailAddress() {
		return this._emailAddress;
	}

	public void set_emailAddress(String emailAddress) {
		this._emailAddress = emailAddress;
		
	}
	
	public String getUserWrapper()
	{
		StringBuilder result = new StringBuilder(300);
		result.append("<div class=\"userpic\">");
		result.append("<div class=\"username\">"+this.get_name()+"</div>");
		result.append("<img src=\"/TicketSchedule/UserProfile/"+this.get_avatarID()+"\" alt=\"Profile Picture\"></img>");
		result.append("<span class=\"vip\"></span>");
		result.append("</div>");
		result.append("<div id=\"user_info\">");
		result.append("Welcome "+ this.get_name());
		result.append("</div>");
		if (this.get_uid()!=0)
		{
	        result.append("<div id=\"message_info\">");
	        result.append("<a href=\"/TicketSchedule/UserCenter.jsp\">You have "+this.numofnewMessage + " new messages</a>");
	        result.append("</div>");
		}
		else
		{
			 result.append("<div id=\"login\">");
		     result.append("<a href=\"/TicketSchedule/Zh/Login.jsp\">Login please</a>");
		     result.append("</div>");
		}
        result.append("<div id=\"user_level\">");
        result.append("Level: "+ this.get_userLevel());
        result.append("</div>");
        if (this.get_uid()!=0)
        {
	        result.append("<div id=\"logout\">");
	        result.append("<a href='/TicketSchedule/servlet/Logout?uid="+this.get_uid()+"'>Logout</a>");
	        result.append("</div>");
        }

		return result.toString();
	}

	@Override
	public String get_cellphone() {
		return this._cellphone;
	}

	public void set_cellphone(String cellphone){
		this._cellphone = cellphone;
	}
	
	
}
