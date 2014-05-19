package com.hitchride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.access.TransientRideAccess;
import com.hitchride.global.AllRides;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.TransientRide;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;

/**
 * Servlet implementation class SearchTransientRide
 */
public class SearchTransientRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchTransientRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Environment.getEnv();
		{
			TransientRide tranRide = new TransientRide();
			tranRide.userType = Boolean.parseBoolean(request.getParameter("userType"));
			GeoInfo orig=null;
			try
			{
				Double origLat = Double.parseDouble(request.getParameter("origLat"));
				Double origLon = Double.parseDouble(request.getParameter("origLng"));
			    String origAddr = request.getParameter("s");
			    origAddr = origAddr.replaceAll(" ","" );
			    orig = new GeoInfo(origAddr,origLat,origLon);
				request.setAttribute("orig", origAddr);
			}
			catch(Exception e)
			{
				System.out.println("Orig coordinate not initilized on client site.");
				System.out.println("Caculating from server");
				//origLat,OrigLng for some reason not caculated. Compute it from the original space
				String jsonoutput;
				JSONObject json;
				String start = request.getParameter("s");
				start = start.replaceAll(" ","" );
				//String getURL = "https://maps.googleapis.com/maps/api/place/autocomplete/xml?input=" + start +"&types=geocode&sensor=true&key=";
				String getURL = "http://maps.googleapis.com/maps/api/geocode/json?address="+start+"&sensor=false";
				jsonoutput=jsonquery(getURL);
				
				try {
					json = new JSONObject(jsonoutput);
					String origAddr = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
					origAddr = origAddr.replaceAll(" ","" );
					request.setAttribute("orig", origAddr);
					JSONObject geometry = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
					Double origLat =geometry.getJSONObject("location").getDouble("lat");
					Double origLon =geometry.getJSONObject("location").getDouble("lng");
					orig = new GeoInfo(origAddr,origLat,origLon);
				} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
				}
			}

			tranRide.origLoc = orig;    
			GeoInfo dest=null;
			try
			{
				Double destLat = Double.parseDouble(request.getParameter("destLat"));
				Double destLon = Double.parseDouble(request.getParameter("destLng"));
			    String destAddr = request.getParameter("e");
			    destAddr = destAddr.replaceAll(" ","" );
			    request.setAttribute("dest", destAddr);
			    dest = new GeoInfo(destAddr,destLat,destLon);
			}
			catch (Exception e)
			{
				System.out.println("Target coordinate not initilized on client site.");
				System.out.println("Caculating from server");
				String end = request.getParameter("e");
				end = end.replace(" ", "");
					//getURL = "https://maps.googleapis.com/maps/api/place/autocomplete/xml?input=" + end +"&types=geocode&sensor=true&key=";
				String getURL = "http://maps.googleapis.com/maps/api/geocode/json?address="+end+"&sensor=false";
				String jsonoutput=jsonquery(getURL);
				try {
					JSONObject json = new JSONObject(jsonoutput);
				    String destAddr = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
				    destAddr = destAddr.replaceAll(" ","" );
				    request.setAttribute("dest", destAddr);
				    JSONObject geometry = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
				    Double destLat =geometry.getJSONObject("location").getDouble("lat");
				    Double destLon =geometry.getJSONObject("location").getDouble("lng");
				    dest = new GeoInfo(destAddr,destLat,destLon);
				} catch (JSONException e1) {
						// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			tranRide.destLoc = dest;
				
			//Distance duration
			int dist=0;
			int dura=0;
			try{
				dist = Integer.parseInt(request.getParameter("distance"));
				dura = Integer.parseInt(request.getParameter("duration"));
			}
			catch(Exception e)
			{
				System.out.println("Distance and duration not initilized on client site.");
				System.out.println("Caculating from server");
				//Caculate by coordinate. Not very stable.
				//getURL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
				//	+origLat+","+origLon+"&destinations="+destLat+","+destLon+"&sensor=false"; 
				String getURL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+tranRide.origLoc._addr+"&destinations="+tranRide.destLoc._addr+"&sensor=false";  
				String jsonoutput=jsonquery(getURL);
				JSONObject json;
				try {
					json = new JSONObject(jsonoutput);
				    JSONArray elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
				    dura = elements.getJSONObject(0).getJSONObject("duration").getInt("value");
				    dist = elements.getJSONObject(0).getJSONObject("distance").getInt("value");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
			tranRide.dura=dura;
			tranRide.dist=dist;
				
			String date = request.getParameter("date");
			Date d = TimeFormatHelper.setDate(date);
			tranRide.rideDate=d;
			
			
			request.getSession().setAttribute("tranRide", tranRide);
			request.setAttribute("date", tranRide.rideDate);
			request.setAttribute("orig", tranRide.origLoc.get_formatedAddr());
			request.setAttribute("dest", tranRide.destLoc.get_formatedAddr());
			
			request.setCharacterEncoding("UTF8");
			RequestDispatcher rd = request.getRequestDispatcher("../Zh/SearchTransientRide.jsp");
			rd.forward(request, response);
		}	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	
	public String jsonquery(String getURL) throws IOException
	{
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
	    return lines.toString();
	}
}
