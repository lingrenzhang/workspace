package com.hitchride.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hitchride.global.SQLServerConf;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.TransientTopic;
import com.mysql.jdbc.ResultSet;

public class TransientTopicAccess {
	public static Connection objConn; //This reference is used for batch job.
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException
	{
		Class.forName(SQLServerConf.DriverName);
		objConn = DriverManager.getConnection(SQLServerConf.ServerURL,SQLServerConf.UserName,SQLServerConf.Password);
		return objConn;
	}
	
	public static int insertTransientTopic(TransientTopic topic)
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

		 rows = sql.executeUpdate("insert into TransientTopic values(\"" + 
		    		+ topic.transientRideId + "\",\""
					+ topic.nmiddlePoints +"\",\""
					+ topic.middle[0].get_formatedAddr()+"\",\""
					+ topic.middle[0].get_lat()+"\",\""
					+ topic.middle[0].get_lon()+"\",\""
					+ topic.middle[1].get_formatedAddr()+"\",\""
					+ topic.middle[1].get_lat()+"\",\""
					+ topic.middle[1].get_lon()+"\",\""
					+ topic.middle[2].get_formatedAddr()+"\",\""
					+ topic.middle[2].get_lat()+"\",\""
					+ topic.middle[2].get_lon()+"\",\""
					+ topic.middle[3].get_formatedAddr()+"\",\""
					+ topic.middle[3].get_lat()+"\",\""
					+ topic.middle[3].get_lon()+"\",\""
					+ topic.middle[4].get_formatedAddr()+"\",\""
					+ topic.middle[4].get_lat()+"\",\""
					+ topic.middle[4].get_lon()+"\",\""
					+ topic.nParticipant+"\",\""
					+ topic.partiuid[0]+"\","
					+ topic.partiuid[1]+",\""
					+ topic.partiuid[2]+"\",\""
					+ topic.partiuid[3]+"\",\""
			        + topic.partiuid[4]+"\")");
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

	public static int updateTransientTopic(TransientTopic topic) {
		int rows=0;
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
			
			rows = sql.executeUpdate("update TransientTopic set " 
					+ "nmiddlePoints=\"" + topic.nmiddlePoints+"\","
					+ "middle1Faddr=\""	 + topic.middle[0].get_formatedAddr()+"\","
					+ "middle1Lat=\"" + topic.middle[0].get_lat()+"\","
					+ "middle1Lng=\""+ topic.middle[0].get_lon()+"\","
					+ "middle2Faddr=\""	 + topic.middle[1].get_formatedAddr()+"\","
					+ "middle2Lat=\""	 + topic.middle[1].get_lat()+"\","
					+ "middle2Lng=\"" + topic.middle[1].get_lon()+"\","
					+ "middle3Faddr=\""	 + topic.middle[2].get_formatedAddr()+"\","
					+ "middle3Lat=\"" + topic.middle[2].get_lat()+"\","
					+ "middle3Lng=\""+ topic.middle[2].get_lon()+"\","
					+ "middle4Faddr=\""+ topic.middle[3].get_formatedAddr()+"\","
					+ "middle4Lat=\""   + topic.middle[3].get_lat()+"\","
					+ "middle4Lng=\""  + topic.middle[3].get_lon()+"\","
					+ "middle5Faddr=\""+ topic.middle[4].get_formatedAddr()+"\","
					+ "middle5Lat=\""   + topic.middle[4].get_lat()+"\","
					+ "middle5Lng=\""  + topic.middle[4].get_lon()+"\","
					+ "nParticipant=\""  + topic.nParticipant+"\","
					+ "partiuid1=\"" + topic.partiuid[0]+"\","
					+ "partiuid2=\"" + topic.partiuid[1]+"\","
					+ "partiuid3=\"" + topic.partiuid[2]+"\","
					+ "partiuid4="+ topic.partiuid[3]+","
					+ "partiuid5=\""+ topic.partiuid[4]+"\" "
					+ "where transientRideId=\""+topic.transientRideId+"\"");
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
	
	public static TransientTopic getTransientTopicById(int trid)
	{
		TransientTopic ttopic = new TransientTopic(trid);
		try
		{
			Statement sql;
			if (objConn==null)
			{
				objConn = getConnection();
			} 
			sql=objConn.createStatement();
		
			ResultSet rs= (ResultSet) sql.executeQuery("select * from transientTopic where transientRideId="+trid);
			if (rs.next())
			{
				ttopic.nmiddlePoints = rs.getInt("nmiddlePoints");
				for(int i=1;i<=ttopic.nmiddlePoints;i++)
				{
					String addr = rs.getString("middle"+i+"Faddr");
					double lat = rs.getDouble("middle"+i+"Lat");
					double lng = rs.getDouble("middle"+i+"Lng");
					GeoInfo geoinfo = new GeoInfo(addr,lat,lng);
					ttopic.middle[i-1]=geoinfo;
				}
				ttopic.nParticipant = rs.getInt("nParticipant");
				for (int i=1;i<ttopic.nParticipant;i++)
				{
					ttopic.partiuid[i-1]=rs.getInt("partiuid"+i);
				}
			}
			else
			{
				return null;
			}
			
		}catch (java.lang.ClassNotFoundException e){
			System.err.println("ClassNotFoundException:"+e.getMessage());
		}
		catch (SQLException e)
		{
			System.err.println("SQLException:"+e.getMessage());
		}
		return ttopic;
		
	}
}
