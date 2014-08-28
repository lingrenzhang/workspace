package com.hitchride.servlet.commuteride;
//Async search result topics based on initialized Ride. Must have active Ride initialized.
//Used to initialize the Search Commute Topic page. Manual Search is in SearchCommute.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitchride.GeoInfo;
import com.hitchride.CommuteOwnerRide;
import com.hitchride.CommuteRide;
import com.hitchride.Schedule;
import com.hitchride.CommuteTopic;
import com.hitchride.TransientRide;
import com.hitchride.User;
import com.hitchride.calc.NewScoreCalculator;
import com.hitchride.environ.AllRides;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllTopics;
import com.hitchride.environ.Environment;
import com.hitchride.util.DistanceHelper;
import com.hitchride.util.GsonWrapperForTransientTopic;
import com.hitchride.util.GsonWrapperForTransientRide;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;

/**
 * Servlet implementation class SearchTopics
 */
public class SearchCommuteTopics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	
    public SearchCommuteTopics() {
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
			if (request.getQueryString()!=null)
			{
				QueryStringParser qsp = new QueryStringParser(request.getQueryString());
				if (qsp.getString("rid")!=null) //When request from user center.
				{
					int rid = qsp.getInt("rid");
					actRide = AllRides.getRides().getRide(rid);
					request.getSession().setAttribute("actRide", actRide);
				}
				else //When request from user search. Guarantee input throw client layer check
				{
					User user = (User) request.getSession().getAttribute("user");
					if (user!=null)
					{
						actRide = createNewTempActRide(request);
						request.getSession().setAttribute("actRide", actRide);
					}
				}
			}
			
			if (actRide==null) //actRide not load from user request, check cached actRide
			{
				actRide = (CommuteRide) request.getSession().getAttribute("actRide");
			}

			if (actRide == null)
			{
				System.out.println("ActRide to search not initialized, showing all ride in DB.");
				List<CommuteTopic> resultList = AllTopics.getTopics().getTopicRideAsList();
				
				List<GsonWrapperForTransientTopic> rlist = new ArrayList<GsonWrapperForTransientTopic>();
				for(Iterator<CommuteTopic> itr = resultList.iterator();itr.hasNext();)
				{
					GsonWrapperForTransientTopic gtr = new GsonWrapperForTransientTopic(itr.next());
					rlist.add(gtr);
				}
				JsonHelper jsonhelp = new JsonHelper();
				String topicsJson = jsonhelp.toJson(rlist);
				//response.getWriter().write("{}");
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().write(topicsJson);
			}
			else
			{
				List<CommuteTopic> resultList = new ArrayList<CommuteTopic>();
				NewScoreCalculator sc = new NewScoreCalculator();
				resultList=sc.filterByCoordinates(actRide, 20);
				
				List<GsonWrapperForTransientTopic> rlist = new ArrayList<GsonWrapperForTransientTopic>();
				for(Iterator<CommuteTopic> itr = resultList.iterator();itr.hasNext();)
				{
					GsonWrapperForTransientTopic gtr = new GsonWrapperForTransientTopic(itr.next());
					rlist.add(gtr);
				}
				JsonHelper jsonhelp = new JsonHelper();
				String topicsJson = jsonhelp.toJson(rlist);
				//System.out.println(topicsJson);
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().write(topicsJson);
			}
		}	
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
	
	private CommuteRide createNewTempActRide(HttpServletRequest request)
	{
		CommuteRide actRide = new CommuteRide();
		User user = (User) request.getSession().getAttribute("user");
		
		actRide.set_user(user);
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
			actRide.origLoc = orig;
		}
		catch(Exception e)
		{
			System.out.println("Orig coordinate not initilized on client site.");
			System.out.println("Do more check on UI");
		}
	    
		
	    GeoInfo dest=null;
		try
		{
			Double destLat = Double.parseDouble(request.getParameter("destLat"));
			Double destLon = Double.parseDouble(request.getParameter("destLng"));
		    String destAddr = request.getParameter("e");
		    destAddr = destAddr.replaceAll(" ","" );
		    request.setAttribute("dest", destAddr);
		    dest = new GeoInfo(destAddr,destLat,destLon);
		    actRide.destLoc = dest;
		}
		catch (Exception e)
		{
			System.out.println("Orig coordinate not initilized on client site.");
			System.out.println("Do more check on UI");
		}
		
		if(actRide.origLoc != null && actRide.destLoc != null)
		{	
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
			return actRide;
		}
		else
		{
			return null;
		}
	}
}
