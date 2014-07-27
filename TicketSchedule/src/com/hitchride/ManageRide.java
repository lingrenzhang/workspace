package com.hitchride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;

import com.hitchride.global.AllPartRides;
import com.hitchride.global.AllRides;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.User;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class ManageRide
 */
public class ManageRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QueryStringParser qs = new QueryStringParser(request.getQueryString());
		int rid = qs.getInt("rid");
		RideInfo ride = AllRides.getRides().getRide(rid);
		JsonHelper jh = new JsonHelper();
		String rideJson = jh.toJson(ride);
		System.out.println(rideJson);
		response.getWriter().write(rideJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			schedule.cftime[1] = TimeFormatHelper.getTime(cf);
			cf = request.getParameter("there_time_2");
			schedule.cftime[2] = TimeFormatHelper.getTime(cf);
			cf = request.getParameter("there_time_3");
			schedule.cftime[3] = TimeFormatHelper.getTime(cf);
			cf = request.getParameter("there_time_4");
			schedule.cftime[4] = TimeFormatHelper.getTime(cf);
			cf = request.getParameter("there_time_5");
			schedule.cftime[5] = TimeFormatHelper.getTime(cf);
			cf = request.getParameter("there_time_6");
			schedule.cftime[6] = TimeFormatHelper.getTime(cf);
			cf = request.getParameter("there_time_7");
			schedule.cftime[0] = TimeFormatHelper.getTime(cf);
			
			String bt = request.getParameter("back_time_1");
			schedule.cbtime[1] = TimeFormatHelper.getTime(bt);
			bt = request.getParameter("back_time_2");
			schedule.cbtime[2] = TimeFormatHelper.getTime(bt);
			bt = request.getParameter("back_time_3");
			schedule.cbtime[3] = TimeFormatHelper.getTime(bt);
			bt = request.getParameter("back_time_4");
			schedule.cbtime[4] = TimeFormatHelper.getTime(bt);
			bt = request.getParameter("back_time_5");
			schedule.cbtime[5] = TimeFormatHelper.getTime(bt);
			bt = request.getParameter("back_time_6");
			schedule.cbtime[6] = TimeFormatHelper.getTime(bt);
			bt = request.getParameter("back_time_7");
			schedule.cbtime[0] = TimeFormatHelper.getTime(bt);
			
			String date =request.getParameter("date");
			schedule.tripDate = TimeFormatHelper.setDate(date);
			if (schedule.isCommute())
			{
				schedule.tripTime = TimeFormatHelper.getTime("12:00pm");
			}
			else
			{
				String triptime= request.getParameter("there_time");
				schedule.tripTime = TimeFormatHelper.getTripTime(triptime,schedule.forwardFlexibility);
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
				AllRides.getRides().insert_availride(myRide);
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
		response.sendRedirect("/TicketSchedule/servlet/Search");
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
