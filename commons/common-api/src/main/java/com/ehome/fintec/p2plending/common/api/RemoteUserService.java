

package com.ehome.fintec.p2plending.common.api;

//import com.pig4cloud.pigx.common.core.util.R;
import com.ehome.fintec.p2plending.common.dto.UserInfoDTO;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.USER_SERVICE)
public interface RemoteUserService {
	/**
	 * 根据用户名模糊查询
	 *
	 * @param username 用户名
	 * @return R
	 */
	@GetMapping("/users/auth/username/{username}")
	UserInfoDTO getByUserName(@PathVariable("username") String username);

}
