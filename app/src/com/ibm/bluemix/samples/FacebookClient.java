/*-------------------------------------------------------------------*/
/* Copyright IBM Corp. 2013 All Rights Reserved                      */
/*-------------------------------------------------------------------*/
package com.ibm.bluemix.samples;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FacebookClient {

	public List<String> search(String search) throws Exception {
		HttpClient client = new DefaultHttpClient();
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme("https");
		uriBuilder.setHost("graph.facebook.com");
		uriBuilder.setPath("/search");
		uriBuilder.addParameter("q", search);
		uriBuilder.addParameter("limit", "25");
		uriBuilder.addParameter("fields", "message");
		uriBuilder.addParameter("access_token", getAccessToken());
		
		try {
			HttpGet request = new HttpGet(uriBuilder.build());
			request.setHeader("Content-Type", "application/json");
			HttpResponse response = client.execute(request);
			
			String responseText = EntityUtils.toString(response.getEntity());
			JSONObject result = (JSONObject) new JSONParser().parse(responseText);
			JSONArray data = (JSONArray) result.get("data");
			List<String> posts = new ArrayList<String>(data.size());
			
			for (int i = 0; i < data.size(); i++) {
				JSONObject post = (JSONObject) data.get(i);
				posts.add((String) post.get("message"));
			}
			
			return posts;
		} catch (URISyntaxException e) {
			throw new Exception("Problem generating URI for Facebook API.", e);
		} catch (ParseException e) {
			throw new Exception("Problem parsing response from Facebook.", e);
		}
	}
	
	private String getAccessToken() throws Exception {
    	Properties prop = new Properties();
    	 
    	try {
    		prop.load(getClass().getClassLoader().getResourceAsStream("facebook.properties"));
    		String id = prop.getProperty("appId");
    		String secret = prop.getProperty("appSecret");
    		
    		if (id == null || id.length() == 0 || secret == null || secret.length() == 0) {
    			System.out.println("Access token is null.");
    			throw new Exception("Access token is null. Be sure to enter your App ID and App Secret.");
    		}
    		
    		return id + "|" + secret;
    	} catch (IOException e) {
    		throw new Exception("Problem loading access token from facebook.properties.", e);
        }
	}
}
