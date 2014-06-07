package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.global.SQLServerConf;
import com.mysql.jdbc.ResultSet;

public class UserGroupTbAccess {
	//id char(20)
    //password char(20)
    //emailinformation char(30)
    //Maintain the DB matches this class content
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
	
	
	
	public void insertValue(String groupName,int groupAuthLevel, String authnicationCode)
	{
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			sql.execute("insert into UserGroupTb (groupName,groupAuthLevel,authnicationCode) values(\"" 
					+ groupName + "\",\""
					+ groupAuthLevel + "\",\""
					+ authnicationCode + "\")");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
            Error err=new Error("SQLException:"+e.getMessage());
            throw err;
			//System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	
	public static int checkAuth(String authCode)
	{
		int result = -1;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			ResultSet rs = (ResultSet) sql.executeQuery("select groupid from UserGroup where authnicationCode='"+authCode+"'");
			if(rs.next())
			{
				result = rs.getInt(1);
			}
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
            Error err=new Error("SQLException:"+e.getMessage());
            throw err;
			//System.err.println("SQLException:"+e.getMessage());
		}
		return result;
	}
}
