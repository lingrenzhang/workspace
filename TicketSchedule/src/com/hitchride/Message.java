package com.hitchride;

import java.sql.Timestamp;
import java.util.Date;

import com.hitchride.database.access.MessageAccess;
import com.hitchride.environ.AllTopics;
import com.hitchride.environ.AllUsers;
import com.hitchride.environ.RecentMessages;

public class Message implements IMessageInfo,IPersistentStorage{

	public int _messageId;
	public IUserInfo _from;
	public IUserInfo _to;
	public int _topicID;
	public String _messageContent;
	public Date _generateDate;
	public Timestamp _TimeStamp;
	public CommuteOwnerRide _ownerRide;
	public boolean _isSystemMessage;
	
	private int topicType; //0 for commute 1 for transient.
	
	
	public Message()
	{
		//Used when load from DB
	}
	public Message(String content, IUserInfo from, IUserInfo to,CommuteTopic topic)
	{
		this._messageId = RecentMessages.getRecMessage().messageCount+1;
		RecentMessages.getRecMessage().messageCount = this._messageId;
		
		this._from = from;
		this._to = to;
		this._topicID = topic.get_topicId();
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
		this._isSystemMessage = false;
	}
	
	public Message(String content, int fromID, int toID,int topicID)
	{
		this._messageId = RecentMessages.getRecMessage().messageCount+1;
		RecentMessages.getRecMessage().messageCount = this._messageId;
		
		User from = (User) AllUsers.getUsers().getUser(fromID);
		this._from = from;
		User to = (User) AllUsers.getUsers().getUser(toID);
		this._to = to;
		this._topicID = topicID;
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
		
	}
	
	
	//System generated message for commute
	public Message(int fstatus,int astatus, IUserInfo from, IUserInfo to,CommuteTopic topic)
	{
		this.topicType = 0;
		this._messageId = RecentMessages.getRecMessage().messageCount+1;
		RecentMessages.getRecMessage().messageCount = this._messageId;

		this._from = from;
		this._to = to;
		this._topicID = topic.get_topicId();
		String content="";
		 //0 for not associating with Pride -> 1 (drive by participant)
		 //1 for waiting owner response ->0,2,3 (drive by owner)
         //2 for owner add additional requirement ->0,1 (drive by participant)
		 //3 for owner commit -> 0,1,4 (drive by participant, log)
         //4 for participant Confirm -> (Deal done.)
		int status = 10*fstatus +astatus;
		switch (status)
		{
		    case 1:
		    	content = from.get_name()+" wants to join the ride";
		    	break;
			case 10:
				content = "Owner "+from.get_name()+" declined " + to.get_name()+"'s ride request";
				break;
			case 12:
				content = "Owner "+from.get_name()+" wants more information from " + to.get_name();
				break;
			case 13:
				content = "Owner "+from.get_name()+" accepted " + to.get_name()+"'s ride request.";
				break;
			case 20:
				content = "Participant: " + from.get_name() + " quit the ride.";
				break;
			case 21:
				content = "Participant: " + from.get_name() + " updated the request.";
				break;
			case 23:
				content = "Owner: " + from.get_name() + " confirmed the request.";
				break;
			case 30:
				content = "Participant: " + from.get_name() + " quit the ride.";
				break;
			case 32:
				content = "Participant: " + from.get_name() + " updated the request.";
				break;
			case 34:
				content = "Participant: " + from.get_name() + " confirmed the ride. Officially join";
				//Log, persistent storage
				break;
			default:
				content = "Not leagal status change. " + status;
				break;
		}	
		
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
		this._isSystemMessage= true;
	}
	

