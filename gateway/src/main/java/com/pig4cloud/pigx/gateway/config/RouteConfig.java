package com.pig4cloud.pigx.gateway.config;

import com.pig4cloud.pigx.gateway.vo.RouteDefinitionVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2018/7/22
 * 网关不校验终端配置
 */
@Slf4j
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "route")
public class RouteConfig {
	List<RouteDefinitionVo> list = new ArrayList<>();
	@PostConstruct
	public void init(){
		log.info("init");
	}
}
