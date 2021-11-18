package com.ehome.microservice.auth.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

//import com.ehome.microservice.common.auth.Constants;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	@Resource(name="ssoUserDetailsService")
	private UserDetailsService userDetailsService;
	/*@Resource
	private RedisConnectionFactory redisConnFactory;*/
	@Resource(name="nacosClientDetailsService")
	private ClientDetailsService clientDetailsService;
	@Resource
	private AuthenticationManager authManager;
	@Resource
	private PasswordEncoder passwordEncoder;
	
    @SuppressWarnings("deprecation")
	@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//    	clients.withClientDetails(clientDetailsService);
    	clients.inMemory()
    	.withClient("user")
    	.secret(passwordEncoder.encode("123456"))
    	.redirectUris("http://localhost:5000/auth/logincallback")
    	.authorizedGrantTypes("authorization_code", "refresh_token", "client_credentials", "password")
    	.autoApprove(false)
    	.scopes("read,write")
    	.and()
    	.withClient("client-check-token")
    	.secret(passwordEncoder.encode("123456"))
    	.redirectUris("http://localhost:5000/auth/logincallback")
    	.authorizedGrantTypes("authorization_code", "refresh_token", "client_credentials", "password")
    	.autoApprove(false)
    	.scopes("read,write");
    }

    @SuppressWarnings("deprecation")
	@Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        //endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
        DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
//        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
//        tokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setAuthenticationManager(authManager);
        // 一天有效期
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1));
        endpoints.tokenServices(tokenServices)
        			.userDetailsService(userDetailsService)
        			.authenticationManager(authManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
    	security.tokenKeyAccess("permitAll()")
    			.checkTokenAccess("isAuthenticated()");
        //used to disable http basic authentication
//        security.allowFormAuthenticationForClients();
    }

    /*@Bean
    public TokenStore redisTokenStore() {
    	RedisTokenStore tokenStroe = new RedisTokenStore(redisConnFactory);
    	tokenStroe.setPrefix(Constants.PREFIX_TOKEN);
    	return tokenStroe;
    }*/
    
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("testKey");
        return converter;
    }
    
}