package com.pig4cloud.pigx.common.security.util;


import cn.hutool.core.util.StrUtil;
/*import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.exception.BizException;
import com.pig4cloud.pigx.common.core.exception.CheckedException;
import com.pig4cloud.pigx.common.core.exception.ErrorCode;*/
import com.pig4cloud.pigx.common.security.service.PigxUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {
	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 *
	 * @param authentication
	 * @return PigxUser
	 * <p>
	 */
	/*public PigxUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof PigxUser) {
			return (PigxUser) principal;
		}
		throw new BizException(ErrorCode.NO_LOGIN);
	}*/

	/**
	 * 获取用户
	 */
	/*public PigxUser getUser() {
		Authentication authentication = getAuthentication();
		if(authentication == null){
			return null;
		}
		return getUser(authentication);
	}*/

	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	/*public List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();
		authorities.stream()
				.filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
					roleIds.add(Integer.parseInt(id));
				});
		return roleIds;
	}*/

}
