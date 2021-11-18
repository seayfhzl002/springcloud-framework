package com.pig4cloud.pigx.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author
 * @date 2018/7/22
 * 短信宝配置
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
	String url;
	Map<String,String> contents;
	String userName;
	String passWord;
}
