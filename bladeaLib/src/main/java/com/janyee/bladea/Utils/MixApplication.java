package com.janyee.bladea.Utils;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/4.
 */
public class MixApplication extends Application {
    private static MixApplication myself;
    private static Map<String,Object> dataMap;
    public MixApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataMap=new HashMap<String,Object>();
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());
        // 发送以前没发送的报告(可选)
        myself = this;
    }

    public static MixApplication getInstance() {
        if (myself == null) {
            myself = new MixApplication();
        }
        return myself;
    }

    public void putData(String key, Object obj) {
        if(dataMap==null){
            dataMap=new HashMap<>();
        }
        dataMap.put(key,obj);
    }
    public Object getData(String key){
        if(dataMap!=null){
            return dataMap.get(key);
        }else{
            return null;
        }
    }



    public void removeData(String key) {
        if(dataMap!=null){
            dataMap.remove(key);
        }
    }


}
