package com.pig4cloud.pigx.common.core.util.sign;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtil {

    /**
     * JSON对象转拼接字符串，如cagent=XXXXXXXXX/\\\\/loginname=XXXXXX/\\\\/method=lg
     * /\\\\/actype=0/\\\\/password=XXXXXXXX/\\\\/oddtype=XXX/\\\\/cur=XXX
     *
     * @param jsonObject  JSON对象
     * @param placeholder 拼接符号，如&
     * @return
     */
    public static String convertStr(JSONObject jsonObject, String placeholder) {
        // jsonobject转按字典顺序排序好的map
        SortedMap<String, Object> map = JSONObject.parseObject(jsonObject.toJSONString(), TreeMap.class);
        // key=value+&拼接
        String collect = map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(placeholder));
        log.info("字符串：{}", collect);
        return collect;
    }

    /**
     * JSON对象转拼接字符串，如cagent=XXXXXXXXX/\\\\/loginname=XXXXXX/\\\\/method=lg
     * /\\\\/actype=0/\\\\/password=XXXXXXXX/\\\\/oddtype=XXX/\\\\/cur=XXX
     *
     * @param jsonObject  JSON对象
     * @param placeholder 拼接符号，如&
     * @return
     */
    public static String convertStr(cn.hutool.json.JSONObject jsonObject, String placeholder) {
        // jsonobject转按字典顺序排序好的map
        SortedMap<String, Object> map = jsonObject.toBean(TreeMap.class);
        // key=value+&拼接
        String collect = map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(placeholder));
        log.info("待加密字符串：{}", collect);
        return collect;
    }

    /**
     * JSON对象转拼接字符串，如cagent=XXXXXXXXX/\\\\/loginname=XXXXXX/\\\\/method=lg
     * /\\\\/actype=0/\\\\/password=XXXXXXXX/\\\\/oddtype=XXX/\\\\/cur=XXX
     *
     * @param jsonObject  JSON对象
     * @param placeholder 拼接符号，如&
     * @return
     */
    public static String convertStrNoSort(cn.hutool.json.JSONObject jsonObject, String placeholder) {
        // jsonobject转按字典顺序排序好的map
        Map<String, Object> map = jsonObject.toBean(HashMap.class);
        // key=value+&拼接
        String collect = map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(placeholder));
        log.info("待加密字符串：{}", collect);
        return collect;
    }

    public static String agStr(cn.hutool.json.JSONObject jsonObject) {
        // // hash =md5(cagent + loginname +startdate+ enddate+ slottype + billno +order+by+page+perpage + “明码”)
        String s = jsonObject.getStr("cagent")
                + jsonObject.getStr("startdate")
                + jsonObject.getStr("enddate")
                + jsonObject.getStr("page")
                + jsonObject.getStr("perpage")
                ;
        log.info("待加密字符串：{}", s);
        return s;
    }


    /**
     * 加密方式提供您參考：
     * 加密參數是対參數進行DES (ECB模式, PKCS5填充方式)加密, 再通過Base64編碼所得。
     * 加密函數=Base64編碼(DES_ECB_PKCS5(參數位元組格式))
     *
     * @param param  待加密字符串
     * @param deskey 加密Key
     * @return
     */
    public static String desSignAg(String param, String deskey) {
        log.info("ECB模式Mode：ECB，PKCS5填充方式Padding：PKCS5Padding");
        DES des = new DES(Mode.ECB, Padding.PKCS5Padding, deskey.getBytes(StandardCharsets.UTF_8));
        byte[] decrypt = des.encrypt(param);
        log.info("des加密字符串：{}", new String(decrypt));
        String encode = Base64.getEncoder().encodeToString(decrypt);
        log.info("des加密字符串通过Base64编码后：{}", encode);
        return encode;
    }

    /**
     * @param jsonObject  JSON对象
     * @param placeholder 拼接符号，如&
     * @param deskey
     * @param md5key
     * @return
     */
    public static String signAg(JSONObject jsonObject, String placeholder, String deskey, String md5key) {
        // 获取待加密字符串，类似cagent=XXXXXXX/\\\\/method=tc/\\\\/loginname=XXXXXXX/...
        String param = CommonUtil.convertStr(jsonObject, placeholder);
        // des加密且base64编码后得结果
        param = desSignAg(param, deskey);
        MD5 md5 = MD5.create();
        // 使用 md5 加密 targetParams md5 为 32 位小写字母或数字
        String key = md5.digestHex(param + md5key, "UTF-8").toLowerCase();
        log.info("md5加密后的key：{}", key);
        // // params=XXXXXXXXXXX&key=XXXXXXXXXXXXXXXX
        return "params=" + param + "&key=" + key;
    }

    public static String signAg(cn.hutool.json.JSONObject jsonObject, String placeholder, String deskey, String md5key) {
        // 获取待加密字符串，类似cagent=XXXXXXX/\\\\/method=tc/\\\\/loginname=XXXXXXX/...
//        String param = CommonUtil.convertStr(jsonObject, placeholder);
        String param = CommonUtil.convertStrNoSort(jsonObject, placeholder);
        // des加密且base64编码后得结果
        param = desSignAg(param, deskey);
        MD5 md5 = MD5.create();
        // 使用 md5 加密 targetParams md5 为 32 位小写字母或数字
        String key = md5.digestHex(param + md5key, "UTF-8").toLowerCase();
        log.info("md5加密后的key：{}", key);
        // // params=XXXXXXXXXXX&key=XXXXXXXXXXXXXXXX
        return "params=" + param + "&key=" + key;
    }

    /**
     * 获取xml的元素属性值
     *
     * @param content
     * @return
     */
    public static Element xmlStrToElement(String content) {
        String strXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Notification xmlns=\"http://mns.aliyuncs.com/doc/v1/\">" + content + "</Notification>";
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(strXml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();// 指向根节点
//        System.out.println("msg:" + root.element("result").attribute("msg").getValue());
//        System.out.println("info:" + root.element("result").attribute("info").getValue());
        return root.element("result");

    }
}
