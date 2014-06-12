package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.global.SQLServerConf;
import com.mysql.jdbc.ResultSetImpl;

//PredinedQuery, something like stored procedure from functional consideration
//Keep separate to better view the typical call as (they may need authority?)
public class PredefinedQuery {
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
	
	
	
	public static int getUserAuthByID(int groupId)
	{
		int result=-1;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			ResultSetImpl rs = (ResultSetImpl) sql.executeQuery("select GroupAuthLevel from usergroup where groupid="+groupId);
			if (rs.next()){
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
