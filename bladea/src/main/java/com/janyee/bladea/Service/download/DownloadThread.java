package com.janyee.bladea.Service.download;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

@SuppressLint("NewApi")
public class DownloadThread implements Runnable {
    DownloadListener listener;
    DownloadTask task;
    File apkFile;   //文件保存路径
    boolean isFinished; //下载是否完成
    boolean interupted = false;  //是否强制停止下载线程
    private String TAG="DOWNLOAD_THREAD";

    public DownloadThread(DownloadListener litener, DownloadTask task) {
        this.listener =litener;
        this.task=task;
        apkFile = new File(task.getPath());
        isFinished = false;
    }

    public File getApkFile() {
        if (isFinished)
            return apkFile;
        else
            return null;
    }

    public boolean isFinished() {
        return isFinished;
    }

    /**
     * 强行终止文件下载
     */
    public void interuptThread() {
        interupted = true;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            java.net.URL url = null;
            HttpURLConnection conn = null;
            InputStream iStream = null;
//          if (DEVELOPER_MODE)
            {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()   // or .detectAll() for all detectable problems
                        .penaltyLog()
                        .build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .detectLeakedClosableObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build());
            }
            try {
                url = new java.net.URL(task.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                if(task.getStart()!=null&&task.getEnd()!=null){
                    conn.setRequestProperty("Range", "bytes=" + task.getStart() + "-" + task.getEnd());// 设置获取实体数据的范围
                }
                conn.setReadTimeout(20000);
                iStream = conn.getInputStream();
            } catch (MalformedURLException e) {
                Log.i(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (Exception e) {
                Log.i(TAG, "获得输入流失败");
                e.printStackTrace();
            }
            RandomAccessFile randomAccessFile=null;

            try {
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                String folderstr = apkFile.getPath();
                folderstr = folderstr.substring(0, folderstr.lastIndexOf("/"));
                File folder = new File(folderstr);
                folder.mkdirs();
                apkFile.createNewFile();
                randomAccessFile=new RandomAccessFile(apkFile,"rw");
            } catch (FileNotFoundException e) {
                Log.i(TAG, "获得输出流失败：new FileOutputStream(apkFile);");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bis = new BufferedInputStream(iStream);
            byte[] buffer = new byte[1024];
            int len;
            // 获取文件总长度
            int length = conn.getContentLength();
            double rate = (double) 100 / length;  //最大进度转化为100
            int total = 0;
            int times = 0;//设置更新频率，频繁操作ＵＩ线程会导致系统奔溃
            try {
                randomAccessFile.setLength(length);
                task.setEnd((long)length-1);
                if(task.getStart()!=null){
                    randomAccessFile.seek(task.getStart());
                }
                Log.i("threadStatus", "开始下载");
                while (false == interupted && ((len = bis.read(buffer)) != -1)) {
                    randomAccessFile.write(buffer, 0, len);
                    // 获取已经读取长度
                    total += len;
                    int p = (int) (total * rate);
                    Log.i("num", rate + "," + total + "," + p);
                    if (times >= 512 || p == 100) {/*
                    这是防止频繁地更新通知，而导致系统变慢甚至崩溃。
                                                             非常重要。。。。。*/
                        Log.i("time", "time");
                        times = 0;
                        Message msg = Message.obtain();
                        msg.what = p;
                        task.setStart((long) total - 1);
                        listener.onProcess(task,total,length);
                    }
                    times++;
                }
                randomAccessFile.close();
                bis.close();
                iStream.close();
                if (total == length) {
                    isFinished = true;
                    Log.i(TAG, "下载完成结束");
                }
                listener.onFinished(task,isFinished);
            } catch (Exception e) {
                Log.i(TAG, "异常中途结束");
                listener.onError(task, e);
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "外部存储卡不存在，下载失败！");
            listener.onError(task, new Exception("外部存储卡不存在，下载失败！"));
        }
    }
}