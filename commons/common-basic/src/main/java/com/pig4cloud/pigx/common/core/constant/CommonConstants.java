package com.pig4cloud.pigx.common.core.constant;

/**
 * @author
 * @date 2017/10/29
 */
public interface CommonConstants {
	/**
	 * header 中租户ID
	 */
	String TENANT_ID = "TENANT-ID";

	/**
	 * header 中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * header 中设备标示
	 */
	String DRIVE = "DRIVE";

	/**
	 * 租户ID
	 */
	String DRIVE_UNKNOW = "unknow";

	/**
	 * 租户ID
	 */
	String TENANT_ID_DEV = "dev";

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";
	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单
	 */
	String MENU = "0";

	/**
	 * 菜单树根节点
	 */
	Integer MENU_TREE_ROOT_ID = -1;

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "pigx-ui";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "pigx";

	/**
	 * 公共参数
	 */
	String PIG_PUBLIC_PARAM_KEY = "PIG_PUBLIC_PARAM_KEY";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;
	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * token错误（用于直播主播端登陆）
	 */
	Integer TOKEN_FAIL = 2;

	/**
	 * 电影开关key
	 */
	String MOVIE_KEY = "movie";

	/**
	 * 电影开关key
	 */
	String MOVIE_ENABLE = "enable";

	/**
	 * 赠送新用户num张观影券
	 */
	String MOVIE_NUM = "num";

	/**
	 * 全局配置key
	 */
	String GLOBAL_CONFIG = "global_config";

	/**
	 * 全局配置语言key
	 */
	String GLOBAL_LANGUAGE = "global_language";

	/**
	 * 全局直播机器人拉流链接key
	 */
	String LIVE_ROBOT_URL_PREFIX = "live_robot_url_prefix";

	/**
	 * 全局直播机器人拉流平台key
	 */
	String LIVE_ROBOT_PLATFORM = "live_robot_platform";
	/**
	 * 注册是否需要短信验证码key
	 */
	String IS_NEED_SMS_CODE = "isNeedSmsCode";
	/**
	 * 官方邀请码key
	 */
	String OFFICE_INVITE_CODE = "officeInviteCode";
	/**
	 * 预批量生成用户数
	 */
	final Integer[] PRE_USER_NUMBER = new Integer[]{10,20,30,40,50,100,50,200,500};

	String PRE_BATCH_CREATE_USER_JOB_KEY = "admin:user:prebatch:job:number";

	Integer BALCK_LIST_RANGE_TYPE_BACKGROUND = 0;

	Integer BALCK_LIST_RANGE_TYPE_FONTROUND = 1;

	String ROLE_FRONT = "ROLE_FRONT";
	
	final String ASTERISK = "*";

}
