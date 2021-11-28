

package com.pig4cloud.pigx.gateway;


//import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author
 * @date 2018年06月21日
 * 网关应用
 */
//@EnablePigxResourceServer
@EnableFeignClients("com.ehome.fintec.p2plending.common.api")
@SpringCloudApplication
public class PigxGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxGatewayApplication.class, args);
	}
}
