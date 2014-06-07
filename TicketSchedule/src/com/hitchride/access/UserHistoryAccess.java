package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.global.SQLServerConf;
import com.hitchride.standardClass.UserHistory;

public class UserHistoryAccess {
	
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
	
	
	
	public static void insertRecord(UserHistory userHistory, boolean batch) throws Exception
	{
		Connection con;
		con = UserHistoryAccess.getConnection();
			

		try
		{
			Statement sql=(Statement) con.createStatement();
			String query= "insert into userHisTb(userid, reputation,totalMiles,dealCount,judeCount) values ("+
			        +userHistory.get_userId()+","
			        +userHistory.get_reputation()+","
			        +userHistory.get_totalMiles()+","
			        +userHistory._dealCount+","
			        +userHistory._judgeCount+")";
			sql.execute(query);
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	public static UserHistory getByID(int uid) throws ClassNotFoundException, SQLException
	{
		UserHistory userHis = new UserHistory(uid);
		
		Connection con;
		//Use centralized connection pool for runtime finally 
		//Use one consistent connection for the User History table. 
		con= getConnection();
		
		try
		{
			Statement sql=(Statement) con.createStatement();
			String query= "select * from userHisTb where userid="+uid;
			ResultSet rs = sql.executeQuery(query);
			if (rs.next())
			{
				userHis.set_reputation(rs.getInt("reputation"));
				userHis.set_totalMiles(rs.getInt("totalMiles"));
				userHis._dealCount = rs.getInt("dealCount");
				userHis._judgeCount = rs.getInt("judgeCount");
			}
			else
			{
				System.out.println("User#" +uid + "not found.");
			}
			
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return userHis;
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
