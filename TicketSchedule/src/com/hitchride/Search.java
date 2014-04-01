package com.hitchride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.access.RideInfoAccess;
import com.hitchride.calc.*;
import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllRides;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;
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
		Environment.getEnv();
		synchronized(this)
		{
			
			RideInfo actRide = null;
			if (request.getQueryString()!=null)
			{
				QueryStringParser qsp = new QueryStringParser(request.getQueryString());
				if (qsp.getString("rid")!=null)
				{
					int rid = qsp.getInt("rid");
					actRide = AllRides.getRides().getRide(rid);
					request.getSession().setAttribute("actRide", actRide);
				}
			}
			
			if (request.getParameter("s") == null || request.getParameter("e") == null)
			{
				actRide = (RideInfo) request.getSession().getAttribute("actRide");
			}
			
			if (actRide ==null)
			{ 
				actRide = new RideInfo();
				actRide.userType = true;
				
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
			    actRide.origLoc = orig;
				
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
				actRide.destLoc = dest;
				
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
								+actRide.origLoc._addr+"&destinations="+actRide.destLoc._addr+"&sensor=false";  
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
				actRide.dura=dura;
				actRide.dist=dist;
				
				Schedule schedule = new Schedule(true); //use dummy as default to deal with null value issue now.
				actRide.schedule = schedule;
				schedule.set_isRoundTrip(false);
				schedule.set_isCommute(false);
				String date = request.getParameter("date");
				Date d = TimeFormatHelper.setDate(date);
				schedule.tripDate = d;
				schedule.forwardTime =  java.sql.Time.valueOf("12:00:00"); 
				schedule.forwardFlexibility =  java.sql.Time.valueOf("12:00:00");
				request.getSession().setAttribute("actRide", actRide);
			}
	
				
			List<Topic> resultList = new ArrayList<Topic>();
			NewScoreCalculator sc = new NewScoreCalculator();
			resultList=sc.filterByCoordinates(actRide, 20);
			
			request.setAttribute("results", resultList);
			request.setAttribute("orig", actRide.origLoc.get_formatedAddr());
			request.setAttribute("dest", actRide.destLoc.get_formatedAddr());
	
			RequestDispatcher rd = request.getRequestDispatcher("../SearchRide.jsp");
			rd.forward(request, response);
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//rideInfoParameters myArgs = new rideInfoParameters();
		synchronized(this)
		{
			int recordId = Integer.parseInt(request.getParameter("rid"));
			RideInfo myRide = AllRides.getRides().getRide(recordId);
			if (myRide==null)
			{
				myRide = new RideInfo();
			}
			
			//*********************************Geo Related***********************
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
		    myRide.origLoc = orig;
			
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
			myRide.destLoc = dest;
			
			if (request.getSession().getAttribute("IsLogin")!=null)
			{
				User user = (User) request.getSession().getAttribute("user");
				myRide.username = user.get_name();
				myRide.userId = user.get_uid();
				if (request.getParameter("userType")!=null)
				{
					myRide.userType = request.getParameter("userType").equals("driver");
				}
				else
				{
					myRide.userType=false;
				}
				
				if (request.getParameter("who")!=null)
				{
					myRide.userType = request.getParameter("who").equals("offer");
				}
				else
				{
					myRide.userType=false;
				}
				 
				//*************** Schedule *******************
				Schedule schedule = new Schedule();
				myRide.schedule=schedule;
				
				schedule.set_isRoundTrip(false);
				
				if (request.getParameter("commuteType")!=null)
				{
					schedule.set_isCommute(request.getParameter("commuteType").equals("commute"));
				}
				else
				{
					schedule.set_isCommute(true);
				}
		
				int dayofweek = Integer.parseInt(request.getParameter("dayofweek"));
				schedule.set_dayOfWeek(dayofweek);
				
				String forwardFlexibility = request.getParameter("flex_global");
				int fflx = Integer.parseInt(forwardFlexibility);
				schedule.forwardFlexibility = new Time(fflx*60000-TimeFormatHelper.systemOffset);
				String backFlexibility = request.getParameter("flex_global");
				int bflx = Integer.parseInt(backFlexibility);
				schedule.backFlexibility = new Time(bflx*60000-TimeFormatHelper.systemOffset);
				
				String cf = request.getParameter("there_time_1");
				schedule.cftime[1] = getTime(cf);
				cf = request.getParameter("there_time_2");
				schedule.cftime[2] = getTime(cf);
				cf = request.getParameter("there_time_3");
				schedule.cftime[3] = getTime(cf);
				cf = request.getParameter("there_time_4");
				schedule.cftime[4] = getTime(cf);
				cf = request.getParameter("there_time_5");
				schedule.cftime[5] = getTime(cf);
				cf = request.getParameter("there_time_6");
				schedule.cftime[6] = getTime(cf);
				cf = request.getParameter("there_time_7");
				schedule.cftime[0] = getTime(cf);
				
				String bt = request.getParameter("back_time_1");
				schedule.cbtime[1] = getTime(bt);
				bt = request.getParameter("back_time_2");
				schedule.cbtime[2] = getTime(bt);
				bt = request.getParameter("back_time_3");
				schedule.cbtime[3] = getTime(bt);
				bt = request.getParameter("back_time_4");
				schedule.cbtime[4] = getTime(bt);
				bt = request.getParameter("back_time_5");
				schedule.cbtime[5] = getTime(bt);
				bt = request.getParameter("back_time_6");
				schedule.cbtime[6] = getTime(bt);
				bt = request.getParameter("back_time_7");
				schedule.cbtime[0] = getTime(bt);
				
				String date =request.getParameter("date");
				schedule.tripDate = TimeFormatHelper.setDate(date);
				if (schedule.isCommute())
				{
					schedule.tripTime = getTime("12:00pm");
				}
				else
				{
					String triptime= request.getParameter("there_time");
					schedule.tripTime = getTripTime(triptime,schedule.forwardFlexibility);
				}
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
								+myRide.origLoc._addr+"&destinations="+myRide.destLoc._addr+"&sensor=false";  
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
				myRide.dura=dura;
				myRide.dist=dist;
				myRide.recordId = Integer.parseInt(request.getParameter("rid"));
				
				if (myRide.recordId==0)
				{
					AllRides.getRides().inser_availride(myRide);
					myRide.insertToDB();
				}
				else
				{
					AllRides.getRides().udpate_availride(myRide);
					myRide.updateDB();
				}
				
				if (AllPartRides.getPartRides().get_participantRide(myRide.recordId)==null)
				{
					ParticipantRide pride = new ParticipantRide(myRide);
					pride.set_status(0);
					pride.set_assoOwnerRideId(-1);
					user.pRides.add(pride);
					AllPartRides.getPartRides().insert_pride(pride);
					pride.insertToDB();
				}
				request.getSession().setAttribute("actRide", myRide);
			}
			
			//TO DO: Sanity check
			List<Topic> resultList = new ArrayList<Topic>();
			NewScoreCalculator sc = new NewScoreCalculator();
			resultList=sc.filterByCoordinates(myRide, 20);
			
			request.setAttribute("results", resultList);
			request.setAttribute("orig", myRide.origLoc._addr);
			request.setAttribute("dest", myRide.destLoc._addr);
			
			RequestDispatcher rd = request.getRequestDispatcher("../SearchRide.jsp");
			rd.forward(request, response);
		}
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
	
	public Time getTime(String timeexp)
	{
		if (timeexp==null || ("NO TRIP").equalsIgnoreCase(timeexp))
		{
			return new Time(36*3600000-TimeFormatHelper.systemOffset);
		}
		String[] time = timeexp.split(":");
		int hour;
		int minute;
		if (time[1].contains("PM"))
		{
			hour = Integer.parseInt(time[0])+12;
			minute = Integer.parseInt(time[1].substring(0, 2));
		}
		else
		{
			hour = Integer.parseInt(time[0]);
			minute = Integer.parseInt(time[1].substring(0, 2));
		}
		Time formattime = new Time(hour*3600000+minute*60000-TimeFormatHelper.systemOffset);
		return formattime;
	}
	
	public Time getTripTime(String timeexp,Time forwardFlex)
	{
		/*
		<option value="anytime" selected="selected">anytime</option>
        <option value="early">early (12a-8a)</option>
        <option value="morning">morning (8a-12p)</option>
        <option value="afternoon">afternoon (12p-5p)</option>
        <option value="evening">evening (5p-9p)</option>
        <option value="night">night (9p-12a)</option>
        */
		if (timeexp.equals("anytime"))
		{
			Time tripTime = new Time(12*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(12*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("early"))
		{
			Time tripTime = new Time(4*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(4*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("morning"))
		{
			Time tripTime = new Time(10*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(2*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("afternoon"))
		{
			Time tripTime = new Time(29*1800000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(5*1800000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("evening"))
		{
			Time tripTime = new Time(19*3600000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(2*3600000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		if (timeexp.equals("night"))
		{
			Time tripTime = new Time(21*1800000-TimeFormatHelper.systemOffset);
			forwardFlex = new Time(3*1800000-TimeFormatHelper.systemOffset);
			return tripTime;
		}
		return getTime(timeexp);
	}
	
}
