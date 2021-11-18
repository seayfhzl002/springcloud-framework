package com.pig4cloud.pigx.gateway.config;

//import com.pig4cloud.pigx.common.gateway.vo.RouteDefinitionVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author
 * @date 2018/7/22
 * 网关不校验终端配置
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "route")
public class RouteConfig {
//	List<RouteDefinitionVo> list;
}
