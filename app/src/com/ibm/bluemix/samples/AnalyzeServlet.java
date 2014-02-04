/*-------------------------------------------------------------------*/
/* Copyright IBM Corp. 2013 All Rights Reserved                      */
/*-------------------------------------------------------------------*/

package com.ibm.bluemix.samples;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class AnalyzeServlet extends HttpServlet {

	private static final long serialVersionUID = -991059984721715352L;
	private FacebookClient client = new FacebookClient();

    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String keyword = request.getParameter("keyword");
        String option = request.getParameter("option");
        
		try {
			List<String> texts = client.search(keyword);
			String results = TextAnalyzer.analyze(texts, option);
			
			response.setContentType("application/json");
			response.setStatus(200);
			response.getWriter().write(results);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			JSONObject error = new JSONObject();
			error.put("error", e.getMessage());
			
			response.setContentType("application/json");
			response.setStatus(200);
			response.getWriter().write(error.toJSONString());
		}
    }
}