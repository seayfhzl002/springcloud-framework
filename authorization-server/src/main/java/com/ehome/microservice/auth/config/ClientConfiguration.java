package com.ehome.microservice.auth.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import lombok.Data;

@Data
@Configuration
//@RefreshScope
@ConfigurationProperties(prefix="security")
public class ClientConfiguration {
	List<BaseClientDetails> clients;
}