	//System generated message for transient ride
	public Message(IUserInfo from, IUserInfo to, int actiontype, TransientRide tride, String info)
	{
		this.topicType=1;
		this._messageId = RecentMessages.getRecMessage().messageCount+1;
		RecentMessages.getRecMessage().messageCount = this._messageId;

		if (from==null) //Use system admin
		{
			this._from = AllUsers.getUsers().getUser(1);
		}
		else
		{
		this._from = from;
		}
		this._to = to;
	  
		
		
		switch (actiontype)
		{
		    case -1:
		    	this._messageContent = "您的从"+tride.origLoc.get_formatedAddr()+"到"+tride.destLoc.get_formatedAddr()+"的行程已创建";
		    	break;
			case 0: //加入行程
		      this._messageContent = "用户" + from.get_name() + "加入了您从"+tride.origLoc.get_formatedAddr() +"到"+tride.destLoc.get_formatedAddr()+"的行程";
		      break;
			case 1: //退出行程
			  this._messageContent = "用户" + from.get_name() + "退出了您从"+tride.origLoc.get_formatedAddr() +"到"+tride.destLoc.get_formatedAddr()+"的行程";
			  break;
			case 2: //添加中间点
			  this._messageContent = "用户" + from.get_name() + "在您从"+tride.origLoc.get_formatedAddr() +"到"+tride.destLoc.get_formatedAddr()+"的行程中添加了途经点:"+ info;
			  break;
			case 3: //删除中间点
			  this._messageContent = "用户" + from.get_name() + "在您从"+tride.origLoc.get_formatedAddr() +"到"+tride.destLoc.get_formatedAddr()+"的行程中删除了途经点:" +info;
			  break;
			
		}
		
		Date date = new Date();
		this._generateDate = date;
		this._isSystemMessage= true;
	}
	
	
	public String getHTMLMessageforJS()
	{
		StringBuilder result = new StringBuilder();
		if (isSystemMessage()){
			result.append("<span class=\\\"time\\\">");
			result.append(this.getMessageGenerateDate().toString());
			result.append("</span><br>");
			result.append("<span class=\\\"message_content\\\">");
			result.append(this.getMessageContent());
			result.append("</span><br>");
		}else{
			result.append("<strong>");
		    result.append("<a href=\\\"#userinfo\\\" class=\\\"auther\\\">");
		    result.append(this.getFrom().get_name()+" ");
		    result.append("</a></strong>");
		    result.append("<span>comments on ");
	        result.append(this.getMessageGenerateDate().toString());
	        result.append("</span><br>");
	        result.append("<span class=\\\"message_content\\\">");
	        result.append(this.getMessageContentforJS());
            result.append("</span>");
            result.append("<br>");
		}
		return result.toString();
	}
	
	public String getHTMLMessage()
	{
		StringBuilder result = new StringBuilder();
		if (isSystemMessage()){
			result.append("<span class=\"time\">");
			result.append(this.getMessageGenerateDate().toString());
			result.append("</span><br>");
			result.append("<span class=\"message_content\">");
			result.append(this.getMessageContent());
			result.append("</span><br>");
		}else{
			result.append("<strong>");
		    result.append("<a href=\"#userinfo\" class=\"auther\">");
		    result.append(this.getFrom().get_name()+" ");
		    result.append("</a></strong>");
		    result.append("<span>comments on ");
	        result.append(this.getMessageGenerateDate().toString());
	        result.append("</span><br>");
	        result.append("<span class=\"message_content\">");
	        result.append(this.getMessageContent());
            result.append("</span>");
            result.append("<br>");
		}
		
		return result.toString();
	}
	public String getCompleteMessage()
	{
		StringBuilder result = new StringBuilder();
		if (isSystemMessage())
			{ result.append("System message at ");}
		else{ result.append(getFrom().get_name()+" says at ");}
		result.append(getMessageGenerateDate()+"\r\n");
		result.append(getMessageContent()+"\r\n");
		return result.toString();
	}
	
	public String getMessageContentforJS()
	{
		String message =_messageContent.replace("\r\n", "<br>");
		return message;
	}
	
	@Override
	public String getMessageContent() {
		return this._messageContent;
	}

	@Override
	public Date getMessageGenerateDate() {
		return this._generateDate;
	}
	
	public Timestamp getTimeStamp(){
		this._TimeStamp = new Timestamp(this._generateDate.getTime());
		return this._TimeStamp;
	}

	@Override
	public IUserInfo getFrom() {
		return this._from;
	}

	@Override
	public IUserInfo getTo() {
		return this._to;
	}

	@Override
	public CommuteOwnerRide getOwnerRide() {
		return this._ownerRide;
	}

    public boolean isSystemMessage()
    {
    	return this._isSystemMessage;
    }
    
    public void sendMessage()
    {
		RecentMessages.getRecMessage().insert_message(this);//Should be message unique ID.
		if (this.topicType==0)
		{
			CommuteTopic topic = AllTopics.getTopics().get_topic(this._topicID);
			topic.messages.add(this);
		}
		User to =  (User) _to;
		to.message.add(this);
		to.numofnewMessage++;
		this._lastCp = new Date();
		if (storageMode())
		{//Instant store on sent
			insertToDB();
		}
    }
	
	//Persistent Storage Related
	boolean _isSaved = false;
	Date _lastCp;
	
	
	@Override
	public void insertToDB() {
		MessageAccess.insertMessage(this);
		this._isSaved =true;
		this._lastCp = new Date();
	}
	
	@Override
	public boolean isChanged() {
		return false;  //Message is not allowed to be update
	}

	@Override
	public boolean isSaved() {
		return this._isSaved;
	}

	@Override
	public Date lastCheckpoint() {
		return this._lastCp;
	}

	@Override
	public boolean storageMode() {
		// Instant storage mode now.
		return true;
	}
}
