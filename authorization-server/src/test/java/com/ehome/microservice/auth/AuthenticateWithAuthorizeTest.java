package com.ehome.microservice.auth;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.ehome.microservice.Application;
import com.ehome.microservice.common.test.auth.OauthClient;
import com.ehome.microservice.common.test.auth.OauthTOkenEntity;
import com.ehome.test.ControllerJunitBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpException;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes=Application.class)
public class AuthenticateWithAuthorizeTest extends OauthClient{

	@LocalServerPort
	private int port;
	
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAuthWithAuthorize() throws Exception {
		String authorizeURL = "http://localhost:5000/auth/oauth/authorize?client_id=user&scope=read,write&redirect_uri=http://localhost:5000/auth/logincallback&response_type=code&grant_type=password";
		String tokenURL = "http://localhost:5000/auth/oauth/token?grant_type=authorization_code&redirect_uri=http://localhost:5000/auth/logincallback";		
		
		String userName = "user";
		String pwd = "123456";
		String clientId = "user";
		String clientSecrect = "123456";
		/*ObjectMapper mapper = new ObjectMapper();
		ByteArrayInputStream bis = null;

		ObjectNode node = mapper.createObjectNode();
		node.putPOJO("username", userName);
		node.putPOJO("password", pwd);
		
		System.out.println(mapper.writeValueAsString(node));
		bis = new ByteArrayInputStream(mapper.writeValueAsBytes(node));*/
		WebRequest request = new GetMethodWebRequest(authorizeURL);
		WebConversation wc = new WebConversation();
//		request.setHeaderField("Authorization", "bearer " + token);

		WebResponse response = wc.sendRequest(request);
		
		int status = response.getResponseCode();
		Assert.assertEquals(HttpServletResponse.SC_OK, status);
		
		WebForm loginForm = response.getForms()[0];
		loginForm.setParameter("username", userName);
		loginForm.setParameter("password", pwd);
		response = loginForm.submit();
		
		
		WebForm approveForm = response.getForms()[0];
		approveForm.setParameter("scope.read,write", "true");
		response = approveForm.submit();
		
		//已经获取到code
		if(response.getResponseCode() == HttpServletResponse.SC_SEE_OTHER) {
			String location = response.getHeaderField("location");
			if(!StringUtils.isBlank(location)) {
				String code = location.substring(location.indexOf("code="));
				tokenURL = tokenURL + "&&" + code;
				
				String basicAuth = clientId + ":" + clientSecrect;
				basicAuth = "Basic " + Base64.encode(basicAuth, Charset.forName("UTF-8"));
				request = new PostMethodWebRequest(tokenURL);
				request.setHeaderField("Authorization", basicAuth);
				response = wc.sendRequest(request);
				status = response.getResponseCode();
				Assert.assertEquals(HttpServletResponse.SC_OK, status);
				
				log.info("token response {}", response.getText());
			}
		}
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testOAuthWithAuthorize() throws Exception {
		String host = "http://localhost:5000";
		String userName = "user";
		String pwd = "123456";
		String clientId = "user";
		String clientSecrect = "123456";
		
		OauthTOkenEntity token = this.oauthGrantPassword(host, userName, pwd, clientId, clientSecrect);
		Assert.assertNotNull(token);
	}
}