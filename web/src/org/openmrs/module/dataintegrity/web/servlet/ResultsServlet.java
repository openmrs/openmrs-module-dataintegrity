package org.openmrs.module.dataintegrity.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.APIException;
import org.openmrs.web.WebConstants;

public class ResultsServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.write("Not valid");
		
	}
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		    HttpSession session = request.getSession();
		    session.setAttribute("name", "Nimantha");
		    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "baduma thamai");
			response.sendRedirect("../../module/dataintegrity/results.list");
		}
		catch (APIException apiexception) {
		}	
	}


}
