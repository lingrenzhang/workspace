package com.hitchride;
//Async search result topics based on initialized Ride. Must have active Ride initialized.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.hitchride.calc.NewScoreCalculator;
import com.hitchride.global.AllRides;
import com.hitchride.global.AllTopicRides;
import com.hitchride.global.AllTopics;
import com.hitchride.global.Environment;
import com.hitchride.standardClass.GeoInfo;
import com.hitchride.standardClass.OwnerRideInfo;
import com.hitchride.standardClass.RideInfo;
import com.hitchride.standardClass.Schedule;
import com.hitchride.standardClass.Topic;
import com.hitchride.standardClass.TransientRide;
import com.hitchride.util.GsonWrapperForTopic;
import com.hitchride.util.GsonWrapperForTransientRide;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;

/**
 * Servlet implementation class SearchTopics
 */
public class SearchTopics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	
    public SearchTopics() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Environment.getEnv();
		{
			RideInfo actRide = (RideInfo) request.getSession().getAttribute("actRide");
			if (actRide == null)
			{
				System.out.println("ActRide to search not initialized, showing all ride in DB.");
				
				List<Topic> resultList = AllTopics.getTopics().getTopicRideAsList();
				
				List<GsonWrapperForTopic> rlist = new ArrayList<GsonWrapperForTopic>();
				for(Iterator<Topic> itr = resultList.iterator();itr.hasNext();)
				{
					GsonWrapperForTopic gtr = new GsonWrapperForTopic(itr.next());
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
				request.getSession().setAttribute("actRide", actRide);
				
				List<Topic> resultList = new ArrayList<Topic>();
				NewScoreCalculator sc = new NewScoreCalculator();
				resultList=sc.filterByCoordinates(actRide, 20);
				
				List<GsonWrapperForTopic> rlist = new ArrayList<GsonWrapperForTopic>();
				for(Iterator<Topic> itr = resultList.iterator();itr.hasNext();)
				{
					GsonWrapperForTopic gtr = new GsonWrapperForTopic(itr.next());
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
}
