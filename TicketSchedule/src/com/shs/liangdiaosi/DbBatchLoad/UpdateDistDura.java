package com.shs.liangdiaosi.DbBatchLoad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.mysql.jdbc.ResultSet;
import com.shs.liangdiaosi.Access.CarpoolTbAccess;

public class UpdateDistDura {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, JSONException
	{
		ResultSet rs;
		rs = CarpoolTbAccess.listAllGeoPosi();
		//while (rs.next())
		for (int i=0;i<2000;i++)
		{
			rs.next(); //363,662
		}
		for (int i=2000;i<2200;i++) //due to api call limitation.
		{
			rs.next();	
			int recordid = rs.getInt("recordid");
			double origLat 	= rs.getDouble("origLat");
			double origLon 	= rs.getDouble("origLon");
			double destLat 	= rs.getDouble("destLat");
			double destLon 	= rs.getDouble("destLon");
			
			String getURL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
			+origLat+","+origLon+"&destinations="+destLat+","+destLon+"&sensor=false";  
			URL getUrl = new URL(getURL);
			HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
			//JSONObject result = new JSONObject();
		
			conn.connect();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(
		                conn.getInputStream()));
		    StringBuffer lines=new StringBuffer();
		    String line;
		    while ((line = reader.readLine()) != null) {
		            lines=lines.append(line);
		            line =  null;
		    }
		    reader.close();
		    conn.disconnect();
		    
		    String jsonoutput=lines.toString();
		    JSONObject json = new JSONObject(jsonoutput);
		    JSONArray elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
		    int duration = elements.getJSONObject(0).getJSONObject("duration").getInt("value");
		    int distance = elements.getJSONObject(0).getJSONObject("distance").getInt("value");
		    
		    CarpoolTbAccess.insertDisDua(recordid, distance, duration, true);
		    if (duration!=0)
		    {
		    System.out.println(recordid+" : "+distance+"m, " +duration+"s, "+distance/duration);
		    }
		}
	}
	
}
