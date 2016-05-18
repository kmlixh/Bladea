package com.janyee.bladea.Dao.Tuple;

/**
 * Created by kmlixh on 2015/9/22.
 */
public class DataTuple<T> {
    Class aClass;
    T datas;

    public DataTuple(Class classs, T data) {
        this.aClass = classs;
        this.datas = data;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Object getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}
