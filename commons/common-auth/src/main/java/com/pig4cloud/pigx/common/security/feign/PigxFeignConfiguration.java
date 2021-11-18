package com.pig4cloud.pigx.common.security.feign;
//
//package com.pig4cloud.pigx.common.security.feign;
//
//import feign.RequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * fegin 配置增强
// *
// * @author L.cm
// */
//@Configuration
////@ConditionalOnClass(Feign.class)
//public class PigxFeignConfiguration {
//
////	@Bean
////	@ConditionalOnProperty("security.oauth2.client.client-id")
////	public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
////															OAuth2ProtectedResourceDetails resource,
////															AccessTokenContextRelay accessTokenContextRelay) {
////		return new PigxFeignClientInterceptor(oAuth2ClientContext, resource, accessTokenContextRelay);
////	}
//
//	@Bean
//	public RequestInterceptor pigxFeignTenantInterceptor() {
//		return new PigxFeignTenantInterceptor();
//	}
//
//}
