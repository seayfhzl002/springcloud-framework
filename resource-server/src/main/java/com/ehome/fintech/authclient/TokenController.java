package com.ehome.fintech.authclient;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value="/tokens")
public class TokenController {
	/**
	 * 这个接口用于在 resource server 独立部署时 被gateway 调用进行接口请求的token 的验证
	 * @param token  接口所携带的token
	 */
	@RequestMapping(value="/check-token", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public void tokenCheck(@RequestHeader("Authorization") String token){
		log.info("token {}", token);
	}
}
