package com.janyee.bladea.Service.download;

import android.content.Intent;


/**
 * Created by kmlixh on 2016/1/11.
 */
public class DownloadBrocastParser {
    public Intent getProcessIntent(int process,int total){
        Intent i=new Intent();
        i.putExtra("type","PR");
        i.putExtra("process",process);
        i.putExtra("total",total);
        return i;
    }
    public Intent getFinishedIntent(boolean success){
        Intent i=new Intent();
        i.putExtra("type","FS");
        i.putExtra("status",success);
        return i;
    }
    public Intent getErrorIntent(Exception e){
        Intent i=new Intent();
        i.putExtra("ER",e);
        i.putExtra("type","ER");
        return i;
    }
    public void onReceive(Intent intent,DownloadExecutor executor){
        String type=intent.getStringExtra("type");
        if(type!=null){
            if(type.equals("PR")){
                int process=intent.getIntExtra("process",0);
                int total=intent.getIntExtra("total",0);
                executor.onProcess(process,total);
            }else if(type.equals("FS")){
                executor.onFinished(intent.getBooleanExtra("status",false));
            }else if(type.equals("ER")){
                Exception e=intent.getParcelableExtra("ER");
                executor.onError(e);
            }
        }
    }
}
