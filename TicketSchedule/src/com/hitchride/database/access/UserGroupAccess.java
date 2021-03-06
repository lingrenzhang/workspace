package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.environ.SQLServerConf;
import com.mysql.jdbc.ResultSetImpl;

public class UserGroupAccess {
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
	
	
	
	public void insertValue(String groupName,int groupAuthLevel, String authenticationCode)
	{
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			sql.execute("insert into UserGroup (groupName,groupAuthLevel,authenticationCode) values(\""
					+ groupName + "\",\""
					+ groupAuthLevel + "\",\""
					+ authenticationCode + "\")");
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
			ResultSetImpl rs = (ResultSetImpl) sql.executeQuery("select groupid from UserGroup where authenticationCode='"+authCode+"'");
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
