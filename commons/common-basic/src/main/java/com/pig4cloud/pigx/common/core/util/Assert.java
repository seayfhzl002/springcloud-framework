package com.pig4cloud.pigx.common.core.util;

import cn.hutool.core.util.ArrayUtil;

public class Assert extends cn.hutool.core.lang.Assert {
	/**
	 * 检查值是否在指定范围内
	 *
	 * @param value 值
	 * @param min 最小值（包含）
	 * @param max 最大值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static Number checkBetween(Number value, Number min, Number max,String msg) {
		notNull(value);
		notNull(min);
		notNull(max);
		double valueDouble = value.doubleValue();
		double minDouble = min.doubleValue();
		double maxDouble = max.doubleValue();
		if (valueDouble < minDouble || valueDouble > maxDouble) {
			throw new IllegalArgumentException(msg);
		}
		return value;
	}

	/**
	 * 检查值是否大于参照值
	 *
	 * @param value 值
	 * @param num 参照值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static Number checkLarger(Number value, Number num, String msg) {
		notNull(value);
		notNull(num);
		double valueDouble = value.doubleValue();
		double numDouble = num.doubleValue();
		if (valueDouble < numDouble) {
			throw new IllegalArgumentException(msg);
		}
		return value;
	}

	/**
	 * 检查值是否小于参照值
	 *
	 * @param value 值
	 * @param num 参照值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static Number checkLittle(Number value, Number num, String msg) {
		notNull(value);
		notNull(num);
		double valueDouble = value.doubleValue();
		double numDouble = num.doubleValue();
		if (valueDouble > numDouble) {
			throw new IllegalArgumentException(msg);
		}
		return value;
	}

	/**
	 * 检查元素是否在数组中
	 * @param l 元素
	 * @param array 数组
	 * @param msg
	 */
	public static void checkContains(long l, long[] array, String msg) {
		if(!ArrayUtil.contains(array,l)){
			throw new IllegalArgumentException(msg);
		}
	}


	public static void checkEquals(Object value1,Object value2, String errorMsgTemplate) throws IllegalArgumentException {

		if (!value1.equals(value2)) {
			throw new IllegalArgumentException(errorMsgTemplate);
		}
	}
}
