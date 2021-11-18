package com.pig4cloud.pigx.common.core.constant;

/**
 * @author
 * @date 2019-04-28
 * <p>
 * 缓存的key 常量
 */
public interface CacheConstants {

	/**
	 * 全局缓存，在缓存名称上加上该前缀表示该缓存不区分租户，比如:
	 * <p/>
	 * {@code @Cacheable(value = CacheConstants.GLOBALLY+CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result == null")}
	 */
	String GLOBALLY = "gl:";

	/**
	 * 验证码前缀
	 */
	String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY:";

	/**
	 * 短信验证码前缀
	 */
	String SMS_DEFAULT_CODE_KEY = "SMS_DEFAULT_CODE_KEY:";


	/**
	 * 菜单信息缓存
	 */
	String MENU_DETAILS = "menu_details";

	/**
	 * 用户信息缓存
	 */
	String USER_DETAILS = "user_details";

	/**
	 * 字典信息缓存
	 */
	String DICT_DETAILS = "dict_details";


	/**
	 * oauth 客户端信息
	 */
	String CLIENT_DETAILS_KEY = "pigx_oauth:client:details";


	/**
	 * spring boot admin 事件key
	 */
	String EVENT_KEY = "event_key";

	/**
	 * 路由存放
	 */
	String ROUTE_KEY = "gateway_route_key";

	/**
	 * redis reload 事件
	 */
	String ROUTE_REDIS_RELOAD_TOPIC = "gateway_redis_route_reload_topic";

	/**
	 * 内存reload 时间
	 */
	String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

	/**
	 * 参数缓存
	 */
	String PARAMS_DETAILS = "params_details";

	/**
	 * 租户缓存 (不区分租户)
	 */
	String TENANT_DETAILS = GLOBALLY + "tenant_details";

	/**
	 * 最近开奖结果
	 */
	String LOTTERY_PERIOD_RESULT_LAST = "lottery_period_result_last";

	/**
	 * 最近期数
	 */
	String LOTTERY_PERIOD_LAST = "lottery_period_last";

    String CHATROOM_LOTTERY_RESULT_MSG = "chatroom_lottery_result_msg";
	/**
	 * 排行榜数据
	 */
	String RANKING_LIST = "ranking_list";

	String SYS_LEVELICONMAP = "sys:leveliconMap";

	String SYS_STOP_LEAVEL = "sys:stoptalkLevel";

	String SYS_USER_INFO = "sys:userInfoMap";

}
