package com.shs.liangdiaosi;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
		if (request.getParameter("commuteType")!=null)
		{
			myArgs.commute = request.getParameter("commuteType").equals("commute");
		}
		else
		{
			myArgs.commute = true;
		}
		myArgs.roundtrip = false;
		
		String time = request.getParameter("there_time_0");
		int flex = Integer.parseInt(request.getParameter("flex_global"));
		
		long time_ms = 1000*60*60*8; // 8 o'clock
		long flex_ms = 1000*60*flex; // 15 minutes
		myArgs.forwardTime = new Time(time_ms);
		myArgs.forwardFlexibility = new Time(flex_ms);
		
		//TO DO: Sanity check
		
		List<rideInfoParameters> resultList = new ArrayList<rideInfoParameters>();
		ScoreCalculator sc = new ScoreCalculator();
		resultList=sc.filterByCoordinates(myArgs, 20);
		
		request.setAttribute("results", resultList);
		RequestDispatcher rd = request.getRequestDispatcher("../search.jsp");
		rd.forward(request, response);
	}

}
