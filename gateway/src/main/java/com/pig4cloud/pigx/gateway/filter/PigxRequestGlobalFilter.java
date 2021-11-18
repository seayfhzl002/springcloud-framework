package com.pig4cloud.pigx.gateway.filter;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.exception.BizException;
import com.pig4cloud.pigx.common.core.exception.ErrorCode;
import com.pig4cloud.pigx.common.core.exception.ValidateCodeException;
import com.pig4cloud.pigx.common.core.util.*;
import com.pig4cloud.pigx.gateway.config.FilterIgnorePropertiesConfig;
import com.pig4cloud.pigx.gateway.feign.RemoteUserService;
import com.pig4cloud.pigx.gateway.feign.RemoteIPLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * @author
 * @date 2018/10/8
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗
 * 2. 重写StripPrefix = 1,支持全局
 * <p>
 * 支持swagger添加X-Forwarded-Prefix header  （F SR2 已经支持，不需要自己维护）
 */
@Slf4j
@Component
public class PigxRequestGlobalFilter implements GlobalFilter, Ordered {
	@Autowired
	private RemoteIPLimitService remoteIPLimitService;

	@Autowired
	private RemoteUserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

	private final String[] BACK_GROUND_CLIENTS = new String[]{"pig"};

	private final String KEY_QUERY_PARAM_USER_NAME = "username";

	private final String KEY_QUERY_PARAM_ADMIN = "admin";
	/**
	 * Process the Web request and (optionally) delegate to the next
	 * {@code WebFilter} through the given {@link GatewayFilterChain}.
	 *
	 * @param exchange the current server exchange
	 * @param chain    provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		// 1. 清洗请求头中from 参数
		ServerHttpRequest request = exchange.getRequest().mutate()
			.headers(httpHeaders -> httpHeaders.remove(SecurityConstants.FROM))
			.build();

		try{
			String userName = parseUserNameFromReq(request);
			if(StrUtil.isEmpty(userName) || !userName.contains(KEY_QUERY_PARAM_ADMIN)){
				String remoteIP = IpUtils.getIpAddress(request);
				String reqUrl = request.getURI().getRawPath();
				validIp(request,remoteIP);
				log.info("remote ip {} is valid, request {}", remoteIP, reqUrl);
			}
			if (StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.OAUTH_TOKEN_URL)) {
				checkCode(request);
			}
		}catch(Exception e){
			ServerHttpResponse response = exchange.getResponse();
			response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
			response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
			try {
				return response.writeWith(Mono.just(response.bufferFactory()
						.wrap(objectMapper.writeValueAsBytes(R.failed(e.getMessage())))));
			} catch (JsonProcessingException e1) {
				log.error("对象输出异常", e1);
			}
		}

		// 2. 重写StripPrefix
		addOriginalRequestUrl(exchange, request.getURI());
		String rawPath = request.getURI().getRawPath();
		String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
			.skip(1L).collect(Collectors.joining("/"));
		ServerHttpRequest newRequest = request.mutate()
			.path(newPath)
			.build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

		return chain.filter(exchange.mutate()
			.request(newRequest.mutate()
				.build()).build());
	}

	private void validIp(ServerHttpRequest request,String remoteIP) {
		String client = request.getHeaders().getFirst("client");
		Integer rangeType = CommonConstants.BALCK_LIST_RANGE_TYPE_FONTROUND;
		if(org.apache.commons.lang3.StringUtils.isBlank(client)){
			return ;
		}
		boolean isBackground = Arrays.stream(BACK_GROUND_CLIENTS).anyMatch(item-> org.apache.commons.lang3.StringUtils.equals(item,client));
		if(isBackground){
			rangeType = CommonConstants.BALCK_LIST_RANGE_TYPE_BACKGROUND;
		}
		boolean isValidIP = remoteIPLimitService.isValidIP(SecurityConstants.FROM_IN,rangeType, remoteIP);
		if(!isValidIP){
			log.error("invalid ip {}", remoteIP);
			String language = request.getHeaders().getFirst("Accept-Language");
			throw new BizException(I18nMessageUtil.getMessage(language,ErrorCode.TP_UPMS_BLACKLIST_IP_RESTRICTED.getMessage(),""));
		}
	}

	/**
	 * 检查谷歌验证码
	 *
	 * @param request
	 */
	private void checkCode(ServerHttpRequest request) throws Exception{
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
				return ;
			}
			String code = request.getQueryParams().getFirst("code");
			if (StrUtil.isBlank(code)) {
				throw new ValidateCodeException("谷歌验证码不能为空");
			}
			String username = request.getQueryParams().getFirst("username");
			if (StrUtil.isBlank(username)) {
				throw new ValidateCodeException("谷歌码验证失败");
			}
			R<String> userInfo = userService.getGoogleSecret(username, SecurityConstants.FROM_IN);
			if (userInfo == null || userInfo.getData() == null) {
				throw new ValidateCodeException("谷歌码验证失败");
			}
			if (StrUtil.isBlank(userInfo.getData())) {
				throw new ValidateCodeException("用户未绑定谷歌码，请联系管理员");
			}
			if(!GoogleAuthenticator.authcode(code, userInfo.getData())){
				throw new ValidateCodeException("谷歌验证码错误");
			}
		} catch (Exception e) {
			throw e;
		}


	}

	private String parseUserNameFromReq(ServerHttpRequest request){
		String token = request.getHeaders().getFirst("Authorization");
		if(!StringUtils.isEmpty(token)){
			String userName = userService.queryUsername(SecurityConstants.FROM_IN, token);
			if(!StringUtils.isEmpty(userName)){
				return userName;
			}
		}

		return request.getQueryParams().getFirst(KEY_QUERY_PARAM_USER_NAME);
	}

	@Override
	public int getOrder() {
		return -1000;
	}
}
