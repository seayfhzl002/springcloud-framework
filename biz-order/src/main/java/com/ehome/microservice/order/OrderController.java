package com.ehome.microservice.order;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private Logger logger = LogManager.getLogger();
	
	@RequestMapping(value="/{orderId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> queryOrder(@PathVariable("orderId")String orderId){
		String url = "http://localhost:8085/uer/users/user-names";
		/*restTemplate.exchange(url, 
				HttpMethod.GET, 
				null,
				String.class);*/
		Map<String, Object> items = new HashMap<>();
		items.put("url", "/orders/");
		return items;
	}
	
	public Map<String, Object> saveOrderInQueue(@PathVariable("orderId") Long orderId){
		
		logger.debug("save order in queue");
		Map<String, Object> ret = new HashMap<>();
		ret.put("result", "save order in queue");
		return ret;
	}
}
