package com.hitchride.servlet.transientride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.GeoInfo;
import com.hitchride.Message;
import com.hitchride.TransientRide;
import com.hitchride.TransientTopic;
import com.hitchride.User;
import com.hitchride.environ.AllUsers;
import com.hitchride.environ.Environment;
import com.hitchride.util.TimeFormatHelper;

/**
 * Servlet implementation class SearchTransientRide
 */
public class PublishTransientTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PublishTransientTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Environment.getEnv();
		boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
		if (!islogin)
		{
			request.getSession().setAttribute("fromLocation", "/TicketSchedule/Zh/SearchTransientRide.jsp");
			request.getSession().setAttribute("queryString", request.getQueryString());
			request.getSession().setMaxInactiveInterval(60*120);
			response.sendRedirect("/TicketSchedule/Login.jsp");
		}
		else
		{
			//GeoInfo is optional for the search. Not taking into account right now
			//Also not search geoinfo at this layer.  Client layer is responsible for guarantee the issue 
			TransientRide tranRide = new TransientRide();
			tranRide.owner = (User) request.getSession().getAttribute("user");
			tranRide.ownerId = tranRide.owner.get_uid();
			tranRide.id = Environment.getEnv().maxTranRideId+1;
			Environment.getEnv().maxTranRideId++;
			tranRide.userType = Boolean.parseBoolean(request.getParameter("userType"));
			GeoInfo orig=null;
			try
			{
				Double origLat = Double.parseDouble(request.getParameter("origLat"));
				Double origLon = Double.parseDouble(request.getParameter("origLng"));
			    String origAddr = request.getParameter("s");
			    orig = new GeoInfo(origAddr,origLat,origLon);
				request.setAttribute("orig", origAddr);
			}
			catch(Exception e)
			{
				System.out.println("Orig coordinate not initilized on client site for transient ride and ignore it");
			}

			tranRide.origLoc = orig;    
			GeoInfo dest=null;
			try
			{
				Double destLat = Double.parseDouble(request.getParameter("destLat"));
				Double destLon = Double.parseDouble(request.getParameter("destLng"));
			    String destAddr = request.getParameter("e");
			    dest = new GeoInfo(destAddr,destLat,destLon);
			}
			catch (Exception e)
			{
				System.out.println("Target coordinate for transient ride not initilized on client site. Ignore it");
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
				System.out.println("Distance and duration not initilized on client site. Ignore it.");
			}
			tranRide.dura=dura;
			tranRide.dist=dist;
				
			String date = request.getParameter("date");
			Date d = TimeFormatHelper.setDate(date);
			tranRide.rideDate=d;
			
			
			tranRide.rideFlex =new Time(15*60000-TimeFormatHelper.systemOffset);
			long rtime =Integer.parseInt(request.getParameter("time_hour"))*3600000+Integer.parseInt(request.getParameter("time_minute"))*60000;
			tranRide.rideTime =new Time(rtime-TimeFormatHelper.systemOffset);
			
			tranRide.totalSeats = Integer.parseInt(request.getParameter("seats"));
			tranRide.price = Double.parseDouble(request.getParameter("price"));
					
			tranRide.insertToDB();
			TransientTopic trantopic = new TransientTopic(tranRide.id);
			trantopic.insertToDB();
			tranRide.owner.insertTopicCommuteRide(tranRide.id);
		
			request.getSession().setAttribute("tranRide", tranRide);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(""+tranRide.id);
			
			Message message = new Message(null,tranRide.owner,-1,tranRide,"");
			message.sendMessage();
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
