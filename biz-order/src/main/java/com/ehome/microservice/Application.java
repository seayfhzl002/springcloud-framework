package com.ehome.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;


@SpringBootApplication
@EnablePigxResourceServer
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
