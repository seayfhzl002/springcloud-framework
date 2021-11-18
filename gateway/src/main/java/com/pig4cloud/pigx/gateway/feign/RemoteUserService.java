

package com.pig4cloud.pigx.gateway.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * @author
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteUserService {

	/**
	 * 通过用户名查询用户、角色信息
	 *
	 * @param username 用户名
	 * @return R
	 */
	@GetMapping("/user/getGoogleSecret/{username}")
	R<String> getGoogleSecret(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM) String from);
	@GetMapping("/user/queryUsername")
    String queryUsername(@RequestHeader(SecurityConstants.FROM) String from, @RequestHeader("Authorization") String auth);
}
