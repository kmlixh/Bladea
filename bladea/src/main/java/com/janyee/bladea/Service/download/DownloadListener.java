package com.janyee.bladea.Service.download;

/**
 * Created by kmlixh on 2016/1/11.
 */
public interface DownloadListener {
    public void onProcess(DownloadTask task, int process, int length);
    public void onFinished(DownloadTask task, boolean success);
    public void onError(DownloadTask task, Exception e);
}