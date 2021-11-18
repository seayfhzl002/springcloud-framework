package com.pig4cloud.pigx.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pig4cloud.pigx.common.security.component.PigxAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

@JsonSerialize(using = PigxAuth2ExceptionSerializer.class)
public class UnauthorizedException extends PigxAuth2Exception {

	public UnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}

}
