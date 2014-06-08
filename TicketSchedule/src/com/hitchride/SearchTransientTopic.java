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
					
					JsonHelper jsonhelp = new JsonHelper();
					String tridesJson = jsonhelp.toJson(resultList);
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
				
				JsonHelper jsonhelp = new JsonHelper();
				String tridesJson = jsonhelp.toJson(resultList);
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
