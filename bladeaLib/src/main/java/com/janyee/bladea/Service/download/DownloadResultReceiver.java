package com.janyee.bladea.Service.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kmlixh on 2016/1/11.
 */
public class DownloadResultReceiver extends BroadcastReceiver {
    DownloadBrocastParser parser;
    DownloadExecutor executor;
    public DownloadResultReceiver(DownloadExecutor executor){
        parser=new DownloadBrocastParser();
        this.executor=executor;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        parser.onReceive(intent,executor);
    }
}
