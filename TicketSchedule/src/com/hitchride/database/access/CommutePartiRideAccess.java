package com.hitchride.database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;




import com.hitchride.MatchScore;
import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteRide;
import com.hitchride.environ.AllRides;
import com.hitchride.environ.SQLServerConf;


public class CommutePartiRideAccess {
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
	
	public static int insertPRide(CommutePartiRide pride)
	{
		int rows=0;
		try
		{
			Statement sql;
			getConnection();
			sql=objConn.createStatement();
			/*
			new DataColumnSchema("RideInfoID","INT(10)"), //Foreign Key constrain? 
	        new DataColumnSchema("TopicId","INT(10)"),
	        new DataColumnSchema("Status","INT(2)"),
			new DataColumnSchema("GeoMatch","INT(3) DEFAULT 0"),
			new DataColumnSchema("ScheduleMatch","INT(3) DEFAULT 0"),
			new DataColumnSchema("BarginMatch","INT(3) DEFAULT 0"),
			*/
			rows = sql.executeUpdate("insert into commutepartiride (RideInfoID,TopicId,Status,GeoMatch,ScheduleMatch,BarginMatch) values(\"" 
					+ pride._pid + "\",\""
					+ pride.get_assoOwnerRideId() +"\",\""
					+ pride.get_status() +"\",\""
					+ pride._Match.getLocationMatching() +"\",\""
					+ pride._Match.getSchedulingMatching()+ "\",\""
					+ pride._Match.getBarginMatching() + "\")");
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
	
	
	public static int updatePride(CommutePartiRide pride)
	{
		int rows = 0;
		try
		{
			Statement sql;
			getConnection();
				
			sql=objConn.createStatement();
	
			rows = sql.executeUpdate("update commutepartiride set "
					+ "TopicId=\""+ pride.get_assoOwnerRideId() +"\","
					+ "Status=\""+pride.get_status() +"\","
					+ "GeoMatch=\""+pride._Match.getLocationMatching() +"\","
					+ "ScheduleMatch=\""+pride._Match.getSchedulingMatching() +"\","
					+ "BarginMatch=\""+pride._Match.getBarginMatching() +"\" "
					+ "where RideInfoID=\""+pride._pid+"\"");
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
	
	public static Hashtable<Integer,CommutePartiRide> LoadAllpRide(){
		Hashtable<Integer,CommutePartiRide> allPRides = new Hashtable<Integer,CommutePartiRide>(5000);
		try {
			Statement sql;
			getConnection();

			sql=objConn.createStatement();
			ResultSet priders = sql.executeQuery("select * from commutepartiride");
			while (priders.next())
			{
				int _pid = priders.getInt("RideInfoID");
				CommuteRide ride = AllRides.getRides()._availRides.get(_pid);
				CommutePartiRide pride = new CommutePartiRide(ride);
				try {
					pride._pid = _pid;
					pride.set_assoOwnerRideId(priders.getInt("TopicId"));
					pride.set_status(priders.getInt("Status"));
					MatchScore match = new MatchScore(priders.getInt("GeoMatch"),priders.getInt("ScheduleMatch"),priders.getInt("BarginMatch"));
			        pride._Match=match;
				} catch (SQLException e) {
					System.out.println("Not able to load participant ride: " + pride._pid);
					e.printStackTrace();
				}
				allPRides.put(pride._pid, pride);
				System.out.println("Participant Ride: " + pride._pid+" loaded.");
			}
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allPRides;
	}
	
	public static void deletePride(CommutePartiRide pride)
	{
		deletePride(pride._pid);
	}
	
	
	public static void deletePride(int rideid)
	{
		try {
			Statement sql;
		    getConnection();

			sql=objConn.createStatement();
			sql.execute("delete from commutepartiride where rideinfoid="+rideid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
