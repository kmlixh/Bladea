package com.janyee.bladea.Cache;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.janyee.bladea.Dao.Dao;
import com.janyee.bladea.Tools.Md5;

import java.util.ArrayList;
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
            String className=info.getClassName().startsWith("List:")?info.getClassName().substring(5):info.getClassName();
            Class classz=Class.forName(className);
            if(info.getClassName().startsWith("List:")){
                obj=JSON.parseArray(info.getInfo(),classz);
            }else{
                obj= JSON.parseObject(info.getInfo(),classz);
            }
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
    public <T> T get(Class<T> tClass){
        String id= Md5.getMd5(tClass.getCanonicalName());
        return (T) get(id);
    }
    public <T> List<T> getList(Class<T> tClass){
        String id="List:"+tClass.getCanonicalName();
        List<T> obj=new ArrayList<>();

        Dao dao=Dao.getInstance(context);
        try{
            CacheInfo info=dao.fetch(CacheInfo.class,id);
            List<T> temp= JSON.parseArray(info.getInfo(),tClass);
            if(temp!=null){
                obj=temp;
            }
        }catch (Exception e){

        }
        return obj;
    }
    public void put(String id,Object object) throws Exception {
        if(object!=null){
            String className=object.getClass().getCanonicalName();
            CacheInfo info=new CacheInfo();
            info.setCacheId(id);
            info.setClassName(className);
            info.setInfo(JSON.toJSONString(object));
            Dao.getInstance(context).save(info);
        }
    }
    public <T> void putList(String id,List<T> tList) throws Exception {
        if(tList!=null&&tList.size()>0){
            String className="List:"+tList.get(0).getClass().getCanonicalName();
            CacheInfo info=new CacheInfo();
            info.setCacheId(id);
            info.setClassName(className);
            info.setInfo(JSON.toJSONString(tList));
            Dao.getInstance(context).save(info);
        }

    }
    public <T> void putList(List<T> tList) throws Exception {
        String id="List:"+tList.get(0).getClass().getCanonicalName();
        put(id,tList);
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
