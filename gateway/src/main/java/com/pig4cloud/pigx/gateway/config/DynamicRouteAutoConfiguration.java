package com.pig4cloud.pigx.gateway.config;

import com.pig4cloud.pigx.common.core.constant.CacheConstants;
/*import com.pig4cloud.pigx.common.gateway.rule.AbstractDiscoveryEnabledRule;
import com.pig4cloud.pigx.common.gateway.rule.MetadataAwareRule;*/
import com.pig4cloud.pigx.gateway.filter.GrayLoadBalancerClientFilter;
//import com.pig4cloud.pigx.gateway.support.RouteCacheHolder;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.time.Duration;

@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class DynamicRouteAutoConfiguration {
	/**
	 * 配置文件设置为空
	 * redis 加载为准
	 *
	 * @return
	 */
	@Bean
	public PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator() {
		return new PropertiesRouteDefinitionLocator(new GatewayProperties());
	}

	/**
	 * redis 监听配置
	 *
	 * @param redisConnectionFactory redis 配置
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container
				= new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener((message, bytes) -> {
			log.warn("接收到重新JVM 重新加载路由事件");
//			RouteCacheHolder.removeRouteList();
		}, new ChannelTopic(CacheConstants.ROUTE_JVM_RELOAD_TOPIC));
		return container;
	}


	@Bean
	@ConditionalOnProperty(value = "spring.redis.cluster.enable",havingValue = "true")
	public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());

		// https://github.com/lettuce-io/lettuce-core/wiki/Redis-Cluster#user-content-refreshing-the-cluster-topology-view
		ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
				.enablePeriodicRefresh()
				.enableAllAdaptiveRefreshTriggers()
				.refreshPeriod(Duration.ofSeconds(5))
				.build();

		ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
				.topologyRefreshOptions(clusterTopologyRefreshOptions).build();

		// https://github.com/lettuce-io/lettuce-core/wiki/ReadFrom-Settings
		LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
				.readFrom(ReadFrom.SLAVE_PREFERRED)
				.clientOptions(clusterClientOptions).build();

		return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
	}


	@Bean
	@ConditionalOnMissingBean(LoadBalancerClient.class)
	public LoadBalancerClient loadBalancerClient(SpringClientFactory springClientFactory) {
		return new RibbonLoadBalancerClient(springClientFactory);
	}

	@Bean
	@ConditionalOnMissingBean(LoadBalancerClientFilter.class)
	public LoadBalancerClientFilter loadBalancerClientFilter(RibbonLoadBalancerClient loadBalancerClient
			, LoadBalancerProperties properties) {
		return new GrayLoadBalancerClientFilter(loadBalancerClient, properties);
	}
}
