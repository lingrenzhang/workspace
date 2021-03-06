package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.environ.SQLServerConf;
import com.mysql.jdbc.ResultSetImpl;



public class CarpoolTbAccess {

	public static Connection objConn; //This reference is used for batch job.
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException
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
	
	
	//Explicitly dispose the static connection whenever batch operation involves;
	public static void disposeConnection() throws SQLException,java.lang.ClassNotFoundException
	{
		if (objConn!=null)
		{
			objConn.close();
		}
	}
	/*
	"VARCHAR(30)", //"userName"
	// trip info
	"INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE", //"recordId"
	"BOOL", //"roundtrip"
	"BOOL", //"userType"
	"INT(7)", //"dayOfWeek"  (1234567)
	//"DATE",
	"VARCHAR(20)",//"origState"
	"VARCHAR(20)",//"origCity"
	"VARCHAR(20)",//"origNbhd"
	"VARCHAR(50)",//"origAddr"
	"VARCHAR(20)",//"destState"
	"VARCHAR(20)",//"destCity"
	"VARCHAR(20)",//"destNbhd"
	"VARCHAR(50)",//"destAddr"
	"DECIMAL(3,2)",//"detourFactor"
	"TIME",//"forwardTime"
	"TIME",//"forwardFlexibility"
	"TIME",//"backTime"
	"TIME" //"backFlexibility"
	*/
	//(Keep the function in stack?)
	public static void insertValue(String userName,boolean roundtrip,boolean userType, int dayOfWeek, String origState,String origCity,
			String origNbhd, String origAddr,String destState,String destCity, String destNbhd, String destAddr, String detourFactor,
			String forwardTime, String forwardFlexibility, String backTime, String backFlexibility, boolean batch) throws Exception
	{
		Connection con;
		con = getConnection();
		
		//Do santity check here
		if (detourFactor.length()>4)
		{
			throw new Exception("detourFactor longer than 3");
		}
		
		try
		{
			Statement sql=(Statement) con.createStatement();
			sql.execute("insert into CarpoolTb(userName,roundtrip,userType,dayOfweek,origState,origCity,origNbhd," +
					"origAddr,destState,destCity,destNbhd,destAddr,detourFactor,forwardTime,forwardFlexibility,backTime," +
					"backFlexibility) values(\"" + 
					userName +"\","+roundtrip+","+ userType+","+dayOfWeek +",\""+origState+"\",\""+origCity+
					"\",\""+origNbhd + "\",\"" + origAddr + "\",\"" + destState + "\",\""+ destCity + "\",\""+
					destNbhd + "\",\""+ destAddr + "\",\"" + detourFactor + "\",\""+forwardTime + "\",\"" + 
					forwardFlexibility + "\",\"" + backTime + "\",\""+ backFlexibility + "\")");

		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	public static ResultSetImpl listAllLocation() throws ClassNotFoundException, SQLException
	{
		ResultSetImpl rs;
		Connection con =CarpoolTbAccess.getConnection();
		Statement sql=(Statement) con.createStatement();
		rs=(ResultSetImpl) sql.executeQuery("select recordId,origCity,origAddr,destCity,destAddr from carpoolTb");
		return rs;
	}
	
	public static ResultSetImpl listAllGeoPosi() throws ClassNotFoundException, SQLException
	{
		ResultSetImpl rs;
		Connection con =CarpoolTbAccess.getConnection();
		Statement sql=(Statement) con.createStatement();
		rs=(ResultSetImpl) sql.executeQuery("select recordId,origLat,origLon,destLat,destLon from carpoolTb");
		return rs;
	}
	
	public static void insertLocation(int recordId, Float origlat,Float origlng,Float destlat,Float destlng, boolean batch) throws ClassNotFoundException, SQLException
	{
		Connection con;
		con= CarpoolTbAccess.getConnection();
	
		//Do santity check here
		
		try
		{
			Statement sql=(Statement) con.createStatement();
			sql.execute("update CarpoolTb set origLat="+origlat+
					", origLon=" + origlng +
					", destLat=" + destlat +
					", destLon=" + destlng +
					" where recordid="+recordId);
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	public static void insertDisDua(int recordId, int dist,int dura, boolean batch) throws ClassNotFoundException, SQLException
	{
		Connection con;
		con= CarpoolTbAccess.getConnection();
		//Do santity check here
		
		try
		{
			Statement sql=(Statement) con.createStatement();
			sql.execute("update CarpoolTb set dist="+dist+
					", dura=" + dura +
					" where recordid="+recordId);
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	
		
	}
	
		
	
	
	public static void postRide(String userName,boolean roundtrip,boolean userType, int dayOfWeek, String origState,String origCity,
			String origNbhd, String origAddr,String destState,String destCity, String destNbhd, String destAddr, String detourFactor,
			String forwardTime, String forwardFlexibility, String backTime, String backFlexibility, double origLat, double origLon, 
			double destLat, double destLon, int dist, int dura, boolean batch) throws Exception
	{
		Connection con;
		con= CarpoolTbAccess.getConnection();
					
		//Do santity check here
		if (detourFactor.length()>4)
		{
			throw new Exception("detourFactor longer than 3");
		}
		
		try
		{
			Statement sql=(Statement) con.createStatement();
			sql.execute("insert into CarpoolTb(userName,roundtrip,userType,dayOfweek,origState,origCity,origNbhd," +
					"origAddr,destState,destCity,destNbhd,destAddr,detourFactor,forwardTime,forwardFlexibility,backTime," +
					"backFlexibility,origLat,origLon,destLat,destLon,dist,dura) values(\"" + 
					userName +"\","+roundtrip+","+ userType+","+dayOfWeek +",\""+origState+"\",\""+origCity+
					"\",\""+origNbhd + "\",\"" + origAddr + "\",\"" + destState + "\",\""+ destCity + "\",\""+
					destNbhd + "\",\""+ destAddr + "\",\"" + detourFactor + "\",\""+forwardTime + "\",\"" + 
					forwardFlexibility + "\",\"" + backTime + "\",\""+ backFlexibility + "\"," +origLat+
					","+origLon + "," + destLat + "," + destLon + ","+dist + "," + dura +")");
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
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
	
	public static void main(String args[]) throws Exception
	{
		//unit test the insert
		CarpoolTbAccess.insertValue("xiyao", true, true, 67, "CA", "SanJose", "origNbhd", "origAddr", "destState", "destCity", "destNbhd", "destAddr", "0.33", "07:00:00", "00:15:00", "18:00:00", "00:15:00", true);
		CarpoolTbAccess.insertValue("xiyao", true, true, 67, "CA", "SanJose", "origNbhd", "origAddr", "destState", "destCity", "destNbhd", "destAddr", "0.33", "07:00:00", "00:15:00", "18:00:00", "00:15:00", true);		
	}

	public static ResultSetImpl getForMessageBox(String recordId) throws ClassNotFoundException, SQLException
	{
		ResultSetImpl rs;
		Connection con =CarpoolTbAccess.getConnection();
		Statement sql=(Statement) con.createStatement();
		rs=(ResultSetImpl) sql.executeQuery("select recordId,origCity,origAddr,destCity,destAddr,userName,participants from carpoolTb where recordId="+recordId);
		return rs;
	}
	
	public static ResultSetImpl rideInitialLoad() throws ClassNotFoundException, SQLException
	{
		ResultSetImpl rs;
		Connection con =CarpoolTbAccess.getConnection();
		Statement sql=(Statement) con.createStatement();
		rs=(ResultSetImpl) sql.executeQuery("select * from carpoolTb");
		return rs;
	}
	
}
