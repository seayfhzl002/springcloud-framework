package com.ehome.microservice.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users")
public class UserController {

	@RequestMapping(value="/user-names", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, Object> getUserName(){
		Map<String, Object> ret = new HashMap<>();
		ret.put("userName", "nick");
		return ret;
	}
}
