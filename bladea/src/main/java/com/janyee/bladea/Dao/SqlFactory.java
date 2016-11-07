package com.janyee.bladea.Dao;

import android.content.Context;

import com.janyee.bladea.Cast.Castor;
import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Module.LinkModule;
import com.janyee.bladea.Dao.Module.TableModule;
import com.janyee.bladea.Dao.Pojo.TableVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kmlixh on 2015/10/8.
 */
public class SqlFactory {


    protected static <T> StringBuilder getLinkQuery(T t, LinkModule linkModule) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(linkModule.getDstModule().getTableName() + " WHERE [" + linkModule.getRemoteFieldName() + "] ='" + linkModule.getHolder().getFieldValue(t, linkModule.getLocalFieldName()) + "';");
        return stringBuilder;
    }

    protected static <T> StringBuilder getLinkFetch(T t, LinkModule linkModule) throws Exception {
        StringBuilder stringBuilder = getLinkQuery(t, linkModule);
        stringBuilder.replace(stringBuilder.lastIndexOf(";"), stringBuilder.length(), "").append("LIMIT 0,1");
        return stringBuilder;
    }

    protected static <T> StringBuilder getQuery(TableModule tableModule) throws Exception {
        return getQuery(tableModule, null);
    }
    protected static StringBuilder getQuery(TableModule tableModule, Condition condition) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append("["+tableModule.getTableName()+"]");
        if (condition != null) {
            stringBuilder.append(condition.toString());
        }
        stringBuilder.append(";");
        return stringBuilder;

    }
    protected static StringBuilder getCount(String tableName,Condition condition) throws  Exception{
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT count(*) FROM ").append("["+tableName+"]");
        if (condition != null) {
            stringBuilder.append(condition.toString());
        }
        stringBuilder.append(";");
        return stringBuilder;
    }
    protected static <T> StringBuilder getCount(TableModule tableModule, Condition condition) throws Exception {
        return getCount(tableModule.getTableName(),condition);
    }

    protected static  StringBuilder getFetch(TableModule tableModule, Condition condition) throws Exception {
        condition.Pager(0, 1);
        return getQuery(tableModule, condition);
    }
    protected static StringBuilder getFetch(TableModule module,String id) throws Exception {
        if(!module.getPrimaryCell().getBoundField().getType().equals(String.class)){
            throw new DaoException("can't use a String value as id for this Pojo!");
        }else{
            return getFetch(module,id);
        }
    }
    protected static StringBuilder getFetch(TableModule module,int id) throws Exception {
        if(!Castor.isNumberic(module.getPrimaryCell().getBoundField().getType())){
            throw new DaoException("can't use a number value as id for this Pojo!");
        }else{
            return getFetch(module,id);
        }
    }
    protected static StringBuilder getFetch(TableModule module,Object value) throws Exception {
        Condition condition = Condition.getPrimaryCondition(module,value);
        condition.Pager(0, 1);
        return getQuery(module, condition);
    }
    protected static StringBuilder getInsert(Object obj) throws Exception {
        return getSave(obj).replace(0,7,"INSERT");
    }
    protected static StringBuilder getSave(Object obj) throws Exception {
        if (obj == null) {
            throw new DaoException("you can't save a NULL object");
        } else if (obj instanceof Class) {
            throw new DaoException("you can't save a 'Class Type' object!");
        } else {
            TableModule tableModule = Dao.getTableModule(obj);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("REPLACE INTO [" + tableModule.getTableName() + "] ");
            stringBuilder.append(tableModule.getInsertList());
            return stringBuilder;
        }
    }
    protected static <T> List<StringBuilder> getInsert(Object[] obj) throws Exception {
       List<StringBuilder> info=getSave(obj);
        for(StringBuilder builder:info){
            builder.replace(0,7,"INSERT");
        }
        return info;
    }
    protected static <T> List<StringBuilder> getSave(T[] objs) throws Exception {
        if (objs == null) {
            throw new DaoException("you can't save a NULL object");
        } else if (objs instanceof Class[]) {
            throw new DaoException("you can't save a 'Class Type' object!");
        } else if (objs.length > 0) {
            List<StringBuilder> modules = new ArrayList<>();
            for (Object obj : objs) {
                StringBuilder stringBuilder = getSave(obj);
                modules.add(stringBuilder);
            }
            return modules;
        } else {
            throw new DaoException("you can't save a zore Length objects!");
        }
    }
    protected static <T> List<StringBuilder> getInsert(List<T> objs) throws Exception {
        return getInsert(objs.toArray());
    }
    protected static <T> List<StringBuilder> getSave(List<T> objs) throws Exception {
        return getSave(objs.toArray());
    }

    protected static <T> StringBuilder getDelete(TableModule tableModule) throws Exception {
        return getDelete(tableModule, Condition.Where());
    }

    protected static <T> StringBuilder getDelete(Object obj, Condition condition) throws Exception {
        TableModule tableModule=Dao.getTableModule(obj);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM [" + tableModule.getTableName() + "] ");
        if (condition != null) {
            stringBuilder.append(condition.toString() + ";");
        }
        return stringBuilder;
    }
    protected static <T> StringBuilder getDelete(TableModule tableModule,String id) throws Exception {
        return getDelete(tableModule,Condition.Where(tableModule.getPrimaryCell().getCellName(),"=",id));
    }
    protected static <T> StringBuilder getDelete(TableModule tableModule,int id) throws Exception {
        return getDelete(tableModule,Condition.Where(tableModule.getPrimaryCell().getCellName(),"=",id));
    }
    protected static <T> StringBuilder getDelete(T obj) throws Exception {
        Condition condition = Condition.getPrimaryCondition(obj);
        return getDelete(obj.getClass(), condition);
    }
    protected static <T> List<StringBuilder> getDelete(List<T> objs)throws Exception{
        return getDelete(objs.toArray());
    }
    protected static List<StringBuilder> getDelete(Object[] objs) throws Exception {
        List<StringBuilder> moduleList = new ArrayList<>();
        if (objs != null && objs.length > 0) {
            TableModule tableModule=Dao.getTableModule(objs[0]);
            for (Object obj : objs) {
                moduleList.add(getDelete(obj));
            }
        }
        return moduleList;
    }

    protected static <T> StringBuilder getCreate(TableModule tableModule) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE [" + tableModule.getTableName() + "] (");
        stringBuilder.append(tableModule.getCreateList() + ") ");
        return stringBuilder;
    }

    //CREATE TABLE IF NOT EXISTS
    protected static <T> StringBuilder getCreateIfNotExists(TableModule tableModule) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS [" + tableModule.getTableName() + "] (");
        stringBuilder.append(tableModule.getCreateList() + ") ");
        return stringBuilder;
    }

    protected static <T> StringBuilder getDrop(TableModule tableModule) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE IF EXISTS [" + tableModule.getTableName() + "];");
        return stringBuilder;
    }

    protected static StringBuilder getDrop(String table) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE IF EXISTS [" + table + "];");
        return stringBuilder;
    }

}
