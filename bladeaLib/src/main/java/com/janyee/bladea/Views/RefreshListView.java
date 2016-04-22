package com.janyee.bladea.Views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janyee.bladea.Tools.LengthConverter;
import com.janyee.bladea.Utils.FastAdapter;

import java.util.List;

/**
 * Created by kmlixh on 2015/8/7.
 */
public abstract class RefreshListView extends LinearLayout implements AbsListView.OnScrollListener {
    RelativeLayout layout;
    ListView myList;
    FrameLayout header, footer;
    Context context;
    float reFreshOffset = 40;
    boolean IsFreshable=true;
    TextView noData;
    public BaseAdapter getAdapter() {
        return adapter;
    }

    BaseAdapter adapter;
    List listdata;
    int pager = 1;
    private Object data;

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==1){
               myList.setAdapter(adapter);
           }else{
               adapter.notifyDataSetChanged();
               hideRefreshInfo();
           }
            if(listdata!=null&&listdata.size()>0){
                noData.setVisibility(GONE);
            }else{
                noData.setVisibility(VISIBLE);
            }
        }
    };

    public RefreshListView(Context context, Object data) {
        super(context);
        this.context = context;
        layout=new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight=1;
        layout.setLayoutParams(layoutParams);
        myList=new ListView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        myList.setLayoutParams(params);
        noData= new TextView(context);
        noData.setLayoutParams(params);
        this.data = data;
        layout.addView(myList);
        layout.addView(noData);
        Init();
    }

    public void setNoDataVisibility(int visibility) {
        noData.setVisibility(visibility);
    }

    public void Init() {
        LayoutParams headparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        header = new FrameLayout(context);
        header.setLayoutParams(headparams);
        LayoutParams bottomparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footer = new FrameLayout(context);
        footer.setLayoutParams(bottomparams);
        new Thread() {
            @Override
            public void run() {
                getUpdateData(RefreshListView.this);
                adapter = getAdapter(listdata);
                mhandler.sendEmptyMessage(1);
                RefreshListView.this.setEnabled(true);
            }
        }.start();
        LayoutParams listparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listparams.topMargin=LengthConverter.dip2px(context,20);
        listparams.bottomMargin=LengthConverter.dip2px(context,20);
        myList.setLayoutParams(listparams);
        myList.setOnTouchListener(new OnTouchListener() {
            float y;
            boolean isTiped = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isFreshable()) {
                    try {
                        float firstY = myList.getChildAt(0).getY();
                        float lastY = myList.getChildAt(myList.getChildCount() - 1).getY() + myList.getChildAt(myList.getChildCount() - 1).getHeight();
                        float cheight = myList.getHeight();
                        float offset = LengthConverter.dip2px(context, reFreshOffset);
                        float moffset = event.getY(0) - y;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                y = event.getY(0);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (Math.abs(event.getY(0) - y) >= offset) {
                                    if (firstY == 0 && moffset > 0) {//top
                                        header.removeAllViews();
                                        header.addView(getHeaderTip());
                                        isTiped = true;
                                    } else if (lastY <= cheight && moffset < 0) {
                                        footer.removeAllViews();
                                        footer.addView(getFooterTip());
                                        isTiped = true;
                                    }
                                } else {
                                    hideRefreshInfo();
                                    isTiped = false;
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                if (isTiped) {
                                    if (firstY == 0) {
                                        header.removeAllViews();
                                        header.addView(getHeaderJob());
                                        update();
                                        RefreshListView.this.setEnabled(false);
                                    } else if (lastY <= cheight) {
                                        footer.removeAllViews();
                                        footer.addView(getFooterJob());
                                        getMore();
                                        RefreshListView.this.setEnabled(false);
                                    }
                                }
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        this.addView(header);
        this.addView(layout);
        this.addView(footer);
    }
    public void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUpdateData(RefreshListView.this);
                if(adapter!=null){
                    ((FastAdapter)adapter).mlist=listdata;
                }

                mhandler.sendEmptyMessage(0);
            }
        }).start();
    }
    public void getMore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMoreData(RefreshListView.this);
                if(adapter!=null){
                    ((FastAdapter)adapter).mlist=listdata;
                }
                mhandler.sendEmptyMessage(0);
            }
        }).start();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        if (myList != null) {
            myList.setAdapter(adapter);
        }
    }

    public void hideRefreshInfo() {
        header.removeAllViews();
        footer.removeAllViews();
    }

    public List getListdata() {
        return listdata;
    }

    public void setListdata(List listdata) {
        this.listdata = listdata;
    }

    public int getPager() {
        return pager;
    }

    public void setPager(int pager) {
        this.pager = pager;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object object){
        this.data=object;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUpdateData(RefreshListView.this);
                if(adapter!=null){
                    ((FastAdapter)adapter).mlist=listdata;
                }
                mhandler.sendEmptyMessage(0);
            }
        }).start();
    }
    public void sendMessage(int i){
        mhandler.sendEmptyMessage(i);
    }
    public abstract View getHeaderTip();

    public abstract View getFooterTip();

    public abstract View getHeaderJob();

    public abstract View getFooterJob();
    public ListView getMyList() {
        return myList;
    }

    public abstract BaseAdapter getAdapter(List data);

    protected abstract void getUpdateData(RefreshListView refreshListView);

    protected abstract void getMoreData(RefreshListView refreshListView);

    public boolean isFreshable() {
        return IsFreshable;
    }

    public void setIsFreshable(boolean isFreshable) {
        IsFreshable = isFreshable;
    }
}
