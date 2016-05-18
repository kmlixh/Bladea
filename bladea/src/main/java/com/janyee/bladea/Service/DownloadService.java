package com.janyee.bladea.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.janyee.bladea.Service.download.DownloadBrocastParser;
import com.janyee.bladea.Service.download.DownloadListener;
import com.janyee.bladea.Service.download.DownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmlixh on 2015/12/30.
 */
public class DownloadService extends Service implements DownloadListener {
    List<DownloadTask> taskPool;
    DownloadBrocastParser parser;
    @Override
    public void onCreate(){
        taskPool=new ArrayList<>();
        parser=new DownloadBrocastParser();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){

        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onProcess(DownloadTask task, int process, int length) {
        parser.getProcessIntent(process,length);
    }

    @Override
    public void onFinished(DownloadTask task,boolean success) {
        taskPool.remove(task);
    }

    @Override
    public void onError(DownloadTask task, Exception e) {

    }

}