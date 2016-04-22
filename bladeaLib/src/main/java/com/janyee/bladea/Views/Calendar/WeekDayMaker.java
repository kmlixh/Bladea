package com.janyee.bladea.Views.Calendar;


/**
 * Created by lier on kmlixh on 2015/11/3.
 */
public class WeekDayMaker {
    public static String[] getWeekTitles(WeekTag tag) {
        if (tag == WeekTag.Monday) {
            return new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日" };
        } else {
            return new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        }
    }
}
