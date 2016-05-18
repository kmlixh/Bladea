package com.janyee.bladea.Dao.Condition;

/**
 * Created by Administrator on 2014/11/26.
 */
public enum OrderBy {
    ASC, DESC;
    String name;
    public OrderBy setKey(String name){
        this.name=name;
        return this;
    }
    public String orderInfo() {
        return "["+name+"] "+this.name();
    }

}
