package com.pig4cloud.pigx.common.core.util;

import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Classname utilStrUtil
 * @Date 2020/10/18 14:02
 * @Created by jack
 */
@Slf4j
public class StringUtil {

	private static SortedMap<Object, Object> sortedMap = new TreeMap<>();

	/**
	 * json字符串转SortedMap
	 *
	 * @param s
	 * @return
	 */
	@SneakyThrows(Exception.class)
	private static SortedMap<Object, Object> strTransSortedMap(String s) {
		sortedMap.clear();
		Map<Object, Object> map = (Map) JSONUtil.parse(s);
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	//按照ASCII码排序
	@SneakyThrows(Exception.class)
	public static String createSign(String s) {
		SortedMap<Object, Object> parameter = strTransSortedMap(s);
		StringBuffer sb = new StringBuffer();
		Set es = parameter.entrySet();  //所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			//空值不传递，不参与签名组串
			if (null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		String result = sb.toString().substring(0, sb.toString().length() - 1);
		//排序后的字符串
//		log.info("待签名字符串：" + result);
		return result;
	}
}
