package com.pig4cloud.pigx.common.security.component;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2019-09-17
 * <p>
 * token增强，客户端模式不增强。
 */
@Slf4j
public class PigxTokenEnhancer implements TokenEnhancer {
	/**
	 * Provides an opportunity for customization of an access token (e.g. through its additional information map) during
	 * the process of creating a new token for use by a client.
	 *
	 * @param accessToken    the current access token with its expiration and refresh token
	 * @param authentication the current authentication including client and user details
	 * @return a new token enhanced with additional information
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (SecurityConstants.CLIENT_CREDENTIALS
				.equals(authentication.getOAuth2Request().getGrantType())) {
			return accessToken;
		}

		final Map<String, Object> additionalInfo = new HashMap<>();
		PigxUser pigxUser = (PigxUser) authentication.getUserAuthentication().getPrincipal();
		additionalInfo.put(SecurityConstants.DETAILS_USER_ID, pigxUser.getId());
		additionalInfo.put(SecurityConstants.DETAILS_USERNAME, pigxUser.getUsername());
		additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PIGX_LICENSE);
		additionalInfo.put(SecurityConstants.ACTIVE, Boolean.TRUE);
		// token 信息放入租户id 防止 使用同一token换租户使用
//		additionalInfo.put(SecurityConstants.DETAILS_TENANT_ID, TenantContextHolder.getTenantId());
		additionalInfo.put(SecurityConstants.PRIZE_TEAM_ID, pigxUser.getPrizeTeamId());
		additionalInfo.put(SecurityConstants.PRIZE_TEAM_RETURN_POINT, pigxUser.getPrizeTeamReturnPoint());
		additionalInfo.put(SecurityConstants.PRIZE_TEAM_RATE_DISCOUNT, pigxUser.getPrizeTeamRateDiscount());
		additionalInfo.put(SecurityConstants.LEVEL_ID,pigxUser.getLevelId());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}
