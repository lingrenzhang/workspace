package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.global.SQLServerConf;

public class MessageTbAccess {

	public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.ServerURL);
		objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return objConn;
	}
	
	public void insertMessage(String from,String to,String message,int recordID)
	{
		try
		{
			Statement sql;
			Connection conn=getConnection();
			sql=conn.createStatement();
			sql.execute("insert into messageTb (senderName,receivername,content,messageSenderStatus,messageReceiverStatus,recordId) values(\"" + from +"\",\""+to+"\",\""+message+"\",1,0,"+recordID+")");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
}
