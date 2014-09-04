package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import com.hitchride.GeoInfo;
import com.hitchride.CommuteRide;
import com.hitchride.Schedule;
import com.hitchride.environ.SQLServerConf;


public class CommuteRideAccess {

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
	
	
	public static int insertRideInfo(CommuteRide ride)
	{
		int rows= 0;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();

		 rows = sql.executeUpdate("insert into commuteride values(\"" + 
		     //Key Info
		 	//	new DataColumnSchema("userId","INT(10)"),
			//  new DataColumnSchema("recordId","INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE"),
					+ ride.get_user().get_uid() + "\",\""
					+ ride.id +"\",\""
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
					+ ride.schedule.cftime[1]+"\",\""
					+ ride.schedule.cftime[2]+"\",\""
					+ ride.schedule.cftime[3]+"\",\""
					+ ride.schedule.cftime[4]+"\",\""
					+ ride.schedule.cftime[5]+"\",\""
					+ ride.schedule.cftime[6]+"\",\""
					+ ride.schedule.cftime[0]+"\",\""
					+ ride.schedule.cbtime[1]+"\",\""
					+ ride.schedule.cbtime[2]+"\",\""
					+ ride.schedule.cbtime[3]+"\",\""
					+ ride.schedule.cbtime[4]+"\",\""
					+ ride.schedule.cbtime[5]+"\",\""
					+ ride.schedule.cbtime[6]+"\",\""
					+ ride.schedule.cbtime[0]+"\","
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
	
	
	public static int updateRideInfo(CommuteRide ride)
	{
		int rows=0;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			
			rows = sql.executeUpdate("update commuteride set " 
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
					+ "f1=\""+ ride.schedule.cftime[1]+"\","
					+ "f2=\""+ ride.schedule.cftime[2]+"\","
					+ "f3=\""+ ride.schedule.cftime[3]+"\","
					+ "f4=\""+ ride.schedule.cftime[4]+"\","
					+ "f5=\""+ ride.schedule.cftime[5]+"\","
					+ "f6=\""+ ride.schedule.cftime[6]+"\","
					+ "f7=\""+ ride.schedule.cftime[0]+"\","
					+ "b1=\""+ ride.schedule.cbtime[1]+"\","
					+ "b2=\""+ ride.schedule.cbtime[2]+"\","
					+ "b3=\""+ ride.schedule.cbtime[3]+"\","
					+ "b4=\""+ ride.schedule.cbtime[4]+"\","
					+ "b5=\""+ ride.schedule.cbtime[5]+"\","
					+ "b6=\""+ ride.schedule.cbtime[6]+"\","
					+ "b7=\""+ ride.schedule.cbtime[0]+"\","
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
					+ "where recordId=\""+ride.id+"\"");
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
	
	public static Hashtable<Integer,CommuteRide> LoadAllRide(){
		Hashtable<Integer,CommuteRide> allRides = new Hashtable<Integer,CommuteRide>(5000);
		try {
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			ResultSet riders = sql.executeQuery("select * from commuteride");
			while (riders.next())
			{
				CommuteRide ride = new CommuteRide();
				try {
					ride.userId = riders.getInt("userId");
					ride.id =riders.getInt("recordId");
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
						schedule.cftime[0] = riders.getTime("f7");
						schedule.cftime[1] = riders.getTime("f1");
						schedule.cftime[2] = riders.getTime("f2");
						schedule.cftime[3] = riders.getTime("f3");
						schedule.cftime[4] = riders.getTime("f4");
						schedule.cftime[5] = riders.getTime("f5");
						schedule.cftime[6] = riders.getTime("f6");
						schedule.cbtime[0] = riders.getTime("b7");
						schedule.cbtime[1] = riders.getTime("b1");
						schedule.cbtime[2] = riders.getTime("b2");
						schedule.cbtime[3] = riders.getTime("b3");
						schedule.cbtime[4] = riders.getTime("b4");
						schedule.cbtime[5] = riders.getTime("b5");
						schedule.cbtime[6] = riders.getTime("b6");
						

						//Load result anyway for development purpose.
						schedule.tripDate = riders.getDate("tripDate");
						schedule.tripTime = riders.getTime("tripTime");
					} else {
						schedule.tripDate = riders.getDate("tripDate");
						schedule.tripTime = riders.getTime("tripTime");
					}
					ride.schedule = schedule;
				} catch (SQLException e) {
					System.out.println("Not able to load Ride: " + ride.id);
					e.printStackTrace();
				}
				allRides.put(ride.id, ride);
				System.out.println("Ride: " + ride.id +" loaded.");
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
	
	
	public static int getMaxRideId()
	{
		int maxRideId=0;
		try
		{
			Statement sql;
			getConnection();
			
			sql=objConn.createStatement();
			ResultSet rs=sql.executeQuery("Select Max(recordId) from commuteride;");
			if (rs.next())
			{
				maxRideId = rs.getInt(1);
				System.out.println("Current Max Ride ID: " + maxRideId);
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
	
	
	public static void deleteRide(CommuteRide ride)
	{
		deleteRide(ride.id);
	}
	
	
	public static void deleteRide(int rideid)
	{
		try {
			Statement sql;
		    getConnection();

			sql=objConn.createStatement();
			sql.execute("delete from commuteride where recordId="+rideid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
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
