package com.janyee.bladea.Cache;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.janyee.bladea.Dao.Dao;
import com.janyee.bladea.Tools.Md5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kmlixh on 2016/4/29.
 */
public class CacheManager {
    private static Map<String,Object> cacheMap;
    Context context;

    public CacheManager(Context context) {
        this.context = context;
        if(cacheMap==null){
            try{
                initMap();
            }catch (Exception e){

            }
        }
    }
    private void initMap() throws Exception {
        cacheMap=new HashMap<>();
        Dao dao= Dao.getInstance(context);
        List<CacheInfo> infoList=dao.query(CacheInfo.class);
        for(CacheInfo info:infoList){
            Object obj=convertCacheToObject(info);
            if(obj!=null){
                cacheMap.put(info.getCacheId(),obj);
            }else{
                dao.delete(info);
            }
        }
    }
    private  Object convertCacheToObject(CacheInfo info){
        Object obj=null;
        try{
            Class classz=Class.forName(info.getClassName());
            obj= JSON.parseObject(info.getInfo(),classz);
        }catch (Exception e){
            obj=null;
        }
        return obj;
    }
    public Object get(String id){
        Object obj=null;

        Dao dao=Dao.getInstance(context);
        try{
            CacheInfo info=dao.fetch(CacheInfo.class,id);
            obj= convertCacheToObject(info);
        }catch (Exception e){

        }
        return obj;
    }
    public <T> Object get(Class<T> tClass){
        String id= Md5.getMd5(tClass.getCanonicalName());
        return get(id);
    }
    public void put(String id,Object object) throws Exception {
        String className=object.getClass().getCanonicalName();
        CacheInfo info=new CacheInfo();
        info.setCacheId(id);
        info.setClassName(className);
        info.setInfo(JSON.toJSONString(object));
        Dao.getInstance(context).save(info);
    }
    public void put(Object object) throws Exception {
        String id=Md5.getMd5(object.getClass().getCanonicalName());
        put(id,object);
    }
    public void remove(String id) throws Exception {
        Dao.getInstance(context).delete(CacheInfo.class,id);
    }
    public <T> void remove(Class<T> tClass) throws Exception {
        String id=Md5.getMd5(tClass.getCanonicalName());
        remove(id);
    }
}
