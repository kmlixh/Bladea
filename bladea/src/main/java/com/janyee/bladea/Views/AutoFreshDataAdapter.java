package com.janyee.bladea.Views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.janyee.bladea.Utils.FastAdapter;

import java.util.List;

/**
 * Created by kmlixh on 2016/5/6.
 */
public abstract class AutoFreshDataAdapter<T, V extends View> extends FastAdapter<T, V> {
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                notifyDataSetChanged();
            }else{
                Toast.makeText(context,"获取数据失败!",Toast.LENGTH_SHORT).show();
            }
        }
    };


    public AutoFreshDataAdapter(Context ctx, List<T> wp) {
        super(ctx, wp);
    }

    public AutoFreshDataAdapter(Context context){
        super(context);
        refresh();
    }

    public void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<T> dataList = refreshWork();
                if (dataList != null && dataList.size() > 0) {
                    mlist = dataList;
                    mHandler.sendEmptyMessage(1);
                }else{
                    mHandler.sendEmptyMessage(-1);
                }
            }
        }).start();
    }

    public void loadMore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<T> dataList = loadMoreWork();
                if (dataList != null && dataList.size() > 0) {
                    if(mlist!=null){
                        mlist.addAll(dataList);
                    }
                    mHandler.sendEmptyMessage(1);
                }else{
                    mHandler.sendEmptyMessage(-1);
                }
            }
        }).start();
    }
    protected abstract List<T> refreshWork();
    protected abstract List<T> loadMoreWork();
}
