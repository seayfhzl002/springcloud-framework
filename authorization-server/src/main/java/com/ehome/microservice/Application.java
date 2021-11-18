package com.ehome.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableAuthorizationServer
/*@EnableResourceServer*/
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
