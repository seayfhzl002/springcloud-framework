package com.pig4cloud.pigx.common.security.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.web.client.RestTemplate;

import com.ehome.microservice.auth.config.resource.AppFilterInvocationSecurityMetadataSource;
import com.ehome.microservice.auth.config.resource.RoleBasedVoter;

/**
 * @author
 * @date 2018/6/22
 * <p>
 * 1. 支持remoteTokenServices 负载均衡
 * 2. 支持 获取用户全部信息
 */
@Slf4j
public class PigxResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
	@Autowired
	protected ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;
	@Autowired
	protected RemoteTokenServices remoteTokenServices;
	@Autowired
	private PermitAllUrlProperties permitAllUrlProperties;
	@Autowired
	private RestTemplate lbRestTemplate;	

	/**
	 * 配置权限检查有2种方式:
	 * 1,通过 accessDecisionManager
	 * 2,通过 withObjectPostProcessor
	 *
	 * @param http
	 */
	@Override
	@SneakyThrows
	public void configure(HttpSecurity http) {
		
		//允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
		http.headers().frameOptions().disable();
		/*ExpressionUrlAuthorizationConfigurer<HttpSecurity>
			.ExpressionInterceptUrlRegistry registry = http
			.authorizeRequests();
		permitAllUrlProperties.getIgnoreUrls()
			.forEach(url -> registry.antMatchers(url).permitAll());
		registry.anyRequest().authenticated()
			.and().csrf().disable();*/
		http.authorizeRequests()
		.antMatchers("/orders/**").hasRole("MANAGER")
		.accessDecisionManager(accessDecisionManager())
		
		/*.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
        @Override
        public <O extends FilterSecurityInterceptor> O postProcess(
            O fsi) {
            fsi.setSecurityMetadataSource(mySecurityMetadataSource(fsi.getSecurityMetadataSource()));
            return fsi;
        }
    	})*/
		
		;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		UserAuthenticationConverter userTokenConverter = new PigxUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);

		remoteTokenServices.setRestTemplate(lbRestTemplate);
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
				.tokenServices(remoteTokenServices);
		resources.expressionHandler(oauthExpressionHandler());
	}
	
	@Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
            = Arrays.asList(
            new WebExpressionVoter(),
            new RoleVoter(),
            new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }
    
    @Bean
    public AppFilterInvocationSecurityMetadataSource mySecurityMetadataSource(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        AppFilterInvocationSecurityMetadataSource securityMetadataSource = new AppFilterInvocationSecurityMetadataSource(filterInvocationSecurityMetadataSource);
        return securityMetadataSource;
    }
    
    @Bean
    public DefaultWebSecurityExpressionHandler oauthExpressionHandler() {
    	DefaultWebSecurityExpressionHandler securityMetadataSource = new DefaultWebSecurityExpressionHandler();
        return securityMetadataSource;
    }
}
