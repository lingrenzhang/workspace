package com.hitchride.access;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hitchride.global.SQLServerConf;
import com.hitchride.standardClass.TransientRide;
import com.mysql.jdbc.ResultSet;

public class TransientRideAccess {
	public static Connection objConn; //This reference is used for batch job.
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return objConn;
	}
	
	public static int insertTransientRideInfo(TransientRide ride)
	{
		int rows= 0;
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();

		 rows = sql.executeUpdate("insert into TransientRide values(\"" + 
		    		+ ride.userId + "\",\""
					+ ride.transientRideId +"\",\""
			//Geo Info
					+ ride.origLoc.get_formatedAddr()+"\",\""
					+ ride.origLoc._addr+"\",\""
					+ ride.origLoc._state+"\",\""
					+ ride.origLoc._city+"\",\""
					+ ride.origLoc.get_lat()+"\",\""
					+ ride.origLoc.get_lon()+"\",\""
					+ ride.destLoc.get_formatedAddr()+"\",\""
					+ ride.destLoc._addr+"\",\""
					+ ride.destLoc._state+"\",\""
					+ ride.destLoc._city+"\",\""
					+ ride.destLoc.get_lat()+"\",\""
					+ ride.destLoc.get_lon()+"\",\""
					+ ride.dist+"\",\""
					+ ride.dura+"\",\""
			//Schedule Info
					+ ride.rideDate+"\",\""
					+ ride.rideTime+"\",\""
					+ ride.rideFlex+"\","
		        //Bargin Info
		        /* 
				new DataColumnSchema("UserType","BOOL"),
				new DataColumnSchema("TotalSeats","INT(2)"),
				new DataColumnSchema("AvailSeats","INT(2)"),
				new DataColumnSchema("PayperSeat","DECIMAL(6,2)")
				*/
					+ ride.userType+",\""
					+ ride.totalSeats+"\",\""
					+ ride.availSeats+"\",\""
			        + ride.price+"\")");

		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	 	return rows;
	}
	
	public static int updateTransientRide(TransientRide ride)
	{
		int rows=0;
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			
			rows = sql.executeUpdate("update TransientRide set " 
				     //Key Info
				 	//	new DataColumnSchema("userId","INT(10)"),
					//  new DataColumnSchema("recordId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
					+ "userId=\"" + ride.userId + "\","
					
					//Geo Info
					+ "origFAddr=\"" + ride.origLoc.get_formatedAddr()+"\","
					+ "origAddr=\""	 + ride.origLoc._addr+"\","
					+ "origState=\"" + ride.origLoc._state+"\","
					+ "origCity=\""	 + ride.origLoc._city+"\","
					+ "origLat=\""	 + ride.origLoc.get_lat()+"\","
					+ "origLon=\""	 + ride.origLoc.get_lon()+"\","
					+ "destFAddr=\"" + ride.destLoc.get_formatedAddr()+"\","
					+ "destAddr=\""	 + ride.destLoc._addr+"\","
					+ "destState=\"" + ride.destLoc._state+"\","
					+ "destCity=\""	 + ride.destLoc._city+"\","
					+ "destLat=\""   + ride.destLoc.get_lat()+"\","
					+ "destLon=\""   + ride.destLoc.get_lon()+"\","
					+ "distance=\""  + ride.dist+"\","
					+ "duration=\""  + ride.dura+"\","
					//Schedule Info
					+ "rideDate=\"" + ride.rideDate+"\","
					+ "rideTime=\"" + ride.rideTime+"\","
					+ "rideFlex=\"" + ride.rideFlex+"\","
				    //Bargin Info
				    /* 
					new D\ataColumnSchema("UserType","BOOL"),
					new DataColumnSchema("TotalSeats","INT(2)"),
					new DataColumnSchema("AvailSeats","INT(2)"),
					new DataColumnSchema("PayperSeat","DECIMAL(6,2)")
					*/
					+ "UserType="+ ride.userType+","
					+ "TotalSeats=\""+ ride.totalSeats+"\","
					+ "AvailSeats=\""+ ride.availSeats+"\","
					+ "PayperSeat=\""+ ride.price+ "\" "
					+ "where transientRideId=\""+ride.transientRideId+"\"");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return rows;
	}
	
	
	public static int getMaxTransientRideId()
	{
		int maxRideId=0;
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			}
			sql=objConn.createStatement();
			ResultSet rs=(ResultSet) sql.executeQuery("Select Max(transientRideId) from transientRide;");
			if (rs.next())
			{
				maxRideId = rs.getInt(1);
				System.out.println("Current Max Transient Ride ID: " + maxRideId);
			}
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return maxRideId;
	}
	
	 
	
	public static void CloseConn() throws SQLException
	{
		if (objConn!=null)
		{
			objConn.close();
			objConn=null;
		}
	};
	
	//Stored query
	//Return first 100 stored ride
	public static List<TransientRide> listTransisentRideByGroupId(int GroupId,Date date)
	{
		List<TransientRide> resultlist = new ArrayList<TransientRide>();
		try {
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			ResultSet riders = (ResultSet) sql.executeQuery("select * from TransientRide where rideDate=\""+date+"\" order by ridetime");
			int i=0;
			while (riders.next() && i<100)
			{
				TransientRide tride = new TransientRide(riders);
				resultlist.add(tride);
				i++;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultlist;
	}

	
	public static TransientRide getTransisentRideById(int trId)
	{
		TransientRide tride=null;
		try {
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			ResultSet ride = (ResultSet) sql.executeQuery("select * from TransientRide where transientRideId="+trId);

			if (ride.next() )
			{
				tride = new TransientRide(ride);
			}
			else
			{
				return null;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tride;
	}
}
