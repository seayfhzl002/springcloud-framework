package com.ehome.test;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletTestCase;

public class ControllerJunitBase extends ServletTestCase {	
	private static Logger logger = LogManager.getLogger(ControllerJunitBase.class.getName());
	
	public ControllerJunitBase(String name) {
		super(name);
	}
	
	
	protected String queryToken(String userName, String pwd, String clientId) {
		String token = null;
		//String server = "http://miss369.com/lottery-api";
		//String server = "http://localhost:8080/";
		String server = "http://localhost/lottery-api";
		String tokenURL = server + "/oauth/token";
		String sessionId = querySessionId(server);
		
		String captcha = null;
		ObjectMapper mapper = new ObjectMapper();
		
		/*if(StringUtils.isBlank(sessionId)) {
			return null;
		}*/
		
		tokenURL += ";jsessionid=" + sessionId;
		captcha = queryCaptcha(server, sessionId);
		try {
			WebRequest request = new PostMethodWebRequest(tokenURL);
			WebConversation wc = new WebConversation();
			
			request.setParameter("grant_type", "password");
			request.setParameter("client_id", clientId);
			request.setParameter("client_secret", "secret_1");
			request.setParameter("username", userName);
			request.setParameter("password", pwd);
			request.setParameter("captcha", captcha);
			//request.setParameter("jsessionid", sessionId);
			WebResponse response = wc.sendRequest(request);
			
			int  status = response.getResponseCode();
			
			Assert.assertEquals(HttpServletResponse.SC_OK, status);
			String result = response.getText();
			
			Map<String, Object> retItems = null;
			
			retItems = mapper.readValue(result, HashMap.class);
			
			Assert.assertNotNull(retItems);

			Assert.assertNotNull(retItems.get("access_token"));
			
			token = (String)retItems.get("access_token");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		return token;
	}
	
	protected String queryRefreshToken(String userName, String pwd, String clientId) {
		String token = null;
		//String server = "http://110.92.64.70/lottery-api";
		String server = "http://localhost:8080/";
		String tokenURL = server + "/oauth/token";
		String sessionId = querySessionId(server);
		
		String captcha = null;
		ObjectMapper mapper = new ObjectMapper();
		
		/*if(StringUtils.isBlank(sessionId)) {
			return null;
		}*/
		
		tokenURL += ";jsessionid=" + sessionId;
		captcha = queryCaptcha(server, sessionId);
		try {
			WebRequest request = new PostMethodWebRequest(tokenURL);
			WebConversation wc = new WebConversation();
			
			request.setParameter("grant_type", "password");
			request.setParameter("client_id", clientId);
			request.setParameter("client_secret", "secret_1");
			request.setParameter("username", userName);
			request.setParameter("password", pwd);
			request.setParameter("captcha", captcha);
			//request.setParameter("jsessionid", sessionId);
			WebResponse response = wc.sendRequest(request);
			
			int  status = response.getResponseCode();
			
			Assert.assertEquals(HttpServletResponse.SC_OK, status);
			String result = response.getText();
			
			Map<String, Object> retItems = null;
			
			retItems = mapper.readValue(result, HashMap.class);
			
			Assert.assertNotNull(retItems);

			Assert.assertNotNull(retItems.get("refresh_token"));
			
			token = (String)retItems.get("refresh_token");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		return token;
	}
	
	
	
	private String querySessionId(String server) {
		String sessionId = null;
		String sessionIdURL = server + "/captchas/query-sesionid";
		ObjectMapper mapper = new ObjectMapper();
		try {
			WebRequest request = new GetMethodWebRequest(sessionIdURL);
			WebConversation wc = new WebConversation();
			WebResponse response = wc.sendRequest(request);
			
			int  status = response.getResponseCode();
			
			Assert.assertEquals(HttpServletResponse.SC_OK, status);
			String result = response.getText();
			
			
			Map<String, Object> retItems = null;
			
			retItems = mapper.readValue(result, HashMap.class);
			
			Assert.assertNotNull(retItems);

			Assert.assertNotNull(retItems.get("data"));
			
			sessionId = (String)((Map)retItems.get("data")).get("sessionId");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		return sessionId;
	}
	
	private void queryCaptchaImage(String server , String sessionId) {		
		//ObjectMapper mapper = new ObjectMapper();
		try {
			WebRequest request = new GetMethodWebRequest(server + "/captchas/verification-code-Img;jsessionid=" + sessionId);
			WebConversation wc = new WebConversation();
			WebResponse response = wc.sendRequest(request);
			
			int  status = response.getResponseCode();
			
			Assert.assertEquals(HttpServletResponse.SC_OK, status);
			/*String result = response.getText();
			
			Map<String, Object> retItems = null;
			
			retItems = mapper.readValue(result, HashMap.class);
			
			Assert.assertNotNull(retItems);

			Assert.assertNotNull(retItems.get("data"));
			
			sessionId = (String)retItems.get("data");*/
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	private String queryCaptcha(String server, String sessionId) {
		String captcha = null;
		//String sessionId = null;
		
		
		//sessionId = querySessionId();
		/*if(StringUtils.isBlank(sessionId)) {
			fail("Can not obtain the session id from the server!!!");
			return null;
		}*/
		
		queryCaptchaImage(server, sessionId);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			WebRequest request = new GetMethodWebRequest(server + "/captchas;jsessionid=" + sessionId);
			WebConversation wc = new WebConversation();
			WebResponse response = wc.sendRequest(request);
			
			int  status = response.getResponseCode();
			
			Assert.assertEquals(HttpServletResponse.SC_OK, status);
			String result = response.getText();
			
			Map<String, Object> retItems = null;
			
			retItems = mapper.readValue(result, HashMap.class);
			
			Assert.assertNotNull(retItems);

			Assert.assertNotNull(retItems.get("data"));
			
			captcha = (String)((Map)retItems.get("data")).get("captcha");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		return captcha;
	}
}