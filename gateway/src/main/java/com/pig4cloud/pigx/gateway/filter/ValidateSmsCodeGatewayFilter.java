package com.pig4cloud.pigx.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.exception.ValidateCodeException;
import com.pig4cloud.pigx.common.core.util.I18nMessageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

/**
 * @author
 * @date 2018/7/4
 * 短信验证码处理
 */
@Slf4j
@Component
@AllArgsConstructor
public class ValidateSmsCodeGatewayFilter extends AbstractGatewayFilterFactory {
	private final ObjectMapper objectMapper;
	private final RedisTemplate redisTemplate;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 不是绑定手机号请求，直接return向下执行
			log.info("==========================:"+request.getURI().getPath());
			Boolean isNeedSmsCode = Boolean.TRUE;
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			if (redisTemplate.hasKey(CommonConstants.IS_NEED_SMS_CODE)){
				  Object object =  redisTemplate.opsForValue().get(CommonConstants.IS_NEED_SMS_CODE);
				  isNeedSmsCode = Boolean.valueOf(object.toString());
				  log.info("Redis isNeedSmsCode value:"+isNeedSmsCode);
			}
			if (!isNeedSmsCode&&StrUtil.containsAnyIgnoreCase(request.getURI().getPath(),SecurityConstants.USER_REGISTER))
				return chain.filter(exchange);
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath()
					, SecurityConstants.BIND_PHONE,SecurityConstants.FIND_PASSWORD,
					SecurityConstants.FIND_WALLET_PASSWORD,SecurityConstants.UN_BIND,
					SecurityConstants.USER_REGISTER)) {
				return chain.filter(exchange);
			}

			try {
				checkCode(request);
			} catch (Exception e) {
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
				response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
				try {
					return response.writeWith(Mono.just(response.bufferFactory()
							.wrap(objectMapper.writeValueAsBytes(
									R.failed(e.getMessage())))));
				} catch (JsonProcessingException e1) {
					log.error("对象输出异常", e1);
				}
			}

			return chain.filter(exchange);
		};
	}

	/**
	 * 检查短信code
	 *
	 * @param request
	 */
	@SneakyThrows
	private void checkCode(ServerHttpRequest request) {
		String language = request.getHeaders().getFirst("Accept-Language");
		String code = request.getQueryParams().getFirst("code");
		if (StrUtil.isBlank(code)) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.smsValidateCodeEmpty", ""));
		}
		String phone = request.getQueryParams().getFirst("phone");
		phone = encodeUrlString(phone, "UTF-8");
		if (StrUtil.isBlank(phone)) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.mobileEmpty", ""));
		}
		String key = CacheConstants.SMS_DEFAULT_CODE_KEY + phone;
		log.info("language: {}, param code: {}, redis key：{}", language, code, key);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		if (!redisTemplate.hasKey(key)) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.smsValidateCodeError", ""));
		}
		Object codeObj = redisTemplate.opsForValue().get(key);
		log.info("redis code value: {}", codeObj);
		if (codeObj == null) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.smsValidateCodeError", ""));
		}
		String saveCode = codeObj.toString();
		if (StrUtil.isBlank(saveCode)) {
//			redisTemplate.delete(key);
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.smsValidateCodeError", ""));
		}
		if (!StrUtil.equals(saveCode, code)) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.smsValidateCodeError", ""));
		}
//		redisTemplate.delete(key);
	}

	public static String encodeUrlString(String str, String charset) {
		String strret;
		if (str == null) {
			return str;
		}
		try {
			strret = URLEncoder.encode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strret;
	}


}
