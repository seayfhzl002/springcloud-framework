

package com.pig4cloud.pigx.common.core.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author
 * @date 😴2018年06月22日16:21:57
 */
@NoArgsConstructor
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private Integer errorCode;

	public BizException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode.getErrorCode();
	}

	public BizException(String message) {
		super(message);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
