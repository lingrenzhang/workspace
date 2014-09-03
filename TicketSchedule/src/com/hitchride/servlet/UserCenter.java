package com.hitchride.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.Message;
import com.hitchride.CommuteOwnerRide;
import com.hitchride.CommutePartiRide;
import com.hitchride.CommuteTopic;
import com.hitchride.TransientRide;
import com.hitchride.User;
import com.hitchride.UserProfile;
import com.hitchride.database.access.TransientRideAccess;
import com.hitchride.database.access.UserAccess;
import com.hitchride.environ.AllPartRides;
import com.hitchride.environ.AllTopicRides;
import com.hitchride.environ.AllTopics;
import com.hitchride.environ.Environment;
import com.hitchride.language.Dictionary;
import com.hitchride.util.QueryStringParser;


/**
 * Servlet implementation class UserCenter
 */
public class UserCenter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserCenter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Environment.getEnv();
		{
			boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
			if (!islogin)
			{
				if (request.getParameter("language").equalsIgnoreCase("ZH"))
				{
					request.getSession().setAttribute("fromLocation", "/TicketSchedule/Zh/UserCenter.jsp");
					request.getSession().setAttribute("queryString", request.getQueryString());
					request.getSession().setMaxInactiveInterval(60*120);
					response.sendRedirect("/TicketSchedule/Zh/Login.jsp");
				}
				else
				{	
				request.getSession().setAttribute("fromLocation", "/TicketSchedule/Zh/UserCenter.jsp");
				request.getSession().setAttribute("queryString", request.getQueryString());
				request.getSession().setMaxInactiveInterval(60*120);
				response.sendRedirect("/TicketSchedule/Login.jsp");
				}
			}
			else
			{
				User user = (User) request.getSession().getAttribute("user");
				QueryStringParser qs = new QueryStringParser(request.getQueryString());
				String content = qs.getString("content");
				String language = qs.getString("language");
				Dictionary dic;
				if(null !=language && "Zh".equals(language))
				{
					dic = new Dictionary("Zh");
				}
				else
				{
					dic = new Dictionary("En");
				}
				if (content.equalsIgnoreCase("messages"))
				{
					StringBuilder value = new StringBuilder();
				    value.append("<div class=\"panel panel-default\">");
					value.append("<div class=\"panel-heading\">"+dic.MyMessages+"</div>");
					value.append("<div class=\"panel-body\">");
					for( Iterator<Message> mesI = user.message.iterator();mesI.hasNext();){ 
					    value.append(mesI.next().getHTMLMessage());
					}
					value.append("</div></div></div>");
					user.numofnewMessage = 0;
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write(value.toString());
				}
				if (content.equalsIgnoreCase("topics"))
				{
					StringBuilder value = new StringBuilder(1000);
				    value.append("<div class=\"panel panel-default\">");
					value.append("<div class=\"panel-heading\">"+dic.MyTopic+"</div>");
					value.append("<div class=\"panel-body\">");
					for(Iterator<CommuteOwnerRide> rideI = user.tRides.iterator();rideI.hasNext(); )
					{
						CommuteOwnerRide ownerRide = AllTopicRides.getTopicRides().getRide(rideI.next()._recordId);
						if (ownerRide!=null)
						{
							value.append("<a href=\"/TicketSchedule/CommuteTopicCenter?topicId="+ownerRide._recordId+"\">");
							value.append("<div class=\"ride_wrapper\">");	
							value.append(ownerRide.getGeoHTML());
							value.append(ownerRide.getScheduleHTML());
							value.append("</div></a>");
							CommuteTopic topic= AllTopics.getTopics().get_topic(ownerRide._recordId);
						}
					}
					
					user.refreshTopicCommuteRide();
					for(Iterator<Integer> rideI = user.topicCommuteRide.iterator();rideI.hasNext(); )
					{
						int rideId = rideI.next();
						TransientRide ownerTRide = TransientRideAccess.getTransisentRideById(rideId);
						if (ownerTRide!=null)
						{
							value.append("<a href=\"/TicketSchedule/Zh/TransientTopic.jsp?trId="+ownerTRide.transientRideId+"\">");
							value.append("<div class=\"ride_wrapper\">");	
							value.append(ownerTRide.getGeoHTML());
							value.append(ownerTRide.getScheduleHTML());
							value.append("</div></a>");
						}
						else
						{
							System.out.println("Owner transientRide " + rideId + " not found. Check DB integrity.");
						}
					}
					value.append("</div>");
					
					
				    value.append("<div class=\"panel panel-default\">");
					value.append("<div class=\"panel-heading\">"+dic.Participate+"</div>");
					value.append("<div class=\"panel-body\">");
					for(Iterator<CommutePartiRide> prideI = user.pRides.iterator();prideI.hasNext(); )
					{
						CommutePartiRide parRide = AllPartRides.getPartRides().get_participantRide(prideI.next()._pid);
						if (parRide.get_status()!=0)
						{
							
							value.append("<a href='/TicketSchedule/CommuteTopicCenter?topicId="+parRide.get_assoOwnerRideId()+"&partiId="+parRide._pid+"'>");
							value.append("<div class=\"ride_wrapper\">");	
							value.append(parRide._rideInfo.getGeoHTML());
							value.append(parRide._rideInfo.getScheduleHTML());
							value.append("</a><a href='/TicketSchedule/Zh/ManageRide.jsp?rid="+parRide._pid+"'><div><button class='btn-primary' type='submit'>±à¼­</button></div></a>");
							value.append("</div></a>");
							CommuteTopic topic= AllTopics.getTopics().get_topic(parRide.get_assoOwnerRideId());
						}
					}
					
					user.refreshpTride();
					for(Iterator<Integer> rideI = user.partiCommuteRide.iterator();rideI.hasNext(); )
					{
						int rideId = rideI.next();
						TransientRide ownerTRide = TransientRideAccess.getTransisentRideById(rideId);
						if (ownerTRide!=null)
						{
							value.append("<a href=\"/TicketSchedule/Zh/TransientTopic.jsp?trId="+ownerTRide.transientRideId+"\">");
							value.append("<div class=\"ride_wrapper\">");	
							value.append(ownerTRide.getGeoHTML());
							value.append(ownerTRide.getScheduleHTML());
							value.append("</div></a>");
						}
						else
						{
							System.out.println("Owner transientRide " + rideId + " not found. Check DB integrity.");
						}
					}
					value.append("</div>");
					
					
				    value.append("<div class=\"panel panel-default\">");
					value.append("<div class=\"panel-heading\">"+dic.FreeRide+"</div>");
					value.append("<div class=\"panel-body\">");
					for(Iterator<CommutePartiRide> prideI = user.pRides.iterator();prideI.hasNext(); )
					{
						CommutePartiRide parRide = AllPartRides.getPartRides().get_participantRide(prideI.next()._pid);
						if (parRide.get_status()==0)
						{
							
							value.append("<div class='ride_wrapper'>");	
							value.append("<a href='/TicketSchedule/Zh/SearchCommuteTopic.jsp?rid="+parRide._pid+"'>");
							value.append(parRide._rideInfo.getGeoHTML());
							value.append(parRide._rideInfo.getScheduleHTML());
							value.append("</a><a href='/TicketSchedule/Zh/ManageRide.jsp?rid="+parRide._pid+"'><div><button class='btn-primary' type='submit'>±à¼­</button></div></a>");
							value.append("<a href='/TicketSchedule/DeleteRide?rid="+parRide._pid+"'><div><button class='btn-primary' type='submit'>È¡Ïû</button></div></a>");
							value.append("</div>");
						}
					}
					value.append("</div>");
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write(value.toString());
				}
				
				
				if (content.equalsIgnoreCase("profile"))
				{
					UserProfile userP = UserAccess.loadUserProfile(user.get_uid());
					StringBuilder value = new StringBuilder();
				    value.append("<div class=\"panel panel-default\">");
					value.append("<div class=\"panel-heading\">"+dic.MyProfile+"</div>");
					value.append("<div class=\"panel-body\">");
					value.append("<form action=\"/TicketSchedule/servlet/UserProfile\" accept-charset=\"utf-8\" method=post onSubmit=\"return validateNewpwd()\">");
					value.append("<div class=propwrapper><span>"+dic.GivenName+"</span>"+"<input type=text class=\"userproperty\" name=givenname value='"+userP._givenname+"'></div>");
					value.append("<div class=propwrapper><span>"+dic.SureName+"</span>"+"<input type=text class=\"userproperty\" name=surename value='"+userP._surename+"'></div>");
					value.append("<div class=propwrapper><span>"+dic.Address+" </span>"+"<input type=text class=\"userproperty\" name=address value='"+userP._address+"'></div>");
					value.append("<div class=propwrapper><span>"+dic.OldPassword+"  </span>"+"<input type=password class=\"userproperty\" name=oldpwd id=oldpwd value=''></div>");
					value.append("<div class=propwrapper><span>"+dic.NewPassword+"  </span>"+"<input type=password class=\"userproperty\" name=newpwd id=newpwd value=''></div>");
					value.append("<div class=propwrapper><span>"+dic.ConfirmPassword+"  </span>"+"<input type=password class=\"userproperty\" name=newpwd2 id=newpwd2 value=''></div>");
					value.append("<input class=\"hidden\" name=uid value='"+userP.get_uid()+"'>");
					value.append("<button type=submit class=\"btn btn-primary\">"+dic.Update+"</button>");
					value.append("</form>");
					value.append("</div></div></div>");
			   

					user.numofnewMessage = 0;
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write(value.toString());
				}
			}
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
