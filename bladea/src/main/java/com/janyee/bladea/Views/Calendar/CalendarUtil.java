package com.janyee.bladea.Views.Calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kmlixh on 2014/8/26.
 */
public class CalendarUtil {
    WeekTag weekTag;
    private Calendar calendar;
    private int firstDayPosition;

    public CalendarUtil(Calendar cal, WeekTag FirstDayInWeek) {
        calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        weekTag = FirstDayInWeek;
        firstDayPosition = getPositionOfFirstDay();
    }

    public static List<Calendar> generateMonthCalendarFromCalendar(Calendar cal, int count) {
        Calendar temps = Calendar.getInstance();
        temps.setTimeInMillis(cal.getTimeInMillis());
        temps.set(Calendar.DAY_OF_MONTH, 1);
        List<Calendar> calendarList = new ArrayList<Calendar>();
        for (int i = 0; i < count; i++) {
            Calendar temp = Calendar.getInstance();
            temp.setTimeInMillis(temps.getTimeInMillis());
            temp.add(Calendar.MONTH, i);
            calendarList.add(temp);
        }
        return calendarList;
    }

    public String getDateString(int position) {
        Calendar cal = getCalendarOfPositon(position);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
    }

    public String getDateString(Calendar cal) {
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
    }

    public WeekTag getWeekTag() {
        return weekTag;
    }

    public void setWeekTag(WeekTag weekTag) {
        this.weekTag = weekTag;
    }

    public Calendar getCalendarOfPositon(int position) {
        int offset = position - firstDayPosition;
        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(calendar.getTimeInMillis());
        temp.set(Calendar.DATE, 1);
        temp.add(Calendar.DATE, offset);
        return temp;
    }

    public int getPositionOfFirstDay() {
        int offset = calendar.get(Calendar.DAY_OF_WEEK) - 1 - weekTag.index();
        if (offset < 0) {
            offset = 7 + offset;
        }
        return offset;
    }

    public String getMonthText() {
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
    }

    public List<Calendar> getList() {
        List<Calendar> result = new ArrayList<Calendar>();
        for (int i = 0; i < getListSize(); i++) {
            Calendar temp = getCalendarOfPositon(i);
            result.add(temp);
        }
        return result;
    }
    public int getDaysOfMonth(){
        int day=calendar.getActualMaximum(Calendar.DATE);
        return day;
    }
    public int getListSize(){
        int days=getDaysOfMonth()+getPositionOfFirstDay();
        int maxRow=days/7*7+(days%7>0?7:0);
        return maxRow;
    }

    public Calendar getCalendar() {
        return calendar;
    }
    public static void main(String[] args){
        CalendarUtil util=new CalendarUtil(Calendar.getInstance(),WeekTag.Wednesday);
        System.out.println(util.getListSize());
    }
}