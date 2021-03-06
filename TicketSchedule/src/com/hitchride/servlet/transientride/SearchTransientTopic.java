package com.hitchride.servlet.transientride;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.hitchride.TransientRide;
import com.hitchride.database.access.TransientRideAccess;
import com.hitchride.environ.AllRides;
import com.hitchride.environ.Environment;
import com.hitchride.util.GsonWrapperForTransientRide;
import com.hitchride.util.JsonHelper;
import com.hitchride.util.QueryStringParser;
import com.hitchride.util.TimeFormatHelper;

/**
 * Servlet implementation class SearchTransientRide
 */
public class SearchTransientTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchTransientTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Environment.getEnv();
		{
			if (request.getQueryString()!=null)
			{
				try
				{
					String date = request.getParameter("date");
					Date sdate = TimeFormatHelper.setDate(date);
					List<TransientRide> resultList = TransientRideAccess.listTransisentRideByGroupId(1, sdate);
					
					List<GsonWrapperForTransientRide> rlist = new ArrayList<GsonWrapperForTransientRide>();
					for(Iterator<TransientRide> itr = resultList.iterator();itr.hasNext();)
					{
						GsonWrapperForTransientRide gtr = new GsonWrapperForTransientRide(itr.next());
						rlist.add(gtr);
					}
					
					JsonHelper jsonhelp = new JsonHelper();
					String tridesJson = jsonhelp.toJson(rlist);
					//System.out.println(topicsJson);
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write(tridesJson);
				}
				catch(Exception e)
				{
					System.out.println("Not proper structure of query.");
					response.setContentType("text/html; charset=UTF-8");
					response.sendError(406);
				}
			}
			else
			{
				java.util.Date dateh = new java.util.Date();
				Time time = new Time(dateh.getTime());
				Date date = new Date(dateh.getTime());
				List<TransientRide> resultList = TransientRideAccess.listTransisentRideByGroupId(1, date);
				
				List<GsonWrapperForTransientRide> rlist = new ArrayList<GsonWrapperForTransientRide>();
				for(Iterator<TransientRide> itr = resultList.iterator();itr.hasNext();)
				{
					GsonWrapperForTransientRide gtr = new GsonWrapperForTransientRide(itr.next());
					rlist.add(gtr);
				}
				JsonHelper jsonhelp = new JsonHelper();
				String tridesJson = jsonhelp.toJson(rlist);
				//System.out.println(topicsJson);
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().write(tridesJson);
			}
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
