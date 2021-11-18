/*package com.pig4cloud.pigx.gateway.filter;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
//import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.exception.ValidateCodeException;
import com.pig4cloud.pigx.common.core.util.I18nMessageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import com.pig4cloud.pigx.gateway.config.FilterIgnorePropertiesConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

*//**
 * @author
 * @date 2018/7/4
 * 验证码处理
 *//*
@Slf4j
@Component
@AllArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory {
	private final ObjectMapper objectMapper;
	private final RedisTemplate redisTemplate;
	private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// 不是登录、代理新增用户、注册请求，直接return向下执行
			log.info("==========================:"+request.getURI().getPath());
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(),
					 SecurityConstants.AGENT_ADD_USER)) {
				return chain.filter(exchange);
			}
			// 刷新token，直接向下执行
			String grantType = request.getQueryParams().getFirst("grant_type");
			if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
				return chain.filter(exchange);
			}
			try {
				//只有登录才会有clientId，但是所有的请求都需要检测验证码，所以对异常不做处理（SecurityConstants.AGENT_ADD_USER这个URL拿不到clientId）
				String[] clientInfos = new String[]{} ;
				try{
					clientInfos = WebUtils.getClientId(request);
				}catch (Exception e){
					log.info("==>clientInfos is empty");
				}
//				 终端设置不校验， 直接向下执行
				if (ArrayUtil.isNotEmpty(clientInfos) && filterIgnorePropertiesConfig.getClients().contains(clientInfos[0])) {
					return chain.filter(exchange);
				}
				// 如果是社交登录，判断是否包含SMS
				if (StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.SOCIAL_TOKEN_URL)) {
					String mobile = request.getQueryParams().getFirst("mobile");
					if (StrUtil.containsAny(mobile, LoginTypeEnum.SMS.getType())) {
						String language = I18nMessageUtil.getLanguage(LocaleContextHolder.getLocale());
						throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.validateCodeError", ""));
					} else {
						return chain.filter(exchange);
					}
				}

				//校验验证码
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

	*//**
	 * 检查code
	 *
	 * @param request
	 *//*
	@SneakyThrows
	private void checkCode(ServerHttpRequest request) {
		String language = request.getHeaders().getFirst("Accept-Language");
		String code = request.getQueryParams().getFirst("code");
//		String language = I18nMessageUtil.getLanguage(LocaleContextHolder.getLocale());
		if (StrUtil.isBlank(code)) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.validateCodeEmpty", ""));
		}
		String randomStr = request.getQueryParams().getFirst("randomStr");
		//https://gitee.com/log4j/pig/issues/IWA0D
		String mobile = request.getQueryParams().getFirst("mobile");
		if (StrUtil.isNotBlank(mobile)) {
			randomStr = mobile;
		}
		String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		if (!redisTemplate.hasKey(key)) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.validateCodeError", ""));
		}
		Object codeObj = redisTemplate.opsForValue().get(key);

		if (codeObj == null) {
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.validateCodeError", ""));
		}
		String saveCode = codeObj.toString();
		if (StrUtil.isBlank(saveCode)) {
			redisTemplate.delete(key);
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.validateCodeError", ""));
		}
		if (!StrUtil.equals(saveCode.toLowerCase(), code.toLowerCase())) {
			redisTemplate.delete(key);
			throw new ValidateCodeException(I18nMessageUtil.getMessage(language, "app.validateCodeError", ""));
		}
		redisTemplate.delete(key);
	}
}
*/