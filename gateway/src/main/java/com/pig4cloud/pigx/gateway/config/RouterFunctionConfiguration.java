package com.pig4cloud.pigx.gateway.config;

import com.pig4cloud.pigx.gateway.handler.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @author
 * @date 2018/7/5
 * 路由配置信息
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
	private final SwaggerResourceHandler swaggerResourceHandler;
	private final SwaggerSecurityHandler swaggerSecurityHandler;
	private final SwaggerUiHandler swaggerUiHandler;
	private final ImageCodeHandler imageCodeHandler;
	private final SmsCodeHandler smsCodeHandler;


	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions.route(RequestPredicates.path("/code")
						.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeHandler)
//				.andRoute(RequestPredicates.path("/smsCode")
//						.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), smsCodeHandler)
				.andRoute(RequestPredicates.GET("/swagger-resources")
						.and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
				.andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
						.and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)
				.andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
						.and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler);

	}

}
