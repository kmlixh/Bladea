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

    public abstract void refresh();

    public abstract void loadMore();
    public void setData(List<T> mlist){
        this.mlist=mlist;
        mHandler.sendEmptyMessage(1);
    }
    public void addData(List<T> mlist){
        if(this.mlist!=null&&this.mlist.size()>0){
            this.mlist.addAll(mlist);
        }else{
            this.mlist=mlist;
        }
        mHandler.sendEmptyMessage(1);

    }
    public List<T> getData(){
        return mlist;
    }

}
