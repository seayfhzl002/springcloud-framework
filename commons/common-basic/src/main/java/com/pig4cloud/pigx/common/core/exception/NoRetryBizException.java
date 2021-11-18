

package com.pig4cloud.pigx.common.core.exception;

import lombok.NoArgsConstructor;

/**
 * @author
 * @date 😴2018年06月22日16:21:57
 */
@NoArgsConstructor
public class NoRetryBizException extends BizException {

	public NoRetryBizException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NoRetryBizException(String message) {
		super(message);
	}
}
