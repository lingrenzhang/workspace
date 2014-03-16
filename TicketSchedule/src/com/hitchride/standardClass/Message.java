package com.hitchride.standardClass;

import java.sql.Timestamp;
import java.util.Date;

import com.hitchride.access.MessageTbAccess;
import com.hitchride.global.AllTopics;
import com.hitchride.global.AllUsers;
import com.hitchride.global.DummyData;
import com.hitchride.global.GlobalCount;

public class Message implements MessageInfo,PersistentStorage{

	public int _messageId;
	public UserInfo _from;
	public UserInfo _to;
	public int _topicID;
	public String _messageContent;
	public Date _generateDate;
	public Timestamp _TimeStamp;
	public OwnerRideInfo _ownerRide;
	public boolean _isSystemMessage;
	
	
	public Message()
	{
		//Used when load from DB
	}
	public Message(String content, UserInfo from, UserInfo to,Topic topic)
	{
		this._messageId = GlobalCount.getGCount().messageCount+1;
		GlobalCount.getGCount().messageCount = this._messageId;
		
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
		this._messageId = GlobalCount.getGCount().messageCount+1;
		GlobalCount.getGCount().messageCount = this._messageId;
		
		User from = (User) AllUsers.getUsers().getUser(fromID);
		this._from = from;
		User to = (User) AllUsers.getUsers().getUser(toID);
		this._to = to;
		this._topicID = topicID;
		this._messageContent = content;
		Date date = new Date();
		this._generateDate = date;
		
	}
	
	
	//System generated message
	public Message(int fstatus,int astatus, UserInfo from, UserInfo to,Topic topic)
	{
		this._messageId = GlobalCount.getGCount().messageCount+1;
		GlobalCount.getGCount().messageCount = this._messageId;
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
	public UserInfo getFrom() {
		return this._from;
	}

	@Override
	public UserInfo getTo() {
		return this._to;
	}

	@Override
	public OwnerRideInfo getOwnerRide() {
		return this._ownerRide;
	}

    public boolean isSystemMessage()
    {
    	return this._isSystemMessage;
    }
    
    public void sendMessage()
    {
		DummyData.getDummyEnv().insert_message(this); //Should be message unique ID.
		Topic topic = AllTopics.getTopics().get_topic(this._topicID);
		topic.messages.add(this);
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
		MessageTbAccess.insertMessage(this);
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
