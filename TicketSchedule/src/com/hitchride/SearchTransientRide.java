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
			//GeoInfo is optional for the search. Not taking into account right now
			//Also not search geoinfo at this layer.  Client layer is responsible for guarantee the issue 
			TransientRide tranRide = new TransientRide();
			tranRide.userType = Boolean.parseBoolean(request.getParameter("userType"));
			GeoInfo orig=null;
			try
			{
				Double origLat = Double.parseDouble(request.getParameter("origLat"));
				Double origLon = Double.parseDouble(request.getParameter("origLng"));
			    String s = request.getParameter("s");
				String origAddr = new String(s.getBytes("ISO-8859-1"),"UTF-8");
			    origAddr = origAddr.replaceAll(" ","" );
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
			    String e = request.getParameter("e");
				String destAddr = new String(e.getBytes("ISO-8859-1"),"UTF-8");
			    destAddr = destAddr.replaceAll(" ","" );
			    request.setAttribute("dest", destAddr);
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
			
			request.getSession().setAttribute("tranRide", tranRide);
			request.setAttribute("date", tranRide.rideDate);
			request.setAttribute("orig", tranRide.origLoc.get_formatedAddr());
			request.setAttribute("dest", tranRide.destLoc.get_formatedAddr());
			
			//response.setContentType("text/html; charset=UTF-8");
			 
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
