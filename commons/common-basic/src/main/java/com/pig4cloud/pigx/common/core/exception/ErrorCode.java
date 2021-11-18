package com.pig4cloud.pigx.common.core.exception;

import com.pig4cloud.pigx.common.core.util.I18nMessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * @author
 * @date 2019-05-16
 * <p>
 * 字典类型
 */

@AllArgsConstructor
@Slf4j
public enum ErrorCode {

	NO_LOGIN(501, "无法获取用户信息"),
	DATE_TIME_FORMAT_EXCEPTION(502, "日期转换异常 格式必须为 2020-01-01----2021-01-01 或 2020-01-01 00:00:00----2021-01-01 23:59:59"),
	UPLOAD_FILE_EXCEPTION(503, "文件上传失败"),
	WALLET_NOALLOW_UPDATE(504, "非出账项目禁止调用API写库，必须通过消息队列异步操作"),
	WALLET_UPDATE_VERSION_EXCEPTION(505, "账户更新失败，请稍后再试"),
	WALLET_BALANCE_NO_ENOUGH(506, "余额不足"),
	OPEN_PRIZE_EXCEPTION(507, "开奖异常"),
	OPEN_PRIZE_ALREADY_EXCEPTION(508, "开奖异常，对应期号已开奖！"),
	CLEARING_ALREADY_EXCEPTION(509, "派彩异常，对应注单已结算！"),
	BET_NOFOUND_EXCEPTION(510, "无法查询注单信息！"),
	BET_CANCEL_FAIL_CAUSE_NO_SELF(511,"撤单失败，不能撤销他人注单！"),
	BET_CANCEL_FAIL_CAUSE_ALREAD_OPEN(512,"撤单失败，期号已封盘！"),
	LOTTERY_CONFIG_RANDOM_LHC(513,"六合彩开奖不支持百分比配置,请设置为随机开奖！"),
	OPEN_PRIZE_PERIOD_NULL_EXCEPTION(514, "开奖异常，对应期号已删除！"),
	ALREADY_LIVE_EXCEPTION(515, "您已有直播间活动中是否继续直播！"),
	WALLET_UPDATE_ERROR(516, "金额有误，变更失败"),
	SYSTEM_ERROR(500, "服务器繁忙"),
	BET_CANCEL_FAIL_CAUSE_ALREAD_CANCEL(517,"撤单失败，不能重复撤销！"),
	SYSTEM_ERRORPARAMETER(518, "app.errorParameter"),
	WALLET_REFRESH_ERROR(600,"查询第3方账户余额失败!"),
	WALLET_TRANSFER_ERROR(601,"转账失败!"),
	//third part game
	TP_GAMES_NO_FISHING_ERROR(700,"app.noFishingGame"),
	TP_GAMES_NO_SLOT_ERROR(701,"app.noSlotGame"),
	TP_GAMES_NO_LIVE_ERROR(702,"app.noLiveGame"),
	TP_GAMES_NO_QIPAI_ERROR(712,"app.noQipaiGame"),
	TP_GAMES_IG_GAME_RECORD_ERROR(703,"查询IG游戏记录失败!"),
	TP_GAMES_GAME_ACCESS_URL_ERROR(707,"app.gameAccessError"),
	TP_GAMES_GAME_CREATE_ACCOUNT_ERROR(708,"app.createUserError"),
	TP_GAMES_TRANSFER_ERROR(709,"app.transferError"),
	TP_GAMES_INQUIRE_BALANCE_ERROR(710,"app.inquireBalanceError"),
	TP_GAMES_CONFIG_STATUS_ERRORACTION(711,"app.game.config.status.errorAction"),
	TP_GAMES_EXTEND_TIME_RANGE_ERROR(712,"app.game.extend_time_range"),
	//upms
	TP_UPMS_BLACKLIST_INVALID_IP(800,"app.upms.blacklist.validIp"),
	TP_UPMS_BLACKLIST_IP_RESTRICTED(801,"app.upms.blacklist.ip.restricted"),
	TP_UPMS_BLACKLIST_IP_LOCAL_ERROR(802,"app.upms.blacklist.ip.local.error"),
	TP_UPMS_BLACKLIST_IP_LOCAL_MISSING(803,"app.upms.blacklist.ip.local.missing"),
	;
	/**
	 * 异常码
	 */
	@Getter
	private int errorCode;
	/**
	 * 描述
	 */
	private String message;

	/**
	 * @return 根据语言环境返回国际化字符串
	 */
	public String getMessage() {
		String language = I18nMessageUtil.getLanguage(LocaleContextHolder.getLocale());
		return I18nMessageUtil.getMessage(language, "app.enum." + this.name(), message);
	}

	public String getMessageDirect() {
		String language = I18nMessageUtil.getLanguage(LocaleContextHolder.getLocale());
		return I18nMessageUtil.getMessage(language, message, message);
	}
}
