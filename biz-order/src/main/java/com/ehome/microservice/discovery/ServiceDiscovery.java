//package com.ehome.microservice.discovery;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.netflix.appinfo.InstanceInfo;
//import com.netflix.discovery.EurekaClient;
//
//@Component
//public class ServiceDiscovery implements CommandLineRunner {
//
//    @Autowired
//    private EurekaClient discoveryClient;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("通过实现CommandLineRunner接口，在spring boot项目启动后打印参数");
//        //String servieUrl = serviceUrl();
//        //System.out.println(servieUrl);
//    }
//
//    public String serviceUrl() {
//        InstanceInfo instance = discoveryClient.getNextServerFromEureka("eureka-client-01", false);
//        return instance.getHomePageUrl();
//    }
//}
