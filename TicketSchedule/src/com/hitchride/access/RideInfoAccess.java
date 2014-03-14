package com.hitchride.access;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Hashtable;
import java.util.Iterator;

import com.hitchride.global.AllRides;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
import com.hitchride.global.SQLServerConf;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.Message;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;

public class RideInfoAccess {

public static Connection objConn; //This reference is used for batch job.
	
	public static Connection getConnection() throws SQLException,	
	java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return objConn;
	}
	
	public static void insertRideInfo(RideInfo ride)
	{
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();

		sql.execute("insert into RideInfo values(\"" + 
		     //Key Info
		 	//	new DataColumnSchema("userId","INT(10)"),
			//  new DataColumnSchema("recordId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
					+ ride.get_user().get_uid() + "\",\""
					+ ride.recordId +"\",\""
			//Geo Info
			/*new DataColumnSchema("origFAddr","VARCHAR(100)"),
			new DataColumnSchema("origAddr","VARCHAR(50)"),
			new DataColumnSchema("origState","VARCHAR(20)"),
			new DataColumnSchema("origCity","VARCHAR(20)"),
			new DataColumnSchema("origLat","DECIMAL(10,6)"),
			new DataColumnSchema("origLon","DECIMAL(10,6)"),
	
			
			new DataColumnSchema("destFAddr","VARCHAR(100)"),
			new DataColumnSchema("destAddr","VARCHAR(50)"),
			new DataColumnSchema("destState","VARCHAR(20)"),
			new DataColumnSchema("destCity","VARCHAR(20)"),
			new DataColumnSchema("destLat","DECIMAL(10,6)"),
			new DataColumnSchema("destLon","DECIMAL(10,6)"),
			
			new DataColumnSchema("distance","INT(8)"),
			new DataColumnSchema("duration","INT(8)"), */
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
					+ ride.dura+"\","
			//Schedule Info
			/*
			    new DataColumnSchema("commute","BOOL"),
				new DataColumnSchema("roundtrip","BOOL"),
				new DataColumnSchema("forwardFlex","TIME"),
				new DataColumnSchema("backFlex","TIME"),
				new DataColumnSchema("tripDate","DATE"),
				new DataColumnSchema("tripTime","TIME"),
				
				new DataColumnSchema("dayOfWeek","INT(7)"),
		        new DataColumnSchema("f1","TIME"),
		        new DataColumnSchema("f2","TIME"),
		        new DataColumnSchema("f3","TIME"),
		        new DataColumnSchema("f4","TIME"),
		        new DataColumnSchema("f5","TIME"),
		        new DataColumnSchema("f6","TIME"),
		        new DataColumnSchema("f7","TIME"),
		        new DataColumnSchema("b1","TIME"),
		        new DataColumnSchema("b2","TIME"),
		        new DataColumnSchema("b3","TIME"),
		        new DataColumnSchema("b4","TIME"),
		        new DataColumnSchema("b5","TIME"),
		        new DataColumnSchema("b6","TIME"),
		        new DataColumnSchema("b7","TIME"), */
					+ ride.schedule.isCommute()+","
					+ ride.schedule.isRoundTrip()+",\""
					+ ride.schedule.forwardFlexibility+"\",\""
					+ ride.schedule.backFlexibility+"\",\""
					+ ride.schedule.tripDate+"\",\""
					+ ride.schedule.tripTime+"\",\""
					+ ride.schedule.get_days()+"\",\""
					+ ride.schedule.cftime[0]+"\",\""
					+ ride.schedule.cftime[1]+"\",\""
					+ ride.schedule.cftime[2]+"\",\""
					+ ride.schedule.cftime[3]+"\",\""
					+ ride.schedule.cftime[4]+"\",\""
					+ ride.schedule.cftime[5]+"\",\""
					+ ride.schedule.cftime[6]+"\",\""
					+ ride.schedule.cbtime[0]+"\",\""
					+ ride.schedule.cbtime[1]+"\",\""
					+ ride.schedule.cbtime[2]+"\",\""
					+ ride.schedule.cbtime[3]+"\",\""
					+ ride.schedule.cbtime[4]+"\",\""
					+ ride.schedule.cbtime[5]+"\",\""
					+ ride.schedule.cbtime[6]+"\","
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
	}
	
	
	public static void updateRideInfo(RideInfo ride)
	{
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			
			sql.execute("update RideInfo set " 
				     //Key Info
				 	//	new DataColumnSchema("userId","INT(10)"),
					//  new DataColumnSchema("recordId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
					+ "userId=\"" + ride.get_user().get_uid() + "\","
					//Geo Info
					/*new DataColumnSchema("origFAddr","VARCHAR(100)"),
					new DataColumnSchema("origAddr","VARCHAR(50)"),
					new DataColumnSchema("origState","VARCHAR(20)"),
					new DataColumnSchema("origCity","VARCHAR(20)"),
					new DataColumnSchema("origLat","DECIMAL(10,6)"),
					new DataColumnSchema("origLon","DECIMAL(10,6)"),
			
					
					new DataColumnSchema("destFAddr","VARCHAR(100)"),
					new DataColumnSchema("destAddr","VARCHAR(50)"),
					new DataColumnSchema("destState","VARCHAR(20)"),
					new DataColumnSchema("destCity","VARCHAR(20)"),
					new DataColumnSchema("destLat","DECIMAL(10,6)"),
					new DataColumnSchema("destLon","DECIMAL(10,6)"),
					
					new DataColumnSchema("distance","INT(8)"),
					new DataColumnSchema("duration","INT(8)"), */
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
					/*
					new DataColumnSchema("commute","BOOL"),
					new DataColumnSchema("roundtrip","BOOL"),
					new DataColumnSchema("forwardFlex","TIME"),
					new DataColumnSchema("backFlex","TIME"),
					new DataColumnSchema("tripDate","DATE"),
					new DataColumnSchema("tripTime","TIME"),
						
					new DataColumnSchema("dayOfWeek","INT(7)"),
				    new DataColumnSchema("f1","TIME"),
				    new DataColumnSchema("f2","TIME"),
				    new DataColumnSchema("f3","TIME"),
				    new DataColumnSchema("f4","TIME"),
				    new DataColumnSchema("f5","TIME"),
				    new DataColumnSchema("f6","TIME"),
	        		new DataColumnSchema("f7","TIME"),
				    new DataColumnSchema("b1","TIME"),
				    new DataColumnSchema("b2","TIME"),
				    new DataColumnSchema("b3","TIME"),
				    new DataColumnSchema("b4","TIME"),
				    new DataColumnSchema("b5","TIME"),
				    new DataColumnSchema("b6","TIME"),
				    new DataColumnSchema("b7","TIME"), */
					+ "commute="+ride.schedule.isCommute()+","
					+ "roundtrip="   + ride.schedule.isRoundTrip()+","
					+ "forwardFlex=\"" + ride.schedule.forwardFlexibility+"\","
					+ "backFlex=\""    + ride.schedule.backFlexibility+"\","
					+ "tripDate=\""    + ride.schedule.tripDate+"\","
					+ "tripTime=\""    + ride.schedule.tripTime+"\","
					+ "dayOfWeek=\""   + ride.schedule.get_days()+"\","
					+ "f1=\""+ ride.schedule.cftime[0]+"\","
					+ "f2=\""+ ride.schedule.cftime[1]+"\","
					+ "f3=\""+ ride.schedule.cftime[2]+"\","
					+ "f4=\""+ ride.schedule.cftime[3]+"\","
					+ "f5=\""+ ride.schedule.cftime[4]+"\","
					+ "f6=\""+ ride.schedule.cftime[5]+"\","
					+ "f7=\""+ ride.schedule.cftime[6]+"\","
					+ "b1=\""+ ride.schedule.cbtime[0]+"\","
					+ "b2=\""+ ride.schedule.cbtime[1]+"\","
					+ "b3=\""+ ride.schedule.cbtime[2]+"\","
					+ "b4=\""+ ride.schedule.cbtime[3]+"\","
					+ "b5=\""+ ride.schedule.cbtime[4]+"\","
					+ "b6=\""+ ride.schedule.cbtime[5]+"\","
					+ "b7=\""+ ride.schedule.cbtime[6]+"\","
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
					+ "where recordId=\""+ride.recordId+"\"");
		}
		catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
	}
	
	public static Hashtable<Integer,RideInfo> LoadAllRide(){
		Hashtable<Integer,RideInfo> allRides = new Hashtable<Integer,RideInfo>(5000);
		try {
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			ResultSet riders = sql.executeQuery("select * from RideInfo");
			while (riders.next())
			{
				RideInfo ride = new RideInfo();
				try {
					ride.userId = riders.getInt("userId");
					ride.recordId =riders.getInt("recordId");
					// assign every field

					String origState = riders.getString("origState");
					String origCity	 = riders.getString("origCity");
					String origAddr	 = riders.getString("origAddr");
					String origFAddr = riders.getString("origFAddr");
					double origLat = riders.getDouble("origLat");
					double origLon = riders.getDouble("origLon");
					
					String destState = riders.getString("destState");
					String destCity	= riders.getString("destCity");
					String destAddr	= riders.getString("destAddr");
					String destFAddr = riders.getString("destFAddr");
					double destLat 	= riders.getDouble("destLat");
					double destLon 	= riders.getDouble("destLon");
					
					GeoInfo origLoc = new GeoInfo(origFAddr,origAddr,origCity,origState,origLat,origLon);
					GeoInfo destLoc = new GeoInfo(destFAddr,destAddr,destCity,destState,destLat,destLon);
					ride.origLoc = origLoc;
					ride.destLoc = destLoc;

					// User Info
					ride.userType 	= riders.getBoolean("UserType");
					ride.totalSeats	= riders.getInt("TotalSeats");
					ride.availSeats = riders.getInt("AvailSeats");
					ride.price = riders.getDouble("PayperSeat");
					
					Schedule schedule= new Schedule();
					schedule.set_isCommute(riders.getBoolean("commute")); // come from table name, not table content
					schedule.set_isRoundTrip(riders.getBoolean("roundtrip"));
					if(schedule.isCommute()){
						// for commute only

						schedule.forwardFlexibility = riders.getTime("forwardFlex");
						schedule.backFlexibility	= riders.getTime("backFlex");	// for round trip only
						int dayOfWeek = riders.getInt("dayofweek");
						schedule.set_dayOfWeek(dayOfWeek);
						schedule.cftime[0] = riders.getTime("f1");
						schedule.cftime[1] = riders.getTime("f2");
						schedule.cftime[2] = riders.getTime("f3");
						schedule.cftime[3] = riders.getTime("f4");
						schedule.cftime[4] = riders.getTime("f5");
						schedule.cftime[5] = riders.getTime("f6");
						schedule.cftime[6] = riders.getTime("f7");
						schedule.cbtime[0] = riders.getTime("b1");
						schedule.cbtime[1] = riders.getTime("b2");
						schedule.cbtime[2] = riders.getTime("b3");
						schedule.cbtime[3] = riders.getTime("b4");
						schedule.cbtime[4] = riders.getTime("b5");
						schedule.cbtime[5] = riders.getTime("b6");
						schedule.cbtime[6] = riders.getTime("b7");
						

						//Load result anyway for development purpose.
						schedule.tripDate = riders.getDate("tripDate");
						schedule.tripTime = riders.getTime("tripTime");
					} else {
						schedule.tripDate = riders.getDate("tripDate");
						schedule.tripTime = riders.getTime("tripTime");
					}
				} catch (SQLException e) {
					System.out.println("Not able to load Ride: " + ride.recordId);
					e.printStackTrace();
				}
				allRides.put(ride.recordId, ride);
				System.out.println("Ride: " + ride.recordId +" loaded.");
			}
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allRides;
	}
	
	public static void CloseConn() throws SQLException
	{
		if (objConn!=null)
		{
			objConn.close();
			objConn=null;
		}
	};
	
	public static int[] parseIds(String input)
	{
		String[] values = input.split(",");
		if (values[0].equals(""))
			return null;
		
		int[] result = new int[values.length];  //50 for the most
	    for (int i=0; i<values.length;i++)
	    {
	    	result[i] = Integer.parseInt(values[i]);
	    }
		return result;
	}
}
