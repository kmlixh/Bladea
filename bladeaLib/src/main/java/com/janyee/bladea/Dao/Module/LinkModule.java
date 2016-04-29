package com.janyee.bladea.Dao.Module;


import com.janyee.bladea.Dao.SqlFactory;
import com.janyee.bladea.Dao.Util.IDataParser;

import java.lang.reflect.Field;

/**
 * Created by kmlixh on 2016/2/15.
 * 用于进行联合查询等操作
 */
public class LinkModule {
    String localFieldName;
    String remoteFieldName;
    TableModule holder;
    TableModule dstModule;
    Field boundField;//默认要绑定数据的字段，可以是实体变量，也可以是List，数组

    public LinkModule( TableModule holder, Field boundField,String localFieldName,String remoteFieldName,Class boundClass) throws Exception {
        this.holder=holder;
        dstModule= SqlFactory.getTableModule(boundClass);
        this.boundField=boundField;
        this.localFieldName=localFieldName;
        this.remoteFieldName=remoteFieldName;
        if(remoteFieldName==null||remoteFieldName.equals("")){
            this.remoteFieldName=dstModule.getPrimaryCell().getCellName();
        }
    }


    public Field getBoundField() {
        return boundField;
    }


    public void bindField(Object t,Object data) throws Exception {
        boundField.setAccessible(true);
        boundField.set(t,data);
    }

    public String getLocalFieldName() {
        return localFieldName;
    }

    public String getRemoteFieldName() {
        return remoteFieldName;
    }

    public TableModule getDstModule() {
        return dstModule;
    }

    public TableModule getHolder() {
        return holder;
    }
    @Override
    public String toString(){
        return localFieldName+":"+remoteFieldName+":"+getBoundField().getType().getCanonicalName()+","+dstModule.getBoundClass().getCanonicalName();
    }
}
