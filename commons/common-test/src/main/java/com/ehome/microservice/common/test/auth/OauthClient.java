package com.ehome.microservice.common.test.auth;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import cn.hutool.core.codec.Base64;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class OauthClient {
    @SneakyThrows
    public OauthTOkenEntity oauthGrantPassword(String host, String userName, String password, String clientId, String clientSecrect){
		String authorizeURL = host + "/auth/oauth/authorize?client_id=user&scope=read,write&redirect_uri="+host+"/auth/logincallback&response_type=code&grant_type=password";
		String tokenURL = host + "/auth/oauth/token?grant_type=authorization_code&redirect_uri="+host+"/auth/logincallback";		
		
		WebRequest request = new GetMethodWebRequest(authorizeURL);
		WebConversation wc = new WebConversation();

		WebResponse response = wc.sendRequest(request);
		
		int status = response.getResponseCode();
		Assert.assertEquals(HttpServletResponse.SC_OK, status);
		
		WebForm loginForm = response.getForms()[0];
		log.info("responsee {} ", response.getText());
		loginForm.setParameter("username", userName);
		loginForm.setParameter("password", password);
		response = loginForm.submit();
		
		if(response.getForms().length > 0) {
			WebForm approveForm = response.getForms()[0];
			approveForm.setParameter("scope.read,write", "true");
			response = approveForm.submit();			
		}
		
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
				OauthTOkenEntity oauthToken = JSON.parseObject(response.getText(), OauthTOkenEntity.class);
				
				log.info("token response {}", response.getText());
				
				return oauthToken;
			}
		}
		
		return null;
	}
}
