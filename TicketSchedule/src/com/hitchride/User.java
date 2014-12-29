package com.hitchride;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.hitchride.database.access.PredefinedQuery;
import com.hitchride.database.access.TransientRideAccess;


//User passively updates i
public class User implements IRideListener,IUserInfo{
	//UserInfo
	public int _uid=0;
	public int _authLevel=-1;
	public int _groupId=-1;
	public String _surename;
	public String _givenname;
	public String _avatarID;
	public int _userLevel;
	public String _emailAddress;
	public String _cellphone;
	
	//Use self defined data structure later.
	public List<Integer> partiCommuteRide = new ArrayList<Integer>();
	public List<Integer> topicCommuteRide = new ArrayList<Integer>();
	
	//Transient Ride is traced by ID and accessed from DB directly.
	public Vector<CommutePartiRide> pRides= new Vector<CommutePartiRide>();
	public Vector<CommuteOwnerRide> tRides= new Vector<CommuteOwnerRide>();
	public Vector<Message> message = new Vector<Message>(); //Load the message in memory. 
	//Switch to message unique ID when persistent storage involved.
	public int numofrides;
	public int numofMessage;
	public int numofnewMessage;
	
	public User()
	{
	}
	
	public User(IUserInfo user)
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
		return _surename;
	}
	public void set_surename(String surename) {
		this._surename = surename;
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
	
	public String get_head_portrait_path(){
		if(this._avatarID == null){
			return "/TicketSchedule/UserProfile/default.jpg";
		}else{
			return "/TicketSchedule/pics/" + this._avatarID;
		}
	}

	public String getUserWrapper()
	{
		StringBuilder result = new StringBuilder(300);
		result.append("<div class='userpic'>");
		result.append("<div class='username'>"+ this.get_name()+"</div>");
		result.append("<img src='"+this.get_head_portrait_path()+"' alt='Profile Picture'></img>");
		result.append("<span class='vip'></span>");
		result.append("</div>");
		result.append("<div class='userinfo' id='user_name'>");
	    //result.append("Welcome " + this.get_name());
		result.append("欢迎" + this.get_name()+"!");
		result.append("</div>");
		if (this.get_uid()!=0)
		{
	        result.append("<div class='userinfo' id='message_info'>");
	        //result.append("<a href=\"/TicketSchedule/UserCenter.jsp\">You have "+this.numofnewMessage + " new messages</a>");
	        result.append("<a href='/TicketSchedule/Zh/UserCenter.jsp'>你有"+this.numofnewMessage + "条新消息。</a>");
	        result.append("</div>");
		}
		else
		{
			 result.append("<div class='userinfo' id='login'>");
		     //result.append("<a href=\"/TicketSchedule/Zh/Login.jsp\">Login please</a>");
		     result.append("<a href=\"/TicketSchedule/Zh/Login.jsp\">请登录</a>");
		     result.append("</div>");
		}
        //result.append("<div id=\"user_level\">");
        //result.append("Level: "+ this.get_userLevel());
        //result.append("用户级别: "+ this.get_userLevel());
        //result.append("</div>");
        if (this.get_uid()!=0)
        {
	        result.append("<div class='userinfo' id='logout'>");
	        //result.append("<a href='/TicketSchedule/servlet/Logout?uid="+this.get_uid()+"'>Logout</a>");
	        result.append("<a href='/TicketSchedule/servlet/Logout?uid="+this.get_uid()+"'>退出</a>");
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
	
	
	//ID operation
	public void refreshpTride()
	{
		for (Iterator<Integer> iTransient = this.partiCommuteRide.iterator();iTransient.hasNext();)
		{
			Integer tid = iTransient.next();
			TransientRide tr = TransientRideAccess.getTransisentRideById(tid);
			java.util.Date d = new java.util.Date();
			long current = d.getTime()+d.getTimezoneOffset()*60000;
			long timestamp = tr.rideDate.getTime()+tr.rideTime.getTime();
			if (current>timestamp)
			{
				iTransient.remove();
			}
		}
	}
	
	public void deletePartiCommuteRide(int id)
	{
		for (Iterator<Integer> iTransient = this.partiCommuteRide.iterator();iTransient.hasNext();)
		{
			if (iTransient.next() == id)
			{
				iTransient.remove();
			}
		}
	}
	
	public void insertPartiCommuteRide(int id)
	{
	    deletePartiCommuteRide(id); //Remove duplicate id
		this.partiCommuteRide.add(id);
	}
	
	public void refreshTopicCommuteRide()
	{
		for (Iterator<Integer> iTransient = this.topicCommuteRide.iterator();iTransient.hasNext();)
		{
			Integer tid = iTransient.next();
			TransientRide tr = TransientRideAccess.getTransisentRideById(tid);
			java.util.Date d = new java.util.Date();
			long current = d.getTime()+d.getTimezoneOffset()*60000;
			long timestamp = tr.rideDate.getTime()+tr.rideTime.getTime();
			if (current>timestamp)
			{
				iTransient.remove();
			}
		}
	}
	
	public void deletetTopicCommuteRide(int id)
	{
		for (Iterator<Integer> iTransient = this.topicCommuteRide.iterator();iTransient.hasNext();)
		{
			if (iTransient.next() == id)
			{
				iTransient.remove();
			}
		}
	}
	
	public void insertTopicCommuteRide(int id)
	{
	    deletetTopicCommuteRide(id); //Remove duplicate id
		this.topicCommuteRide.add(id);
	}
}
