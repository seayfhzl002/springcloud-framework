package com.pig4cloud.pigx.common.security.util;

//import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化敏感词库<br>
 * 将敏感词加入到HashMap中<br>
 * 构建DFA算法模型
 *
 * @author dxm
 *
 */

//@Slf4j
public class SensitivewordFilter {

	// 最小匹配规则
	public static int minMatchTYpe = 1;

	private static Map sensitiveWordMap = null;


	/**
	 * 初始化敏感字库
	 *
	 * @return
	 */
	/*public static boolean isShield(String nickName,List<ZxUserShield> list) {

		// 读取敏感词库
		Set<String> wordSet = new HashSet<String>();
		for(ZxUserShield zxUserShield :list){
			wordSet.add(zxUserShield.getKeyWord());
		}
		// 将敏感词库加入到HashMap中
		addSensitiveWordToHashMap(wordSet);
		return getSensitiveWord(nickName,1);
	}*/

	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = {
	 *      isEnd = 0
	 *      国 = {<br>
	 *           isEnd = 1
	 *           人 = {isEnd = 0
	 *                民 = {isEnd = 1}
	 *                }
	 *           男  = {
	 *                  isEnd = 0
	 *                   人 = {
	 *                        isEnd = 1
	 *                       }
	 *               }
	 *           }
	 *      }
	 *  五 = {
	 *      isEnd = 0
	 *      星 = {
	 *          isEnd = 0
	 *          红 = {
	 *              isEnd = 0
	 *              旗 = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *          }
	 *      }
	 * @author chenming
	 * @date 2014年4月20日 下午3:04:20
	 * @param keyWordSet  敏感词库
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
		String key = null;
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			//关键字
			key = iterator.next();
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				//转换成char型
				char keyChar = key.charAt(i);
				//获取
				Object wordMap = nowMap.get(keyChar);
				//如果存在该key，直接赋值
				if(wordMap != null){
					nowMap = (Map) wordMap;
				}else{//不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String,String>();
					//不是最后一个
					newWorMap.put("isEnd", "0");
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //最后一个
				}
			}
		}
	}

	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @author chenming
	 * @date 2014年4月20日 下午4:31:03
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes"})
	public static  int checkSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
		int matchCnt = 0;     //匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word);     //获取指定key
			if(nowMap != null){     //存在，则判断是否为最后一个
				matchCnt++;     //找到相应key，匹配标识+1
				if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true;       //结束标志位为true
					if(SensitivewordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}else{     //不存在，直接返回
				break;
			}
		}
		if(matchCnt < 2 && !flag){
			matchCnt = 0;
		}
		return matchCnt;
	}


	/**
	 *获取文字中的敏感词
	 *@param txt       文字
	 *@param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
	 *@return
	 */

	public static boolean getSensitiveWord(String txt, int matchType) {
		Set<String> sensitiveWordList = new HashSet<>();
		for (int i = 0; i < txt.length(); i++) {
			//判断是否包含敏感字符
			int length = checkSensitiveWord(txt, i, matchType);
			if (length > 0) {//存在,加入list中
				sensitiveWordList.add(txt.substring(i, i + length));
				i = i + length - 1;//减1的原因，是因为for会自增
			}
		}
//		log.info("语句中包含敏感词的个数为：{}。包含：{}",sensitiveWordList.size(), sensitiveWordList);
		return CollectionUtils.isEmpty(sensitiveWordList)?false:true;
	}
}