package com.ehome.microservice.common.test.auth;

import lombok.Data;

@Data
public class OauthTOkenEntity {
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private String expiresIn;
	private String scope;
}
