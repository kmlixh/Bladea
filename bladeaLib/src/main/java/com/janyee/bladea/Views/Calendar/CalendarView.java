package com.janyee.bladea.Views.Calendar;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.janyee.bladea.POJO.ValueTuple;
import com.janyee.bladea.Utils.FastAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lier on kmlixh on 2015/11/3.
 */
public abstract class CalendarView<T> extends ListView implements android.widget.AdapterView.OnItemClickListener{
    Calendar startCal;
    Calendar endCal;
    MonthView startMonth,endMonth;
    T subs;
    FastAdapter adapter;
    CalendarViewChooseMode mode= CalendarViewChooseMode.CHOOSE_AREA;
    List<ValueTuple<CalendarUtil, List<CalendarTypedValue>>> localData;
    int monthCounts;
    private Context context;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                localData = generateData();

                adapter = new FastAdapter(context, localData) {
                    @Override
                    public View getView(Context context, int position, Object o) {
                        MonthView view = new MonthView(context, (ValueTuple<CalendarUtil, List<CalendarTypedValue>>) o, CalendarView.this);
                        return view;
                    }

                    @Override
                    public View update(View v, int position, Object o) {
                        ((MonthView) v).update((ValueTuple<CalendarUtil, List<CalendarTypedValue>>) o);
                        return v;
                    }
                };
                setAdapter(adapter);
            }
        }
    };

    public CalendarView(Context context, T t, int num) {
        super(context);
        this.context = context;
        subs = t;
        this.monthCounts = num;
        new Thread(new RemoteTask()).start();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract boolean LongTimeWork(T t);

    public List<ValueTuple<CalendarUtil, List<CalendarTypedValue>>> generateData(){
        List<Calendar> monthCal = CalendarUtil.generateMonthCalendarFromCalendar(Calendar.getInstance(), monthCounts);
        List<ValueTuple<CalendarUtil, List<CalendarTypedValue>>> LLP = new ArrayList<ValueTuple<CalendarUtil, List<CalendarTypedValue>>>();
        for (Calendar cal : monthCal) {
            List<CalendarTypedValue> tuples = new ArrayList<CalendarTypedValue>();
            Calendar temps = Calendar.getInstance();
            temps.setTimeInMillis(cal.getTimeInMillis());
            CalendarUtil util = new CalendarUtil(temps, WeekTag.Monday);
            for (Calendar temp : util.getList()) {
                CalendarTypedValue item=getListItem(temp,util);
                tuples.add(item);

            }
            LLP.add(new ValueTuple<CalendarUtil, List<CalendarTypedValue>>(util, tuples));
        }
        return LLP;
    }
    public abstract CalendarTypedValue getListItem(Calendar cal,CalendarUtil util);
    private class RemoteTask implements Runnable {
        @Override
        public void run() {
            boolean ok = LongTimeWork(subs);
            if (ok) {
                mHandler.sendEmptyMessage(1);
            }
        }
    }
    @Override
    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
        MonthView monthView= (MonthView) view.getTag();
        if(monthView.calendarTypedValueList.get(position).isEnable()){
            if(mode== CalendarViewChooseMode.CHOOSE_AREA){
                CalendarUtil util=((MonthView) view.getTag()).util;
                if(startCal==null&&endCal==null){
                    startCal=util.getCalendarOfPositon(position);
                    startMonth= (MonthView) view.getTag();
                    startMonth.calendarTypedValueList.get(position).setIsChoosed(true);
                    startMonth.adapter.notifyDataSetChanged();
                }else if(startCal!=null&&endCal==null){
                    if(startMonth.calendarTypedValueList.get(position).isChoosed()){
                        startMonth.cleanChoosed();
                        startMonth.adapter.notifyDataSetChanged();
                        startCal=null;
                        endCal=null;
                        startMonth=null;
                    }else{
                        endCal=util.getCalendarOfPositon(position);
                        endMonth= (MonthView) view.getTag();
                        startMonth.setChoosedBetweenDate(startCal.getTimeInMillis(),endCal.getTimeInMillis());
                        endMonth.setChoosedBetweenDate(startCal.getTimeInMillis(),endCal.getTimeInMillis());
                        startMonth.adapter.notifyDataSetChanged();
                        endMonth.adapter.notifyDataSetChanged();
                    }
                }
                else if(startCal!=null&&endCal!=null){
                    startMonth.cleanChoosed();
                    startMonth.adapter.notifyDataSetChanged();
                    endMonth.cleanChoosed();
                    endMonth.adapter.notifyDataSetChanged();
                    endCal=null;
                    endMonth=null;
                    startCal=util.getCalendarOfPositon(position);
                    startMonth= (MonthView) view.getTag();
                    startMonth.calendarTypedValueList.get(position).setIsChoosed(true);
                    startMonth.adapter.notifyDataSetChanged();
                }
            }else if(mode== CalendarViewChooseMode.CHOOSE_SINGLE){
                if(startMonth!=null){
                    startMonth.cleanChoosed();
                }
                CalendarUtil util=((MonthView) view.getTag()).util;
                startCal=util.getCalendarOfPositon(position);
                startMonth= (MonthView) view.getTag();
                startMonth.calendarTypedValueList.get(position).setIsChoosed(true);
                startMonth.adapter.notifyDataSetChanged();
            }
        }

    }
    public List<Calendar> getChoosedCalendar(){
        List<Calendar> calendarList=new ArrayList<Calendar>();
        if(startMonth!=null){
            for(int i=0;i<42;i++){
                if(startMonth.calendarTypedValueList.get(i).isChoosed()){
                    calendarList.add(startMonth.util.getCalendarOfPositon(i));
                }
            }
        }
        if(endMonth!=null){
            for(int i=0;i<42;i++){
                if(endMonth.calendarTypedValueList.get(i).isChoosed()){
                    calendarList.add(endMonth.util.getCalendarOfPositon(i));
                }
            }
        }
        return calendarList;
    }
    public void setChooseMode(CalendarViewChooseMode mode){
        this.mode=mode;
    }
    public enum CalendarViewChooseMode{
        CHOOSE_SINGLE,CHOOSE_AREA,UN_CHOOSED;
    }
}
