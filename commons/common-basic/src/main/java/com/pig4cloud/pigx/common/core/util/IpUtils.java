package com.pig4cloud.pigx.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
public class IpUtils {

	/**
	 * 获取请求地址IP
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		log.info("X-Real-IP" + Xip);
		String XFor = request.getHeader("X-Forwarded-For");
		log.info("X-Forwarded-For" + XFor);
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}

	/**
	 * 获取用户 设备的物理地址
	 * @param ip
	 * @return
	 */
	public static String what(String ip) {
		try {
			NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(ip));
			byte[] mac = ne.getHardwareAddress();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				//字节转换为整数
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				System.out.println("每8位:" + str);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
			return sb.toString().toUpperCase();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String getIpAddress(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		String ip = headers.getFirst("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = headers.getFirst("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = headers.getFirst("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = headers.getFirst("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = headers.getFirst("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddress().getAddress().getHostAddress();
		}
		return ip;
	}

	public static boolean validIp(List<String> ips) {
		String patternStr = "[0-9]{1,3}.[0-9]{1,3}.[0-9\\*]{1,3}.[0-9\\*]{1,3}";
		String patternStr6 = "([a-fA-F0-9]{1,4}[:]{1,2}){0,7}([:]{0,1}[a-fA-F0-9]{0,4}){0,1}[/]{0,1}[0-9]{0,3}";
		Pattern pattern = Pattern.compile(patternStr);
		Pattern pattern6 = Pattern.compile(patternStr6);
		for(String ip : ips){
			boolean isMatches = pattern.matcher(ip).matches();
			if(!isMatches){
				isMatches = pattern6.matcher(ip).matches();
				if(!isMatches)
					return false;
			}else{
				String[] sections = ip.split("\\.");
				for(String section : sections){
					if(StringUtils.isNumeric(section)){
						if(Long.parseLong(section) > 255){
							return false;
						}
					}
				}
			}
		}


		return true;
	}

    public static boolean isIncludingIp(String ip, List<String> ips) {
		if(CollectionUtil.isEmpty(ips)){
			return false;
		}
		boolean isMatches = false;
		for(String item : ips){
			Pattern pattern = Pattern.compile(item);
			if(pattern.matcher(ip).matches()){
				if(item.contains("*") || StringUtils.equals(ip, item)){
					isMatches = true;
					break;
				}
			}
		}

		return isMatches;
    }
}
