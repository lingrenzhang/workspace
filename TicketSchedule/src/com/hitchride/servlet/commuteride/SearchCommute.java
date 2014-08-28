package com.hitchride.servlet.commuteride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;





import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.GeoInfo;
import com.hitchride.CommuteRide;
import com.hitchride.Schedule;
import com.hitchride.environ.AllRides;
import com.hitchride.environ.Environment;
import com.hitchride.util.DistanceHelper;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;
/**
 * Servlet implementation class Search
 */
public class SearchCommute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchCommute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Environment.getEnv();
		{
			CommuteRide actRide = null;
						
			if (request.getParameter("s") == null || request.getParameter("e") == null)
			{
				actRide = (CommuteRide) request.getSession().getAttribute("actRide");
			}
			
			if (actRide ==null) //Create new tempor ride
			{ 
				actRide = new CommuteRide();
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
					System.out.println("Caculating from server. user simple result now");
					DistanceHelper distancehelper = new DistanceHelper(actRide.origLoc,actRide.destLoc,0);
					distancehelper.computeResult();
					dist = distancehelper.getDistance();
					dura = distancehelper.getDuration();
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
				schedule.tripTime =  java.sql.Time.valueOf("12:00:00"); 
				schedule.forwardFlexibility =  java.sql.Time.valueOf("12:00:00");
				request.getSession().setAttribute("actRide", actRide);
			}
				
			request.setAttribute("orig", actRide.origLoc.get_formatedAddr());
			request.setAttribute("dest", actRide.destLoc.get_formatedAddr());
			
			RequestDispatcher rd = request.getRequestDispatcher("/Zh/SearchCommuteTopic.jsp");
			rd.forward(request, response);
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//rideInfoParameters myArgs = new rideInfoParameters();
			CommuteRide myRide = (CommuteRide) request.getSession().getAttribute("actRide");
		/*
			List<Topic> resultList = new ArrayList<Topic>();
			NewScoreCalculator sc = new NewScoreCalculator();
			resultList=sc.filterByCoordinates(myRide, 20);
			
			request.setAttribute("results", resultList);
		*/
			request.setAttribute("orig", myRide.origLoc._addr);
			request.setAttribute("dest", myRide.destLoc._addr);
			
			RequestDispatcher rd = request.getRequestDispatcher("/TicketSchedule/Zh/SearchCommuteTopic.jsp");
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
