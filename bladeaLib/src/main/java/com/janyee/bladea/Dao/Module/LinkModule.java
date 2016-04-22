package com.janyee.bladea.Dao.Module;


import com.janyee.bladea.Dao.Util.IDataParser;
import com.janyee.bladea.Dao.Util.impl.DataParser;

import java.lang.reflect.Field;

/**
 * Created by kmlixh on 2016/2/15.
 * 用于进行联合查询等操作
 */
public class LinkModule<T> extends TableModule<T> {
    String field;
    TableModule<T> parent;
    Field boundField;//默认要绑定数据的字段，可以是实体变量，也可以是List，数组
    IDataParser parser;

    /**
     *
     * @param tableModule
     * @param boundField
     * @param field
     * @throws Exception
     */
    public LinkModule( TableModule<T> tableModule, Field boundField,String field) throws Exception {
        super(tableModule.getBoundClass());
        parent=tableModule;
        this.field=field;
        this.boundField = boundField;
        parser=new DataParser();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Field getBoundField() {
        return boundField;
    }

    public void setBoundField(Field boundField) {
        this.boundField = boundField;
    }

    public void bindField(T t,Object data) throws Exception {
        boundField.set(t,data);
    }

    public TableModule<T> getParent() {
        return parent;
    }
    @Override
    public String toString(){
        return getField()+":"+getBoundField().getType().getCanonicalName();
    }
}
