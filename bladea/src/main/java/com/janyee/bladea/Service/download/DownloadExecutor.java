package com.janyee.bladea.Service.download;

/**
 * Created by kmlixh on 2016/1/11.
 */
public abstract class DownloadExecutor {
    private DownloadTask task;
    public DownloadExecutor(String name, String url, String path){
        task=new DownloadTask(name,url,path);
    }

    public DownloadTask getTask() {
        return task;
    }
    public abstract void onProcess(int process,int length);
    public abstract void onFinished(boolean success);
    public abstract void onError(Exception e);
}
