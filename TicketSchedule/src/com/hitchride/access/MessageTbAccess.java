package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

import com.hitchride.global.AllUsers;
import com.hitchride.global.SQLServerConf;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.User;

public class MessageTbAccess {

	public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		if (objConn==null)
		{
			objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		}  
		else
		{
			if (objConn.isClosed())
			{
				objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
			}
		}
		return objConn;
	}
	
	
	public static void insertMessage(int messageId, int from,int to,int topicID, String messageContent, Timestamp timestamp, Boolean isSystemMessage)
	{
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			sql.execute("insert into message (MessageId,fromUser,toUser,topicID,messageContent, timestamp,isSystemMessage) values(\"" + 
			messageId +"\",\""
			+from +"\",\""
			+to+"\",\""
			+topicID+"\",\""
			+messageContent+"\",\""
			+timestamp+"\","
			+isSystemMessage+")");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	public static void insertMessage(Message message)
	{
		try
		{
			Statement sql;
			getConnection();

			sql=objConn.createStatement();
			sql.execute("insert into message (MessageId,fromUser,toUser,topicID,messageContent, timestamp,isSystemMessage) values(\"" 
			+message._messageId +"\",\""
			+message.getFrom().get_uid() +"\",\""
			+message.getTo().get_uid()+"\",\""
			+message._topicID+"\",\""
			+message.getMessageContent()+"\",\""
			+message.getTimeStamp()+"\","
			+message.isSystemMessage()+")");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	public static int getMaxMessageId()
	{
		int maxMessageId=0;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			ResultSet rs=sql.executeQuery("Select Max(MessageId) from Message;");
			if (rs.next())
			{
				maxMessageId = rs.getInt(1);
				System.out.println("Current Max Message ID: " + maxMessageId);
			}
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return maxMessageId;
	}
	
	
	public static Hashtable<Integer,Message> LoadAllMessage(){
		Hashtable<Integer,Message> allMessages = new Hashtable<Integer,Message>(5000);
		try {
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			ResultSet messagers = sql.executeQuery("select * from Message");
			while (messagers.next())
			{
				Message message = new Message();
				try {
					message._messageId = messagers.getInt("MessageId");
					message._from = (User) AllUsers.getUsers().getUser(messagers.getInt("fromUser"));
					message._to = (User) AllUsers.getUsers().getUser(messagers.getInt("fromUser"));
					message._topicID = messagers.getInt("topicID");
					message._messageContent = messagers.getString("messageContent");
					message._TimeStamp = messagers.getTimestamp("timestamp");
					Date date = new Date();
					date.setTime(message._TimeStamp.getTime());
					message._generateDate = date;
					message._isSystemMessage = messagers.getBoolean("isSystemMessage");
				} catch (SQLException e) {
					System.out.println("Not able to load Message: " + message._messageId);
					e.printStackTrace();
				}
				allMessages.put(message._messageId, message);
				System.out.println("Message: " + message._messageId+" loaded.");
			}
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allMessages;
	}
	
	
	public static void CloseConn() throws SQLException
	{
		if (objConn!=null)
		{
			objConn.close();
			objConn=null;
		}
	};
	
}
