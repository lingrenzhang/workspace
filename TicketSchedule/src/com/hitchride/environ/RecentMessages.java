//This keeps messages starting from last week.

package com.hitchride.environ;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.hitchride.Message;
import com.hitchride.User;
import com.hitchride.database.access.MessageAccess;

public class RecentMessages {
	public static RecentMessages recMessages;
	public Hashtable<Integer, Message> _recMessages; //Attach message to ownerRide (topic) on initialize in production.
	public int messageCount =0;
	public int loadWindow =-7;  //Load message # of days after. Should be negative number.
	
	private RecentMessages(){
        _recMessages = new Hashtable<Integer,Message>(); //Think about move out message in memory later
		initializeMessage();
		messageCount = MessageAccess.getMaxMessageId();
	}
	

	private void initializeMessage() {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime()+loadWindow*24*3600000);
		_recMessages = MessageAccess.LoadMessageFrom(ts);
		Enumeration<Integer> k = _recMessages.keys();
		while (k.hasMoreElements())
		{
			Message message  = _recMessages.get(k.nextElement());
			User user = (User) message._to;
			user.message.add(message);
		}

		
	}

	public static RecentMessages getRecMessage(){
		if (null == recMessages){
			recMessages = new RecentMessages();
		}
		return recMessages;
	}

	public void insert_message(Message message) {
		_recMessages.put(this.messageCount,message);
		messageCount++;
	}
}
