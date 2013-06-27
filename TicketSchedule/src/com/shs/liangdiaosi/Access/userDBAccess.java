package com.shs.liangdiaosi.Access;
import java.sql.*;

public class userDBAccess 
	{
	    //id char(20)
	    //password char(20)
	    //emailinformation char(30)
	    //Maintain the DB matches this class content
		private boolean test = false;
	
		public void setTest(boolean test){
			this.test = test;
		}
		
		private String tableName(){
			return (test)?"userInfo_Test":"userInfo";
		}
		
		public static Connection getConnection() throws SQLException,	
		java.lang.ClassNotFoundException
		{
			String url = "jdbc:mysql://localhost/ticketschedule";
			Class.forName("com.mysql.jdbc.Driver");
			String userName="admin";
			String password="admin";
			Connection con = DriverManager.getConnection(url,userName,password);
			return con;
		}
		
		
		public void insertValue(String id,String password,String emailinformation)
		{
			if (id==null)
			{
				id="";
			}
			if (password==null)
			{
				password="";
			}
			if (emailinformation==null)
			{
				emailinformation="";
			}
			try
			{
				Connection con=getConnection();
				Statement sql=con.createStatement();
				sql.execute("insert into " + tableName()+ " values(\"" + id +"\",\""+password+"\",\""+emailinformation+"\")");
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
	        	Connection con= getConnection();
				Statement sql = con.createStatement();
				String query = "select * from " + tableName();
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
        
        public ResultSet selectByName(String name)
        {
        	ResultSet result=null;
        	try
        	{
	        	Connection con= getConnection();
				Statement sql = con.createStatement();
				String query = "select * from " + tableName() + " where id=\""+name+"\"";
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
		
        // should have an id (int or string?) for each row in the table because a user should be able to submit multiple rides
        public ResultSet selectByRecordId(String RecordId)
        {
        	ResultSet result=null;
        	try
        	{
	        	Connection con= getConnection();
				Statement sql = con.createStatement();
				String query = "select * from " + tableName() + " where id=\""+RecordId+"\"";
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

}
