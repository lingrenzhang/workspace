//@Sean: This class wraps several ways of compute distance
//    0 for self defined;
//    1 for baidu api;
//    2 for google api;
// Could define internal chain of responsibility logic here to increase system robust and efficiency

package com.hitchride.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.standardClass.GeoInfo;

public class DistanceHelper {
	private int computeMode=0; //0 for rough compute; 1 for baidu api; 2 for google api
	private GeoInfo origLoc;
	private GeoInfo destLoc;
	
	private boolean computed = false;
	private int distance;
	private int duration;
	
	public int getDistance()
	{
	  	if (computed) return this.distance;
	   	else
	   		System.out.println("Call computeResult first");
	   	return -1;
	}
	    
	    public int getDuration()
	    {
	    	if (computed) return this.duration;
	    	else
	    		System.out.println("Call computeResult first");
	    	return -1;
	    }
    public DistanceHelper(GeoInfo origLoc, GeoInfo destLoc,int computeMode)
    {
    	this.origLoc = origLoc;
    	this.destLoc = destLoc;
    	if (computeMode>=3 || computeMode<0)
    	{
    		System.out.println("computeMode Invliad. 0 for rough compute; 1 for baidu api; 2 for google api");
    	}
    	else
    	{
    		this.computeMode = computeMode;
    	}
    }
    
    public DistanceHelper(GeoInfo origLoc, GeoInfo destLoc)
    {
    	this.origLoc = origLoc;
    	this.destLoc = destLoc;
    	this.computeMode = 0;
    }
    
    public void changeComputeMode(int computeMode)
    {
    	if (computeMode>=3 || computeMode<0)
    	{
    		System.out.println("computeMode Invliad. 0 for rough compute; 1 for baidu api; 2 for google api");
    	}
    	else
    	{
    		this.computeMode = computeMode;
    	}
    }

    public void computeResult()
    {
    	switch (computeMode)
    	{
	    	case 0:
	    		greatCircleDistance();
	    		break;
	    	case 1:
	    		//TODO : computeDisDuabyBaidu();
	    		System.out.println("Not implement computeDisDuabyBaidu()");
	    		greatCircleDistance();
	    		break;
	    	case 2:
				try {
					computeDisDuabyGoogle();
				} catch (IOException e) {
					System.out.println("Google api call failed. Use rough compute. Please check network.");
					greatCircleDistance();
				} catch (JSONException e) {
					System.out.println("Parse of Google api call failed. Use rough compute now. Please check code.");
					greatCircleDistance();
				}	    		
				break;
    	}
    	this.computed= true;
    }

    
	public void greatCircleDistance(){
		double dLat_rad = (destLoc.get_lat()-origLoc.get_lat())/180*Math.PI; // convert deg to rad
		double dLon_rad = (destLoc.get_lon()-origLoc.get_lon())/180*Math.PI;
		double lat1_rad = origLoc.get_lat()/180*Math.PI;
		double lat2_rad = destLoc.get_lat()/180*Math.PI;
		double a = Math.sin(dLat_rad/2) * Math.sin(dLat_rad/2) +
		        Math.sin(dLon_rad/2) * Math.sin(dLon_rad/2) * Math.cos(lat1_rad) * Math.cos(lat2_rad); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		//double R = 6371; // km
		//double R = 6371/1.6; // mile
		double R = 6371*1000; // meters
		this.distance = (int) Math.floor(R*c);
		this.duration = (int) Math.floor(this.distance/30); //TODO: Use better piecewise checking algorithm later
	}
	
	
   	private void computeDisDuabyGoogle() throws IOException, JSONException
   	{
   		String getURL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
   			+origLoc.get_lat()+","+origLoc.get_lon()+"&destinations="+origLoc.get_lat()+","+origLoc.get_lon()+"&sensor=false";  
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
