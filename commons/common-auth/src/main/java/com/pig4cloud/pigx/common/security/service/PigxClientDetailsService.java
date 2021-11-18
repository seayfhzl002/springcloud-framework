package com.pig4cloud.pigx.common.security.service;
//package com.pig4cloud.pigx.common.security.service;
//
//import com.alibaba.fastjson.JSON;
//import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//
//import javax.sql.DataSource;
//
///**
// * @author
// * @date 2018/10/22
// * <p>
// * see JdbcClientDetailsService
// */
//public class PigxClientDetailsService extends JdbcClientDetailsService {
//
//	public PigxClientDetailsService(DataSource dataSource) {
//		super(dataSource);
//	}
//
//	/**
//	 * 重写原生方法支持redis缓存
//	 *
//	 * @param clientId
//	 * @return
//	 * @throws InvalidClientException
//	 */
//	@Override
////	@Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
//	public ClientDetails loadClientByClientId(String clientId) {
//		ClientDetails clientDetails = super.loadClientByClientId(clientId);
//		System.out.println(JSON.toJSONString(clientDetails));
//		return clientDetails;
//	}
//}
