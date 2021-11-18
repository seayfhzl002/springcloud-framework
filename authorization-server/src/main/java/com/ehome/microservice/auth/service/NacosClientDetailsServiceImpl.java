package com.ehome.microservice.auth.service;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import com.ehome.microservice.auth.config.ClientConfiguration;

@Service("nacosClientDetailsService")
public class NacosClientDetailsServiceImpl implements ClientDetailsService {
	@Resource
	private ClientConfiguration clientCongig;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		/*ClientDetails clientDetails = clientCongig.getClients().stream().filter(client->StringUtils.equals(client.getClientId(), clientId)).findFirst().orElse(null);
		return clientDetails;*/
		return null;
	}

}
