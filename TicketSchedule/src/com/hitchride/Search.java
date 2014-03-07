package com.hitchride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.calc.*;
import com.hitchride.global.AllRides;
import com.hitchride.global.DummyData;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.ParticipantRide;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.User;
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
		RideInfo actRide = (RideInfo) request.getSession().getAttribute("actRide");
		if (actRide==null)
		{

		}
		else
		{
		
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//rideInfoParameters myArgs = new rideInfoParameters();
		RideInfo myRide = new RideInfo();
		
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
	
			schedule.set_dayOfWeek(1234);
			
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
			schedule.forwardTime = new Time(fhour*3600000+fminute*60000);
			
			String forwardFlexibility = request.getParameter("flex_global");
			int fflx = Integer.parseInt(forwardFlexibility);
			schedule.forwardFlexibility = new Time(fflx*60000);
			
			
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
			schedule.backTime = new Time(bhour*3600000+bminute*60000);
			
			String backFlexibility = request.getParameter("flex_global");
			int bflx = Integer.parseInt(backFlexibility);
			schedule.backFlexibility = new Time(bflx*60000);
			
			//Distance duration
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
							+myRide.origLoc._addr+"&destinations="+myRide.destLoc._addr+"&sensor=false";  
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
			myRide.dura=dura;
			myRide.dist=dist;
		
		
			/*Not insert the new ride, or put the ride to normal ride queue. Only valid after decide to join a new ride.
			try {
				CarpoolTbAccess.postRide(user.get_name(), roundtrip, userType, dayOfWeek, "", "", "", myArgs.origAddr, "",
						"", "", myArgs.destAddr, detourFactor, forwardTime, forwardFlexibility, backTime, backFlexibility, 
						myArgs.origLat, myArgs.origLon, myArgs.destLat, myArgs.destLon, dist, dura, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			//Temporary saved in memory before deciding whether to put it as participant or ownerride
			
			//ParticipantRide pride = new ParticipantRide(ride);
			//pride.set_status(0);
			AllRides.getRides().inser_availride(myRide);
			user.rides.add(myRide);

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
