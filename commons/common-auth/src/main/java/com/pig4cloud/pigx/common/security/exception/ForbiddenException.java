package com.pig4cloud.pigx.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pig4cloud.pigx.common.security.component.PigxAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author
 * @date 2018/7/8
 */
@JsonSerialize(using = PigxAuth2ExceptionSerializer.class)
public class ForbiddenException extends PigxAuth2Exception {
	public ForbiddenException(String msg) {
		super(msg);
	}
	public ForbiddenException(String msg, Throwable t) {
		super(msg,t);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "access_denied";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.FORBIDDEN.value();
	}

}

