package com.hitchride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.access.CarpoolTbAccess;
import com.hitchride.calc.*;
import com.hitchride.calc.ScoreCalculator;
import com.hitchride.calc.rideInfoParameters;
/**
 * Servlet implementation class Search
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String qs= request.getQueryString();
		request.getAttribute("oLat");
		rideInfoParameters myArgs = new rideInfoParameters();
		myArgs.commute = true;
		myArgs.roundtrip = false;
		myArgs.userType = false;
		
		if (qs==null)
		{
		// Stanford coordinates
		myArgs.origLat = 37.42573;
		myArgs.origLon = -122.166094;
		
		// Ranch 99 Cupertino
		myArgs.destLat = 37.338022;
		myArgs.destLon = -122.015118;
		}
		else
		{
			String[] args=qs.split("&");
			
			myArgs.origLat = Double.parseDouble(args[2].split("=")[1]); 
			myArgs.origLon = Double.parseDouble(args[3].split("=")[1]); 
			myArgs.destLat = Double.parseDouble(args[4].split("=")[1]); 
			myArgs.destLon = Double.parseDouble(args[5].split("=")[1]); 
		}
		// Time
		long time_ms = 1000*60*60*8; // 8 o'clock
		long flex_ms = 1000*60*15; // 15 minutes
		myArgs.forwardTime = new Time(time_ms);
		myArgs.forwardFlexibility = new Time(flex_ms);
		
		List<rideInfoParameters> resultList = new ArrayList<rideInfoParameters>();
		ScoreCalculator sc = new ScoreCalculator();
		resultList=sc.filterByCoordinates(myArgs, 20);
		
		request.setAttribute("results", resultList);
		RequestDispatcher rd = request.getRequestDispatcher("../search.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		rideInfoParameters myArgs = new rideInfoParameters();
		
		long time_ms = 1000*60*60*8; // 8 o'clock
		long flex_ms = 1000*60*15; // 15 minutes
		myArgs.forwardTime = new Time(time_ms);
		myArgs.forwardFlexibility = new Time(flex_ms);
		try
		{
			myArgs.origLat = Double.parseDouble(request.getParameter("origLat"));
			myArgs.origLon = Double.parseDouble(request.getParameter("origLng"));
			String start = request.getParameter("s");
			start = start.replaceAll(" ","" );
			request.setAttribute("orig", start);
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
			    myArgs.origAddr = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
			    myArgs.origAddr = myArgs.origAddr.replaceAll(" ","" );
			    request.setAttribute("orig", myArgs.origAddr);
			    JSONObject geometry = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
			    myArgs.origLat =geometry.getJSONObject("location").getDouble("lat");
			    myArgs.origLon =geometry.getJSONObject("location").getDouble("lng");
		    
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
		try
		{
			myArgs.destLat = Double.parseDouble(request.getParameter("destLat"));
			myArgs.destLon = Double.parseDouble(request.getParameter("destLng"));
			String end = request.getParameter("e");
			end = end.replace(" ", "");
			request.setAttribute("dest", end);
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
			    myArgs.destAddr = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
			    myArgs.destAddr = myArgs.destAddr.replaceAll(" ","" );
			    request.setAttribute("dest", myArgs.destAddr);
			    JSONObject geometry = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
			    myArgs.destLat =geometry.getJSONObject("location").getDouble("lat");
			    myArgs.destLon =geometry.getJSONObject("location").getDouble("lng");
			    
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		if (request.getParameter("who")!=null)
		{
		myArgs.userType = request.getParameter("who").equals("offer");
		}
		else
		{
			myArgs.userType=false;
		}
		myArgs.roundtrip = false;
		
		if (request.getParameter("commuteType")!=null)
		{
			myArgs.commute = request.getParameter("commuteType").equals("commute");
		}
		else
		{
			myArgs.commute = true;
		}

		if (request.getSession().getAttribute("IsLogin")!=null)
		{
			String userName = (String) request.getSession().getAttribute("userName");
			boolean roundtrip = true;
			boolean userType = request.getParameter("who").equals("offer");
			//int dayOfWeek = Integer.parseInt(request.getParameter(""));
			int dayOfWeek = 1234;
			//String origState = request.getParameter("");
			//String origCity = request.getParameter("");
			//String origNbhd = request.getParameter("");
			//origAddr = request.getParameter("s");
			//request.setAttribute("origAddr", origAddr);
			//String destState = request.getParameter("");
			//String destCity = request.getParameter("");
			//String destNbhd = request.getParameter("");
			//destAddr = request.getParameter("e");
			//request.setAttribute("destAddr", destAddr);
			//String detourFactor = request.getParameter("");
			String detourFactor = "0.2";
			String forwardTime = request.getParameter("there_time_0");
			String[] ftime = forwardTime.split(":");
			int fhour;
			int fminute;
			if (ftime[1].contains("pm"))
			{
				fhour = Integer.parseInt(ftime[0])+12;
				fminute = Integer.parseInt(ftime[1].substring(0, 2));
			}
			else
			{
				fhour = Integer.parseInt(ftime[0]);
				fminute = Integer.parseInt(ftime[1].substring(0, 2));
			}
			forwardTime = fhour + ":" + fminute+ ":00";
			myArgs.forwardTime = new Time(fhour*3600000+fminute*60000);
			
			String forwardFlexibility = request.getParameter("flex_global");
			int fflx = Integer.parseInt(forwardFlexibility);
			myArgs.forwardFlexibility = new Time(fflx*60000);
			
			//String forwardFlexibility = "30";
			String backTime = request.getParameter("back_time_0");
			String[] btime = backTime.split(":");
			int bhour;
			int bminute;
			if (btime[1].contains("pm"))
			{
				bhour = Integer.parseInt(btime[0])+12;
				bminute = Integer.parseInt(btime[1].substring(0, 2));
			}
			else
			{
				bhour = Integer.parseInt(btime[0]);
				bminute = Integer.parseInt(btime[1].substring(0, 2));
			}
			backTime = bhour + ":" + bminute+ ":00";
			myArgs.backTime = new Time(bhour*3600000+bminute*60000);
			
			String backFlexibility = request.getParameter("flex_global");
			int bflx = Integer.parseInt(backFlexibility);
			myArgs.backFlexibility = new Time(bflx*60000);
			
			
			//String backFlexibility = "30";

			int dist=0;
			int dura=0;
			try{
				dist = Integer.parseInt(request.getParameter("distance"));
				dura = Integer.parseInt(request.getParameter("dtime"));
			}
			catch(Exception e)
			{
				System.out.println("Distance and duration not initilized on client site.");
				System.out.println("Caculating from server");
				//Caculate by coordinate. Not very stable.
				//getURL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
				//	+origLat+","+origLon+"&destinations="+destLat+","+destLon+"&sensor=false"; 
				String getURL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+myArgs.origAddr+"&destinations="+myArgs.destAddr+"&sensor=false";  
				String jsonoutput=jsonquery(getURL);
				JSONObject json;
				try {
					json = new JSONObject(jsonoutput);
				    JSONArray elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
				    dura = elements.getJSONObject(0).getJSONObject("duration").getInt("value");
				    dist = elements.getJSONObject(0).getJSONObject("distance").getInt("value");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
			
			try {
				CarpoolTbAccess.postRide(userName, roundtrip, userType, dayOfWeek, "", "", "", myArgs.origAddr, "",
						"", "", myArgs.destAddr, detourFactor, forwardTime, forwardFlexibility, backTime, backFlexibility, 
						myArgs.origLat, myArgs.origLon, myArgs.destLat, myArgs.destLon, dist, dura, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//TO DO: Sanity check
		
		List<rideInfoParameters> resultList = new ArrayList<rideInfoParameters>();
		ScoreCalculator sc = new ScoreCalculator();
		resultList=sc.filterByCoordinates(myArgs, 20);
		
		request.setAttribute("results", resultList);
		request.setAttribute("orig", myArgs.origAddr);
		request.setAttribute("dest", myArgs.destAddr);
		
		RequestDispatcher rd = request.getRequestDispatcher("../search.jsp");
		rd.forward(request, response);
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
