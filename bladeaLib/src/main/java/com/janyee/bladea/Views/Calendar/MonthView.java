package com.janyee.bladea.Views.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.janyee.bladea.POJO.ValueTuple;
import com.janyee.bladea.Utils.FastAdapter;

import java.util.List;


/**
 * Created by lier on kmlixh on 2015/11/3.
 */
public class MonthView extends LinearLayout {
    CalendarAdapter adapter;
    GridView mGridView;
    Context context;
    List<CalendarTypedValue> calendarTypedValueList;
    AdapterView.OnItemClickListener listener;
    CalendarUtil util;
    CalendarView view;
    public MonthView(Context context, ValueTuple<CalendarUtil, List<CalendarTypedValue>> tuple, AdapterView.OnItemClickListener listener) {
        super(context);
        this.util = tuple.front;
        setOrientation(VERTICAL);
        calendarTypedValueList = tuple.last;
        this.listener = listener;
        this.view=view;
        this.context = context;
        mGridView = new GridView(context) {
            @Override
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);
            }
        };//让gridView显示全部内容
        mGridView.setNumColumns(7);
        mGridView.setBackgroundColor(Color.parseColor("#656565"));
        mGridView.setOnItemClickListener(listener);
        update();
    }

    public void update() {
        if (adapter == null) {
            adapter = new CalendarAdapter(this, context, util, calendarTypedValueList) {
                @Override
                public View getView(Context context, int position, Object data) {
                    return null;
                }

                @Override
                public View update(View v, int position, Object data) {
                    return null;
                }
            };
            mGridView.setAdapter(adapter);
        } else {
            adapter.mlist = calendarTypedValueList;
            adapter.setUtil(util);
            adapter.notifyDataSetChanged();
        }
        removeAllViews();
        addView(adapter.getTitleView());
        addView(mGridView);
    }

    public void update(ValueTuple<CalendarUtil, List<CalendarTypedValue>> tuple) {
        this.calendarTypedValueList = tuple.getLast();
        this.util = tuple.getFront();
        update();
    }


    public void setChoosedBetweenDate(long t1, long t2) {
        long d1 = t1;
        long d2 = t2;
        if (d1 > d2) {
            long t = d1;
            d1 = d2;
            d2 = t;
        }
        for (int i = 0; i < 42; i++) {
            long cal = util.getCalendarOfPositon(i).getTimeInMillis();
            if (cal >= d1 && cal <= d2) {
                calendarTypedValueList.get(i).setIsChoosed(true);
            }
        }
        adapter.mlist= calendarTypedValueList;
        adapter.notifyDataSetChanged();
    }
    public void cleanChoosed(){
        for(CalendarTypedValue calendarTypedValue : calendarTypedValueList){
            calendarTypedValue.setIsChoosed( false);

        }
        adapter.mlist= calendarTypedValueList;
        adapter.notifyDataSetChanged();
    }
}
