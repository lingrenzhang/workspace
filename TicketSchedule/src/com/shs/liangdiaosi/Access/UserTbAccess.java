package com.shs.liangdiaosi.Access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTbAccess {
	    //id char(20)
	    //password char(20)
	    //emailinformation char(30)
	    //Maintain the DB matches this class content
		public static Connection objConn; //This reference is used for batch job.
		
		public static Connection getConnection() throws SQLException,	
		java.lang.ClassNotFoundException
		{
			String url = "jdbc:mysql://localhost/ticketschedule";
			Class.forName("com.mysql.jdbc.Driver");
			String userName="admin";
			String password="admin";
			objConn = DriverManager.getConnection(url,userName,password);
			return objConn;
		}
		
		
		public void insertValue(String userName,String password,String givenname, String surename,String address,int userLevel)
		{
			if (userLevel==0)
			{
				userLevel=1;
			}
			try
			{
				Statement sql;
				if (objConn==null)
				{
					getConnection();
				}
				sql=objConn.createStatement();
				sql.execute("insert into userTb (emailAddress,password,givenname,surename,address,userLevel) values(\"" 
						+ userName + "\",\""
						+ password + "\",\""
						+ givenname + "\",\""
						+ surename + "\",\""
						+ address + "\","
						+ userLevel + ")");
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
				System.err.println("SQLException:"+e.getMessage());
			}
		}
		
        public ResultSet showall()
        {
        	ResultSet result=null;
        	try
        	{
				Statement sql;
				if (objConn==null)
				{
					getConnection();
				}
				sql=objConn.createStatement();
				String query = "select * from userTb";
				result = sql.executeQuery(query);

        	}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
				System.err.println("SQLException:"+e.getMessage());
			}
			return result;
        }
        
        public ResultSet selectByName(String name, boolean batch)
        {
        	ResultSet result=null;
        	try
        	{
				Statement sql;
				if (batch)
				{
					if (objConn==null)
					{
						getConnection();
					}
					sql=objConn.createStatement();
				}
				else
				{
					Connection conn = getConnection();
					sql=conn.createStatement();
				}
				String query = "select * from userTb where emailAddress=\""+name+"\"";
				result = sql.executeQuery(query);
        	}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
				System.err.println("SQLException:"+e.getMessage());
			}
			return result;
        }
        
        public ResultSet selectByUserId(int userId)
        {
        	ResultSet result=null;
        	try
        	{
				Statement sql;
				if (objConn==null)
				{
					getConnection();
				}
				sql=objConn.createStatement();
				String query = "select * from userTb where userId=\""+userId+"\"";
				result = sql.executeQuery(query);
        	}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
				System.err.println("SQLException:"+e.getMessage());
			}
			return result;
        }
		
        protected void finalize(){
        	if (objConn!=null){
    			try {
    				objConn.close();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        }
}