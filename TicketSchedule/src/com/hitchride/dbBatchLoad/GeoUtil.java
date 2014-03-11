package com.hitchride.dbBatchLoad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoUtil {
	public int distance;
	public int duration;
	public double origLat;
	public double origLon;
	public double destLat;
	public double destLon;
	public GeoUtil(double origLat,double origLon,double destLat,double destLon)
	{
		//Sanity check here
		this.origLat=origLat;
		this.origLon=origLon;
		this.destLat=destLat;
		this.destLon=destLon;
	}
	
	public void getDisDua() throws IOException, JSONException
	{
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
		    this.duration = elements.getJSONObject(0).getJSONObject("duration").getInt("value");
		    this.distance = elements.getJSONObject(0).getJSONObject("distance").getInt("value");
	}
}
