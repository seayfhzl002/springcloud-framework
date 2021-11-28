package com.pig4cloud.pigx.common.security.service;

/*import com.ehome.fintec.p2plending.common.api.RemoteUserService;
import com.ehome.fintec.p2plending.common.dto.UserInfoDTO;*/
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * 用户详细信息
 *
 * @author
 */
@Slf4j
@AllArgsConstructor
public class PigxUserDetailsServiceImpl implements PigxUserDetailsService {
//	private final RemoteUserService remoteUserService;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {
		/*UserInfoDTO userInfo = remoteUserService.getByUserName(username);
		if(userInfo == null){
			return null;
		}*/
//		PigxUser pigxUser = PigxUser.builder().build();
		PigxUser user = new PigxUser(0L, username, "N_A", true
				, true, true, true, null, 0L, 0L,0L,0L/*,tenantId*/);
		return user;
	}


	/**
	 * 根据社交登录code 登录
	 *
	 * @param inStr TYPE@CODE
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserBySocial(String inStr) {
//		return getUserDetails(remoteUserService.social(inStr, SecurityConstants.FROM_IN));
		return null;
	}

	/**
	 * 构建userdetails
	 *
	 * @param result 用户信息
	 * @return
	 */
//	private UserDetails getUserDetails(R<?> result) {
		/*if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		UserInfo info = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}
		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();
//		boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);
		// 构造security用户

		return new PigxUser(user.getUserId(), user.getUsername(), SecurityConstants.BCRYPT + user.getPassword(), true,
				true, true, !user.getLockFlag().booleanValue(), authorities,
				Optional.ofNullable(info.getZxUser()).map(ZxUser::getPrizeTeamId).orElse(0L),
				Optional.ofNullable(info.getZxUser()).map(ZxUser::getPrizeTeamReturnPoint).orElse(0L),
				Optional.ofNullable(info.getZxUser()).map(ZxUser::getPrizeTeamRateDiscount).orElse(0),
				Optional.ofNullable(info.getZxUser()).map(ZxUser::getLevelId).orElse(0L),
				Optional.ofNullable(TenantContextHolder.getTenantId()).orElse(""));*/
		/*return null;
	}*/
}
