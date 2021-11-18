package com.ehome.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.netflix.appinfo.InstanceInfo;
//import com.netflix.discovery.EurekaClient;

@SpringBootApplication
@RestController
public class Application {

//	@Autowired
//    private EurekaClient discoveryClient;
	
    @RequestMapping("/")
    public String home() {
    	String servieUrl = serviceUrl();
        return servieUrl;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public String serviceUrl() {
//        InstanceInfo instance = discoveryClient.getNextServerFromEureka("eureka-client-01", false);
//        return instance.getHomePageUrl();
    	return null;
    }
}
