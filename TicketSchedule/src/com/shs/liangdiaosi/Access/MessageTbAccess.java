package com.shs.liangdiaosi.Access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageTbAccess {

	public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		//String url = "jdbc:mysql://rs.luzhuoer.info/ticketschedule";
		String url = "jdbc:mysql://localhost/ticketschedule";
		Class.forName("com.mysql.jdbc.Driver");
		String userName="root";
		String password="rideshare";
		objConn = DriverManager.getConnection(url,userName,password);
		return objConn;
	}
	
	public void insertMessage(String from,String to,String message)
	{
		try
		{
			Statement sql;
			Connection conn=getConnection();
			sql=conn.createStatement();
			sql.execute("insert into messageTb (senderName,receivername,content,messageSenderStatus,messageReceiverStatus) values(\"" + from +"\",\""+to+"\",\""+message+"\",1,0)");
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
