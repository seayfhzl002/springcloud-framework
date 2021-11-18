

package com.pig4cloud.pigx.gateway.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteIPlimitService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteIPLimitService {
	/**
	 * 验证登录Ip是否在黑名单中
	 *
	 * @param remoteIP 需要验证的Ip
	 * @return Boolean 有效IP返回true，否则返回 false
	 */
	@GetMapping("/ipLimit/isValidIP")
	Boolean isValidIP(@RequestHeader(SecurityConstants.FROM) String from,@RequestParam("rangeType") Integer rangeType, @RequestParam("remoteIP") String remoteIP);
}
