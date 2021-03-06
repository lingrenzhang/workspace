package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;

import com.hitchride.GeoInfo;
import com.hitchride.CommuteOwnerRide;
import com.hitchride.environ.SQLServerConf;

public class CommuteOwnerRideAccess {
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
	
	
	public static int insertTopicRide(CommuteOwnerRide commuteownerride)
	{
		int rows = 0;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			/*
			new DataColumnSchema("userId","INT(10)"), //Foreign Key constrain? 
		                                          //Fixed value not visible to client.
			new DataColumnSchema("RideInfoId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
			//Ready to extend to middle point info
			*/
			rows = sql.executeUpdate("insert into commuteownerride (userId,RideInfoId) values(\"" 
					+ commuteownerride._rideInfo.get_user().get_uid() + "\",\""
					+ commuteownerride.id + "\")");
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
	
	
	public static int updateTopicRide(CommuteOwnerRide commuteownerride)
	{
		int rows = 0;
		try
		{
			Statement sql;
			getConnection();
			 
			sql=objConn.createStatement();
				
			//Ride fixed. Update middle point here.
			/*
			new DataColumnSchema("MiddlePointCount","INT(2) DEFAULT 0"),
			new DataColumnSchema("M1Addr","VARCHAR(50)"),
			new DataColumnSchema("M1Lat","DECIMAL(10,6)"),
			new DataColumnSchema("M1Lon","DECIMAL(10,6)"),
			new DataColumnSchema("M2Addr","VARCHAR(50)"),
			new DataColumnSchema("M2Lat","DECIMAL(10,6)"),
			new DataColumnSchema("M2Lon","DECIMAL(10,6)"),
			new DataColumnSchema("M3Addr","VARCHAR(50)"),
			new DataColumnSchema("M3Lat","DECIMAL(10,6)"),
			new DataColumnSchema("M3Lon","DECIMAL(10,6)"),
			new DataColumnSchema("M4Addr","VARCHAR(50)"),
			new DataColumnSchema("M4Lat","DECIMAL(10,6)"),
			new DataColumnSchema("M4Lon","DECIMAL(10,6)"),
			new DataColumnSchema("M5Addr,"VARCHAR(50)"),
			new DataColumnSchema("M5Lat","DECIMAL(10,6)"),
			new DataColumnSchema("M5Lon","DECIMAL(10,6)"),
			*/
			StringBuilder updatequery = new StringBuilder();
			updatequery.append("update Topic set ");
			updatequery.append("MiddlePointCount=\""+commuteownerride.middlePointCount+"\"");
			int i=1;
			for (Iterator<GeoInfo> mpi=commuteownerride.middlepoint.iterator();mpi.hasNext();)
			{
				GeoInfo mp = mpi.next();
				updatequery.append(",M"+i+"Addr=\""+mp._addr+"\"");
				updatequery.append(",M"+i+"Lat=\""+mp.get_lat()+"\"");
				updatequery.append(",M"+i+"Lon=\""+mp.get_lon()+"\"");
				i++;
			}
			if (i-1!=commuteownerride.middlePointCount)
			{
				System.out.println("Incorrect number of middle point");
			}
			updatequery.append(" where RideInfoId=\""+ commuteownerride._rideInfo.id+"\"" );
			rows = sql.executeUpdate(updatequery.toString());
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
	
	public static Hashtable<Integer,CommuteOwnerRide> LoadAllOwnerRide(){
		Hashtable<Integer,CommuteOwnerRide> allTRides = new Hashtable<Integer,CommuteOwnerRide>(5000);
		try {
			Statement sql;
			getConnection();
			 
			sql=objConn.createStatement();
			ResultSet triders = sql.executeQuery("select * from commuteownerride");
			while (triders.next())
			{
				int rid = triders.getInt("RideInfoID");
				//TODO: Rethink about it
				CommuteOwnerRide tride = new CommuteOwnerRide();
				try {
					tride.id = rid;
					tride.set_ownerId(triders.getInt("userId"));
					tride.middlePointCount=(triders.getInt("MiddlePointCount"));
					for (int i=1;i<tride.middlePointCount+1;i++)
					{
						String addr = triders.getString("M"+i + "Addr");
						Double lat = triders.getDouble("M"+i + "Lat");
						Double lon = triders.getDouble("M"+i + "Lon");
						GeoInfo geo = new GeoInfo(addr,lat,lon);
						tride.middlepoint.add(geo);
					}
					
				} catch (SQLException e) {
					System.out.println("Not able to load topic ride: " + tride.id);
					e.printStackTrace();
				}
				allTRides.put(tride.id, tride);
				System.out.println("Topic Ride: " + tride.id+" loaded.");
			}
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allTRides;
	}
	
	public static void CloseConn() throws SQLException
	{
		if (objConn!=null)
		{
			objConn.close();
			objConn=null;
		}
	};
}
