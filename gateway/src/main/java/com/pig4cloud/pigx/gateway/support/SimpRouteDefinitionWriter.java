/*package com.pig4cloud.pigx.gateway.support;

import com.pig4cloud.pigx.gateway.config.RouteConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

*//**
 * @author
 * @date 2018/10/31
 * <p>
 * redis 保存路由信息，优先级比配置文件高
 *//*
@Slf4j
@Component
@AllArgsConstructor
public class SimpRouteDefinitionWriter implements RouteDefinitionRepository {

	private final RouteConfig routeConfig;


	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return Flux.fromIterable(routeConfig.getList());
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return null;
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return null;
	}
}
*/