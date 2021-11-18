package com.pig4cloud.pigx.common.core.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class I18nMessageUtil {

	private static final String PATH_PARENT = "classpath:i18n/";
	private static final String BUNDLE = "messages";
	private static final String SUFFIX = ".properties";
	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static Map<String, MessageSourceAccessor> messageSourceAccessorMap = new HashMap<>();

	private I18nMessageUtil(){

	}

	/**
	 * 初始化资源文件的存储器
	 * 加载指定语言配置文件
	 *
	 * @param language 语言类型(文件名即为语言类型,eg: en_us 表明使用 美式英文 语言配置)
	 */
	private static MessageSourceAccessor initMessageSourceAccessor(String language) throws IOException {
		if (messageSourceAccessorMap.containsKey(language)) {
			return messageSourceAccessorMap.get(language);
		}
		/**
		 * 获取配置文件名
		 */
		Resource resource = resourcePatternResolver.getResource(PATH_PARENT + BUNDLE + StrUtil.C_UNDERLINE + language + SUFFIX);
		if (!resource.exists()) {
			resource = resourcePatternResolver.getResource(PATH_PARENT + BUNDLE + SUFFIX);
		}
		String fileName = resource.getURL().toString();
		int lastIndex = fileName.lastIndexOf(".");
		String baseName = fileName.substring(0,lastIndex);
		/**
		 * 读取配置文件
		 */
		ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
		reloadableResourceBundleMessageSource.setBasename(baseName);
		reloadableResourceBundleMessageSource.setCacheSeconds(5);
		reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
		MessageSourceAccessor accessor = new MessageSourceAccessor(reloadableResourceBundleMessageSource);
		messageSourceAccessorMap.put(language, accessor);
		return accessor;
	}

	/**
	 * 获取语言标识
	 * @param locale
	 * @return
	 */
	public static String getLanguage(Locale locale) {
		return locale.toLanguageTag().replace("-", "_");
	}

	/**
	 * 获取一条语言配置信息
	 *
	 * @param language 语言类型,zh_cn: 简体中文, en_us: 英文
	 * @param message 配置信息属性名,eg: api.response.code.user.signUp
	 * @param defaultMessage 默认信息,当无法从配置文件中读取到对应的配置信息时返回该信息
	 * @return
	 * @throws IOException
	 */
	public static String getMessage(String language, String message, String defaultMessage) {
		try {
			return initMessageSourceAccessor(language).getMessage(message, defaultMessage, LocaleContextHolder.getLocale());
		} catch (IOException ex) {
			return defaultMessage;
		}
	}

}