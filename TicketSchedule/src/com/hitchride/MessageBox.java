package com.hitchride;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitchride.access.MessageTbAccess;
import com.hitchride.standardClass.MessageInfo;

/**
 * Servlet implementation class MessageBox
 */
public class MessageBox extends HttpServlet {
	private static final long serialVersionUID = 1L;
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageBox() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean islogin = (request.getSession().getAttribute("IsLogin")!=null)? true:false;
		if (!islogin)
		{
			request.getSession().setAttribute("fromLocation", "/TicketSchedule/servlet/MessageBox");
			request.getSession().setAttribute("queryString", request.getQueryString());
			request.getSession().setMaxInactiveInterval(60*30);
			response.sendRedirect("/TicketSchedule/Login.jsp");
		}
		else
		{
			String qs = request.getQueryString();
			String[] pars = qs.split("&");
			String id = pars[0].split("=")[1];
			String rid = pars[1].split("=")[1];
			String type = pars[2].split("=")[1];
			request.setAttribute("to", id);
			request.setAttribute("rid",rid );
		    MessageInfo messageInfo = new MessageInfo(rid);
		    request.setAttribute("messageInfo",messageInfo);

			RequestDispatcher rd = request.getRequestDispatcher("/MessageBox.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		String message = request.getParameter("notes");
		int recordID = Integer.parseInt(request.getParameter("recordID"));
		MessageTbAccess messageTb = new MessageTbAccess();
		messageTb.insertMessage(from, to, message,recordID);
		response.sendRedirect("/TicketSchedule/servlet/Search");
	}

}
