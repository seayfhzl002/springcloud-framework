

package com.ehome.fintec.p2plending.common.api;

//import com.pig4cloud.pigx.common.core.util.R;

//import com.ehome.fintec.p2plending.common.dto.UserInfoDTO;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author
 * @date 2018/6/22
 */
@FeignClient(contextId = "auth-client", value = ServiceNameConstants.RESOURCE_SERVER_SERVICE)
public interface RemoteAuthClientService {
	/**
	 * 向authClient 请求当前请求的token 是否有效
	 *
	 * @param token 用户名
	 * @return R
	 */
	@RequestMapping(value="/tokens/check-token", method= RequestMethod.GET)
	public void tokenCheck(@RequestHeader("Authorization") String token);

}
