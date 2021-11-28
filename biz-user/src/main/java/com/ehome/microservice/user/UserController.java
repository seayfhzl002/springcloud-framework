package com.ehome.microservice.user;

import java.util.HashMap;
import java.util.Map;

import com.ehome.fintec.p2plending.common.dto.UserInfoDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users")
public class UserController {

	@RequestMapping(value="/auth/userName/{userName}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
//	@PreAuthorize("hasAuthority('ROLE_USER')")
	public UserInfoDTO authByUserName(@PathVariable("userName") String userName){
		UserInfoDTO userInfo = UserInfoDTO.builder().userId(1000L).userName("testuser").build();
		return userInfo;
	}
}
