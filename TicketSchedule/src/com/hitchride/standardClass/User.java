package com.hitchride.standardClass;

import java.util.Vector;


//User passively updates i
public class User implements RideListener,UserInfo{
	//UserInfo
	private int _uid;
	private String _name;
	private String _avatarID;
	private int _userLevel;
	private String _emailAddress;
	
	public Vector<RideInfo> rides= new Vector<RideInfo>();
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
		this.set_uid(user.get_uid());
		this.set_name(user.get_name());
		this.set_avatarID(user.get_avatarID());
		this.set_emailAddress(user.get_avatarID());

	}
	
	@Override
	public void updateRideInfo() {
		// TODO Auto-generated method stub
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
        result.append("<div id=\"message_info\">");
        result.append("<a href=\"/TicketSchedule/UserCenter.jsp\">You have "+this.numofnewMessage + " new messages</a>");
        result.append("</div>");
        result.append("<div id=\"user_level\">");
        result.append("Level: "+ this.get_userLevel());
        result.append("</div>");


		return result.toString();
	
	}
}
