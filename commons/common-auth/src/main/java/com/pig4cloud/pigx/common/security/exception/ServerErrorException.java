package com.pig4cloud.pigx.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pig4cloud.pigx.common.security.component.PigxAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author
 * @date 2018/7/8
 */
@JsonSerialize(using = PigxAuth2ExceptionSerializer.class)
public class ServerErrorException extends PigxAuth2Exception {

	public ServerErrorException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "server_error";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

}
