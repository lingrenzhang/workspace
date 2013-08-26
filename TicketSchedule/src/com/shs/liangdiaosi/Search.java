package com.shs.liangdiaosi;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.shs.liangdiaosi.Access.CarpoolTbAccess;
import com.shs.liangdiaosi.Calc.*;
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
		
		myArgs.origLat = Double.parseDouble(request.getParameter("origLat"));
		myArgs.origLon = Double.parseDouble(request.getParameter("origLng"));
		myArgs.destLat = Double.parseDouble(request.getParameter("destLat"));
		myArgs.destLon = Double.parseDouble(request.getParameter("destLng"));
		
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
			String origAddr = request.getParameter("s");
			//String destState = request.getParameter("");
			//String destCity = request.getParameter("");
			//String destNbhd = request.getParameter("");
			String destAddr = request.getParameter("e");
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
			double origLat = Double.parseDouble(request.getParameter("origLat"));
			double origLon = Double.parseDouble(request.getParameter("origLng"));
			double destLat = Double.parseDouble(request.getParameter("destLat"));
			double destLon = Double.parseDouble(request.getParameter("destLng"));
			int dist = Integer.parseInt(request.getParameter("distance"));
			int dura = Integer.parseInt(request.getParameter("dtime"));
			try {
				CarpoolTbAccess.postRide(userName, roundtrip, userType, dayOfWeek, "", "", "", origAddr, "",
						"", "", destAddr, detourFactor, forwardTime, forwardFlexibility, backTime, backFlexibility, 
						origLat, origLon, destLat, destLon, dist, dura, false);
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
		RequestDispatcher rd = request.getRequestDispatcher("../search.jsp");
		rd.forward(request, response);
	}
}
