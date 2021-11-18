package com.ehome.microservice.auth.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.ehome.microservice.auth.config.resource.AppFilterInvocationSecurityMetadataSource;
import com.ehome.microservice.auth.config.resource.RoleBasedVoter;

//@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    /*@Autowired
    @Qualifier("SSOUserDetailsService")
    private UserDetailsService userDetailsService;

    

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }*/

	/**
	 * there are two ways to authorize URL
	 * 1,customizing FilterInvocationSecurityMetadataSource
	 * 2,customizing AccessDecisionVoter
	 * please prefer to following code to configure the authorize URL
	 * 
	 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	/*.formLogin()
	        .loginPage("/login")
	        .failureHandler(null)
            .and()*/
            .authorizeRequests()
            /*.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(
                    O fsi) {
                    fsi.setSecurityMetadataSource(mySecurityMetadataSource(fsi.getSecurityMetadataSource()));
                    return fsi;
                }
            })*/
            .antMatchers("/oauth/**").permitAll()
            
            /*.accessDecisionManager(accessDecisionManager())*/
            .and()
            .csrf().disable()
            .formLogin().permitAll()
            
            ;
    }

    
    
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationManagerBean());
    }
    */
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	return super.authenticationManager();
    	
    }
    
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
            = Arrays.asList(
            new WebExpressionVoter(),
            // new RoleVoter(),
            new RoleBasedVoter(),
            new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }
    
    @Bean
    public AppFilterInvocationSecurityMetadataSource mySecurityMetadataSource(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        AppFilterInvocationSecurityMetadataSource securityMetadataSource = new AppFilterInvocationSecurityMetadataSource(filterInvocationSecurityMetadataSource);
        return securityMetadataSource;
    }
}