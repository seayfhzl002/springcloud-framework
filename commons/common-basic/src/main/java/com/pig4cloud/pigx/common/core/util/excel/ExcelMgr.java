package com.pig4cloud.pigx.common.core.util.excel;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



@Slf4j
public class ExcelMgr {

    public static Map<String,ExcelModel> map = new ConcurrentHashMap();

    private static ExcelMgr cm = null;

    //构造方法
    private ExcelMgr() {
    }

    public static ExcelMgr getInstance() {
        if (cm == null) {
            cm = new ExcelMgr();
            Thread t = new ClearCache();
            t.start();
        }
        return cm;
    }


    /**
     * 增加缓存
     * @param excelCacheConfModel   缓存对象
     * @return
     */
    public  boolean addCache(ExcelModel excelCacheConfModel) {
        boolean flag = false;
        try {
            map.put(excelCacheConfModel.getDownloadUrl(), excelCacheConfModel);
            System.out.println("now addcache==" + map.get(excelCacheConfModel.getDownloadUrl()));
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }


    /**
     * 获取缓存实体
     */
    public ExcelModel getValue(String key) {
        ExcelModel ob = (ExcelModel) map.get(key);
        if (ob != null) {
            return ob;
        } else {
            return null;
        }
    }


    /**
     * 获取缓存数据的数量
     *
     * @return
     */
    public static int getSize() {
        return map.size();
    }


    /**
     * 删除缓存
     * @param key
     * @return
     */
    public  boolean removeCache(Object key) {
        boolean flag = false;
        try {
            map.remove(key);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static class ClearCache extends Thread{
        @Override
        public void run(){
            while(true){
                Set tempSet = new HashSet();
                Set set = map.keySet();
                Iterator it = set.iterator();
                while(it.hasNext()){
                    Object key = it.next();
                    ExcelModel ccm = (ExcelModel)map.get(key);
                    if((System.currentTimeMillis()-ccm.getBeginTime())>= ccm.getDurableTime()*60*1000){
                        //可以清除，先记录下来
                        tempSet.add(key);
                    }
                }
                //真正清除
                Iterator tempIt = tempSet.iterator();
                while(tempIt.hasNext()){
                    Object key = tempIt.next();
                    map.remove(key);

                }
                log.info("ExcelMgr map size:{}",map.size());
                try {
                    Thread.sleep(60*1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
