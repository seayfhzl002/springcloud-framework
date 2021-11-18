package com.pig4cloud.pigx.common.security.component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pig4cloud.pigx.common.core.annotation.EncryptResponse;
import com.pig4cloud.pigx.common.core.exception.BizException;
import com.pig4cloud.pigx.common.core.util.EncryptUtil;
import com.pig4cloud.pigx.common.core.util.I18nMessageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Base64;

/**
 *
 * 对响应体进行加密
 * @author Administrator
 *
 */
@ControllerAdvice
@Slf4j
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body instanceof R) {
			R resBody = (R) body;
			if (resBody.getData() != null) {
				boolean isEncrypt = true;
				if (returnType.hasMethodAnnotation(EncryptResponse.class)) {
					EncryptResponse encryptResponse = returnType.getMethodAnnotation(EncryptResponse.class);
					if (!encryptResponse.value()) {
						isEncrypt = false;
					}
				}
				if (returnType.hasMethodAnnotation(Inner.class)) {
					isEncrypt = false;
				}
				if(isEncrypt) {
					/**
					 * 重写data，进行加密
					 */
					resBody.setData(this.encode(resBody.getData()));
				}
			}
			// 提示msg国际化处理
			if (StringUtils.isNotBlank(resBody.getMsg())) {
				String language = I18nMessageUtil.getLanguage(LocaleContextHolder.getLocale());
				String message = I18nMessageUtil.getMessage(language, resBody.getMsg(), "");
//				log.info("i18n message：{}-{}-{}", language, resBody.getMsg(), message);
				if (StringUtils.isNotBlank(message)) {
					resBody.setMsg(message);
				}
			}
			return resBody;
		}
		return body;
	}

	private String encode(Object data) {
		try {
			String jsonValue = JSONObject.toJSONString(data,
					SerializerFeature.WriteMapNullValue,
					SerializerFeature.WriteNullBooleanAsFalse,
					SerializerFeature.WriteNullStringAsEmpty,
					SerializerFeature.WriteNullListAsEmpty,
					SerializerFeature.DisableCircularReferenceDetect);
//			if (log.isDebugEnabled()) {
//				log.debug("响应体AES加密：raw={},cipher={}", jsonValue, cipher);
//			}
			return Base64.getEncoder().encodeToString(EncryptUtil.aesEncrypt(jsonValue).getBytes());
		} catch (Exception e) {
			throw new BizException("对数据加密异常", e);
		}
	}

}