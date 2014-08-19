package com.hitchride.database.dummydata;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.hitchride.database.access.MessageAccess;



public class GenerateTestingMessageDB {
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		//Long messageId, Long from,Long to,Long topicID, String messageContent, Timestamp timestamp, boolean isSystemMessage
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		MessageAccess.insertMessage(1,10,839, 838, "Dummy user input",ts,false);
		MessageAccess.insertMessage(2,20,839, 838, "Dummy system input",ts,true);
		MessageAccess.insertMessage(4,839,20, 838, "Dummy system input",ts,true);
		
	}
}
