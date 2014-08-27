package com.hitchride.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;

import com.hitchride.GeoInfo;
import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteRide;
import com.hitchride.Schedule;
import com.hitchride.User;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllRides;
import com.hitchride.util.DistanceHelper;
import com.hitchride.util.GsonWrapperForCommuteRide;
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
public class PublishRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PublishRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QueryStringParser qs = new QueryStringParser(request.getQueryString());
		int rid = qs.getInt("rid");
		CommuteRide ride = AllRides.getRides().getRide(rid);
		GsonWrapperForCommuteRide cr = new GsonWrapperForCommuteRide(ride);
		JsonHelper jh = new JsonHelper();
		String rideJson = jh.toJson(cr);
		System.out.println(rideJson);
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(rideJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int recordId = Integer.parseInt(request.getParameter("rid"));
		CommuteRide myRide = AllRides.getRides().getRide(recordId);
		if (myRide==null)
		{
			myRide = new CommuteRide();
		}
		
		//*********************************Geo Related***********************
		GeoInfo orig=null;
		try
		{
			Double origLat = Double.parseDouble(request.getParameter("origLat"));
			Double origLon = Double.parseDouble(request.getParameter("origLng"));
		    String origAddr = new String(request.getParameter("s").getBytes(
					"iso-8859-1"), "UTF-8");
		    //origAddr = origAddr.replaceAll(" ","" );
		    orig = new GeoInfo(origAddr,origLat,origLon);
			request.setAttribute("orig", origAddr);
		}
		catch(Exception e)
		{
			System.out.println("Orig coordinate not initilized on client site.");
			System.out.println("Check logic Please");
		}
	    myRide.origLoc = orig;
		
	    GeoInfo dest=null;
		try
		{
			Double destLat = Double.parseDouble(request.getParameter("destLat"));
			Double destLon = Double.parseDouble(request.getParameter("destLng"));
		    String destAddr = new String(request.getParameter("e").getBytes(
					"iso-8859-1"), "UTF-8");
		    //destAddr = destAddr.replaceAll(" ","" );
		    request.setAttribute("dest", destAddr);
		    dest = new GeoInfo(destAddr,destLat,destLon);
		}
		catch (Exception e)
		{
			System.out.println("Target coordinate not initilized on client site.");
			System.out.println("Check logic please");
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
				System.out.println("Caculating from server. user simple result now");
				DistanceHelper distancehelper = new DistanceHelper(myRide.origLoc,myRide.destLoc,0);
				distancehelper.computeResult();
				dist = distancehelper.getDistance();
				dura = distancehelper.getDuration();
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
				CommutePartiRide pride = new CommutePartiRide(myRide);
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
