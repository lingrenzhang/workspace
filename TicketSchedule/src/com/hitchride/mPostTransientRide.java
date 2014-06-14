package com.hitchride;

/*
PostTransientRide API

Method: POST
Field: 
userType: true/false(Passenger(Taxi)=true,Driver=false)
origLat: source Lat
origLng: source Lng
origAddr: source address
destLat: destination Lat
destLng: destination Lng
destAddr: destination address
distance: distance
duration: duration
date: date
time: time
seats: seats
price: price

Return: json
Format:
{
"status": "successful|failed",
"reason": String,
"trId": TransientRide ID,
"redirect": target URL
}
*/

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

import com.google.gson.Gson;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.TransientRide;
import com.hitchride.standardClass.TransientTopic;
import com.hitchride.standardClass.User;
import com.hitchride.util.TimeFormatHelper;

/**
 * Servlet implementation class SearchTransientRide
 */
public class mPostTransientRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class mPostTransientRideJson {
		String status = "failed";
		String reason = "";
		int trId = 0;
		String redirect = "";
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mPostTransientRide() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		mPostTransientRideJson json = new mPostTransientRideJson();
		Environment.getEnv();
		boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
		if (!islogin)
		{
			json.reason="not login";
			json.redirect="login.html";
		}
		else
		{
			TransientRide tranRide = new TransientRide();
			tranRide.owner = (User) request.getSession().getAttribute("user");
			tranRide.userId = tranRide.owner.get_uid();
			tranRide.transientRideId = Environment.getEnv().maxTranRideId+1;
			Environment.getEnv().maxTranRideId++;
			tranRide.userType = Boolean.parseBoolean(request.getParameter("userType"));
			GeoInfo orig=null;
			try
			{
				Double origLat = Double.parseDouble(request.getParameter("origLat"));
				Double origLon = Double.parseDouble(request.getParameter("origLng"));
			    String origAddr = request.getParameter("origAddr");
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
			    String destAddr = request.getParameter("destAddr");
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
			TransientTopic trantopic = new TransientTopic(tranRide.transientRideId);
			trantopic.insertToDB();
			
			json.status = "successful";
			json.reason="";
			json.trId=tranRide.transientRideId;
			json.redirect="transientride.html#"+json.trId;
			
		}
		Gson gson = new Gson();
		response.getWriter().write(gson.toJson(json));
	}
}
