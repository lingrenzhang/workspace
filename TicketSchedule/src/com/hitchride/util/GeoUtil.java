package com.hitchride.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.standardClass.GeoInfo;


public class GeoUtil {

	public GeoUtil(){
		
	};

	public GeoInfo reverseGeoCoding(double lat,double lng) throws IOException,JSONException
	{

		String getURL = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+lat+","+lng+"&language=en&sensor=false";  
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
			    
			    GeoInfo geo=null;
				String jsonoutput=lines.toString();
				JSONObject json;
				json = new JSONObject(jsonoutput);
				JSONObject result = json.getJSONArray("results").getJSONObject(0);
				if (result==null)
				{
				    	//System.out.println("Address Not found");
				  	return null;
				}
				String formatedAddress = result.getString("formatted_address");
				JSONObject geometry = result.getJSONObject("geometry");
				double flat = geometry.getJSONObject("location").getDouble("lat");
				double flng = geometry.getJSONObject("location").getDouble("lng");
				geo = new GeoInfo(formatedAddress,flat,flng);			
				return geo;
	}
	
	
	
	public GeoInfo baiduGeoCoding(double lat,double lng) throws IOException,JSONException
	{
		//?ak=ÄúµÄÃÜÔ¿&callback=renderReverse&location=39.983424,116.322987&output=json&pois=0//
        String getURL= "http://api.map.baidu.com/geocoder/v2/?ak=EDf26616cd5fdfb811cc9b56edd8999a"
        		+"&location="+lat+","+lng+"&output=json";
				URL getUrl = new URL(getURL);
				HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
				//JSONObject result = new JSONObject();
			
				conn.connect();
			    BufferedReader reader = new BufferedReader(new InputStreamReader(
			                conn.getInputStream(),"UTF-8"));
			    StringBuffer lines=new StringBuffer();
			    String line;
			    while ((line = reader.readLine()) != null) {
			            lines=lines.append(line);
			            line =  null;
			    }
			    reader.close();
			    conn.disconnect();
			    
			    GeoInfo geo=null;
				String jsonoutput=lines.toString();
				JSONObject json;
				json = new JSONObject(jsonoutput);
				JSONObject result = json.getJSONObject("result");
				if (result==null)
				{
				    System.out.println("Address Not found");
				  	return null;
				}
				String formatedAddress = result.getString("formatted_address");
				//JSONObject addressComponent = result.getJSONObject("addressComponent");
				double flat = result.getJSONObject("location").getDouble("lat");
				double flng = result.getJSONObject("location").getDouble("lng");
				geo = new GeoInfo(formatedAddress,flat,flng);			
				return geo;
	}
	
	public static void main(String[] args) 
	{
		GeoUtil geoUtil = new GeoUtil();
		try {
			GeoInfo origLoc = geoUtil.baiduGeoCoding(31.1,121.8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
