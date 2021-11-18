package com.pig4cloud.pigx.common.security.component;

import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.exception.AnchorTokenException;
import com.pig4cloud.pigx.common.core.exception.BizException;
import com.pig4cloud.pigx.common.core.exception.CheckedException;
import com.pig4cloud.pigx.common.core.exception.ErrorCode;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author
 * @date 2018/8/30
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerResolver {

	/**
	 * 全局异常.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		return R.failed(null, ErrorCode.SYSTEM_ERROR.getMessage());
	}

	/**
	 * 校验异常.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(CheckedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleCheckException(CheckedException e) {
		log.error("校验异常信息 ex={}", e.getMessage(), e);
		return R.failed(null, e.getMessage());
	}

	/**
	 * 业务异常.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BizException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleBizException(BizException e) {
		log.error("业务异常信息 ex={}", e.getMessage());
		return R.failed(e);
	}

	/**
	 * AccessDeniedException
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor()
				.getMessage("AbstractAccessDecisionManager.accessDenied"
						, e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg, e);
		return R.failed(null, e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 *
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.error("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(null, fieldErrors.get(0).getDefaultMessage());
	}

	/**
	 * AnchorTokenException
	 * 主播token失效
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(AnchorTokenException.class)
	@ResponseStatus(HttpStatus.OK)
	public R handleAccessDeniedException(AnchorTokenException e) {
		log.error("主播token异常 ex={}", e.getMessage(), e);
		return R.failed(null, CommonConstants.TOKEN_FAIL,e.getMessage());
	}
}
