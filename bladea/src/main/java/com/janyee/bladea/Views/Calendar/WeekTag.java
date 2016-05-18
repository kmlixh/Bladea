package com.janyee.bladea.Views.Calendar;

/**
 * Created by Administrator on 2014/12/4.
 */
public enum WeekTag {
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;

    public int index() {
        return this.ordinal();
    }
}
