package com.ehome.fintech.user.auth.service;

import javax.annotation.Resource;

import com.ehome.fintech.user.auth.config.ClientConfiguration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

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
