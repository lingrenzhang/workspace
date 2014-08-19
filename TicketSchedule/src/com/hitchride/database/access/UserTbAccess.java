package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.Error;

import com.hitchride.IUserInfo;
import com.hitchride.UserProfile;
import com.hitchride.environ.SQLServerConf;

public class UserTbAccess {
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
		
		
		
		public void insertValue(String userName,int groupId, String password,String givenname, String surname,String address,int userLevel,String avatarID,String cellphone) throws Exception
		{
			if (userLevel==0)
			{
				userLevel=1;
			}
			try
			{
				Statement sql;
				getConnection();
				
				sql=objConn.createStatement();
				sql.execute("insert into userTb (emailAddress,groupid,password,givenname,surname,address,userLevel,avatarID,cellphone) values(\"" 
						+ userName + "\","
						+ groupId + ",\""
						+ password + "\",\""
						+ givenname + "\",\""
						+ surname + "\",\""
						+ address + "\","
						+ userLevel + ",\""
						+ avatarID +"\",\""
						+ cellphone +"\")");
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
                Exception ex=new Exception("SQLException:"+e.getMessage());
                throw ex;
				//System.err.println("SQLException:"+e.getMessage());
			}
		}
		
        public ResultSet showall()
        {
        	ResultSet result=null;
        	try
        	{
				Statement sql;
				getConnection();
				
				
				sql=objConn.createStatement();
				sql.setFetchSize(100);
				//String query = "select * from userTb";
				String query ="select * from userTb where userid>0";
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
				getConnection();
				sql=objConn.createStatement();
				
				
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
				getConnection();
				
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
		
        public int getIDbyName(String name) 
        {
        	Statement sql;
        	ResultSet rs;
        	int result=0;
        	try
        	{
				getConnection();
						
				sql=objConn.createStatement();
	        	String query = "select userid from usertb where givenname= '"+name+"'";
	        	rs = sql.executeQuery(query);
	        	rs.next();
	        	result = rs.getInt(1);
	        }catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        return result;
	    }
        
        
        public int deleteUser(IUserInfo user)
        {
        	int rows=0;
        	Statement sql;
        	try
        	{
				getConnection();
				
				sql=objConn.createStatement();
	        	String query = "delete from usertb where userid= '"+user.get_uid()+"'";
	        	rows = sql.executeUpdate(query);
	        }catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	return rows;
        }
        
        
        public static UserProfile loadUserProfile(int userid)
        {
        	UserProfile user = new UserProfile();
        	Statement sql;
        	ResultSet rs;
        	try
        	{
				getConnection();
				
				sql=objConn.createStatement();
	        	String query = "select * from userTb where userid= '"+ userid +"'";
	        	rs = sql.executeQuery(query);
	        	if (rs.next()==true)
	        	{
	        		user.set_uid(userid);
	        		user._address=rs.getString("address");
	        		user._avatarID=rs.getString("avatarID");
	        		user._emailAddress=rs.getString("emailAddress");
	        		user._givenname=rs.getString("givenname");
	        		user._surename =rs.getString("surname");
	        		user.set_userLevel(rs.getInt("userLevel"));
	        		user._password = rs.getString("password");
	        		user._cellphone = rs.getString("cellphone");
	        	}
	        	else
	        	{
	        		System.out.println("Incorrect userid: "+ userid);
	        	}
	        		
	        }catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	return user;
        }
        
		public static int insertUser(UserProfile user) {
			int rows=0;
			try
			{
				Statement sql;
				getConnection();
			
				sql=objConn.createStatement();
				sql.execute("insert into userTb (emailAddress,password,givenname,surname,address,userLevel,avatarID,cellphone) values(\"" 
						+ user._emailAddress + "\",\""
						+ user._password + "\",\""
						+ user._givenname + "\",\""
						+ user._surename + "\",\""
						+ user._address + "\","
						+ user.get_userLevel() + ",\""
						+ user._avatarID +"\",\"" 
						+ user._cellphone+"\")");
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
			return rows;
		}
		
        public static int updateUserProfile(UserProfile user)
        {
        	int rows=0;
        	try
			{
				Statement sql;
				getConnection();
				
				sql=objConn.createStatement();
				rows=sql.executeUpdate("update userTb set "
						+ "givenname=\""+ user._givenname
						+ "\",surname=\""+ user._surename
						+ "\",address=\""+user._address
						+ "\",cellphone=\""+user._cellphone
						+ "\" where emailAddress=\""+ user._emailAddress+"\"");
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
                Error err=new Error("SQLException:"+e.getMessage());
                throw err;
			}
        	return rows;
        }
        
        public static int updateUserProfile(String givenname, String surename, String address, int uid)
        {
        	int rows=0;
        	try
			{
				Statement sql;
				getConnection();
		
				sql=objConn.createStatement();
				rows=sql.executeUpdate("update userTb set "
						+ "givenname=\""+ givenname
						+ "\",surname=\""+ surename
						+ "\",address=\""+ address
						+ "\" where userid=\""+ uid+"\"");
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
                Error err=new Error("SQLException:"+e.getMessage());
                throw err;
			}
        	return rows;
        }
        
        public static int updateUserProfile(String givenname, String surename, String address, String password, int uid)
        {
        	int rows=0;
        	try
			{
				Statement sql;
				getConnection();
		
				sql=objConn.createStatement();
				rows=sql.executeUpdate("update userTb set "
						+ "givenname=\""+ givenname
						+ "\",surname=\""+ surename
						+ "\",address=\""+ address
						+ "\",password=\""+password
						+ "\" where userid=\""+ uid+"\"");
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
                Error err=new Error("SQLException:"+e.getMessage());
                throw err;
			}
        	return rows;
        }
        
        public static boolean verifyPassword(int uid,String password)
        {
        	boolean result=false;
        	try
			{
				Statement sql;
				getConnection();
				sql=objConn.createStatement();
				ResultSet rs = sql.executeQuery("select password from userTb where userId="+uid);
				rs.next();
			    String realpass= rs.getString("password");
			    if (realpass.equals(password)){
			    	result=true;
			    }
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
                Error err=new Error("SQLException:"+e.getMessage());
                throw err;
			}
        	return result;
        }
        
        public static boolean verifyPassword(String userName,String password)
        {
        	boolean result=false;
        	try
			{
				Statement sql;
				getConnection();
				sql=objConn.createStatement();
				ResultSet rs = sql.executeQuery("select password from userTb where userName="+userName);
			    String realpass= rs.getString("password");
			    rs.next();
			    if (realpass.equals(password)){
			    	result=true;
			    }
			}
			catch (java.lang.ClassNotFoundException e){
				System.err.println("ClassNotFoundException:"+e.getMessage());
			}
			catch (SQLException e)
			{
                Error err=new Error("SQLException:"+e.getMessage());
                throw err;
			}
        	return result;
        }
        
        protected void finalize(){
        	if (objConn!=null){
    			try {
    				objConn.close();
    				objConn=null;
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        }

}
