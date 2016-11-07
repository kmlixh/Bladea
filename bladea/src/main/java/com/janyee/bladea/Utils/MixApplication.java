package com.janyee.bladea.Utils;

import android.app.Application;

import com.janyee.bladea.Cache.CacheManager;

/**
 * Created by Administrator on 2014/12/4.
 */
public abstract class MixApplication<T extends MixApplication> extends Application {
    private static MixApplication myself;
    public T instance;
    CacheManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        manager=new CacheManager(getApplicationContext());
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext());
        // 发送以前没发送的报告(可选)
        myself = this;
        instance=(T)myself;
    }

    public static MixApplication getInstance() {
        return myself;
    }

    public CacheManager getManager(){
        return manager;
    }


}
