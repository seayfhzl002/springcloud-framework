package com.ehome.microservice.order;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ehome.microservice.Application;
import com.ehome.microservice.common.test.auth.OauthClient;
import com.ehome.microservice.common.test.auth.OauthTOkenEntity;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes=Application.class)
public class OrderControllerTest extends OauthClient{
	
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueryOrder() throws Exception {
		String host = "http://localhost:5000";
		String userName = "user";
		String pwd = "123456";
		String clientId = "user";
		String clientSecrect = "123456";
		
		OauthTOkenEntity token = this.oauthGrantPassword(host, userName, pwd, clientId, clientSecrect);
		Assert.assertNotNull(token);
		String queryOrderURL = "http://localhost:8086/biz-order/orders/1";
		
		WebRequest request = new GetMethodWebRequest(queryOrderURL);
		request.setHeaderField("Authorization", "Bearer " + token.getAccessToken());
		WebConversation wc = new WebConversation();

		WebResponse response = wc.sendRequest(request);
		
		int status = response.getResponseCode();
		Assert.assertEquals(HttpServletResponse.SC_OK, status);
		log.info("response from biz-order {}", response.getText());
	}

}