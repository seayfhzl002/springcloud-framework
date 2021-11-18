package com.pig4cloud.pigx.common.security.service;

import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;

import java.util.List;

/**
 * @author
 * @date 2018/10/22
 * <p>
 * see JdbcClientDetailsService
 */
public class PigxNacosClientDetailsService extends InMemoryClientDetailsService {

	private List<BaseClientDetails> clients;

	public PigxNacosClientDetailsService(List<BaseClientDetails> clients) {
		this.clients = clients;
	}

	/**
	 * 重写原生方法支持redis缓存
	 *
	 * @param clientId
	 * @return
	 * @throws InvalidClientException
	 */
	@Override
	public ClientDetails loadClientByClientId(String clientId) {
		BaseClientDetails baseClientDetails = clients.stream().filter(c -> c.getClientId().equals(clientId)).findFirst().orElse(null);
		return baseClientDetails;
	}
}
