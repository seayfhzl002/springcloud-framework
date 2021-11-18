package com.pig4cloud.pigx.gateway.handler;

import cn.hutool.core.util.RandomUtil;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.gateway.config.SmsConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date 2018/7/5
 * 验证码生成逻辑处理类
 */
@Slf4j
@Component
@AllArgsConstructor
public class SmsCodeHandler implements HandlerFunction<ServerResponse> {
	private final SmsConfig smsConfig;
	private final RedisTemplate redisTemplate;

	@Override
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		//短信配置
		String url = smsConfig.getUrl();
		String userName = smsConfig.getUserName(); //在短信宝注册的用户名
		String passWord = smsConfig.getPassWord(); //在短信宝注册的密码
		String phone = serverRequest.queryParam("phone").get();
		Map<String,String> contents = smsConfig.getContents(); // 注意测试时，也请带上公司简称或网站签名，发送正规内容短信。千万不要发送无意义的内容：例如 测一下、您好。否则可能会收不到
		//构造短信参数
		String type = serverRequest.queryParam("type").get();
		String code = RandomUtil.randomNumbers(6);
		Integer time = SecurityConstants.SMS_CODE_TIME / 60;
		String content = contents.get(type);
		if(StringUtils.isEmpty(content)){
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(BodyInserters.fromObject(R.failed("参数错误")));
		}
		content=content.replace("{code}",code).replace("{time}",time.toString());
		StringBuffer httpArg = new StringBuffer();
		httpArg.append("u=").append(userName).append("&");
		httpArg.append("p=").append(md5(passWord)).append("&");
		httpArg.append("m=").append(phone).append("&");
		httpArg.append("c=").append(encodeUrlString(content, "UTF-8"));

		//保存验证码信息
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.opsForValue().set(CacheConstants.SMS_DEFAULT_CODE_KEY + phone, code
				, SecurityConstants.SMS_CODE_TIME, TimeUnit.SECONDS);
		String result = request(url, httpArg.toString());
		return ServerResponse
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters.fromObject(R.ok()));

	}

	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = reader.readLine();
			if (strRead != null) {
				sbf.append(strRead);
				while ((strRead = reader.readLine()) != null) {
					sbf.append("\n");
					sbf.append(strRead);
				}
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String md5(String plainText) {
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
                    i += 256;
                }
				if (i < 16) {
                    buf.append("0");
                }
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	public static String encodeUrlString(String str, String charset) {
		String strret = null;
		if (str == null) {
            return str;
        }
		try {
			strret = java.net.URLEncoder.encode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strret;
	}
}
