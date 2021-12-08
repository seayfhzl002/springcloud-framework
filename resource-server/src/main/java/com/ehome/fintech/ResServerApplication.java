package com.ehome.fintech;

import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
//import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

//@EnablePigxSwagger2
@EnablePigxResourceServer
@SpringCloudApplication
public class ResServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResServerApplication.class, args);
    }
}
