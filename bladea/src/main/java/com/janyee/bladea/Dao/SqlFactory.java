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
    protected static StringBuilder getInsert(TableModule tableModule) throws Exception {
        return getSave(tableModule).replace(0,7,"INSERT");
    }
    protected static StringBuilder getSave(TableModule tableModule) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("REPLACE INTO [" + tableModule.getTableName() + "] ");
        stringBuilder.append(tableModule.getInsertList());
        return stringBuilder;
    }
    protected static <T> List<StringBuilder> getInsert(TableModule<T>...tableModules) throws Exception {
       List<StringBuilder> info=getSave(tableModules);
        for(StringBuilder builder:info){
            builder.replace(0,7,"INSERT");
        }
        return info;
    }
    protected static <T> List<StringBuilder> getSave(TableModule<T>...tableModules) throws Exception {
        List<StringBuilder> modules = new ArrayList<>();
        for (TableModule<T> obj : tableModules) {
            StringBuilder stringBuilder = getSave(obj);
            modules.add(stringBuilder);
        }
        return modules;
    }

    protected static <T> StringBuilder getDelete(TableModule tableModule) throws Exception {
        return getDelete(tableModule, Condition.Where());
    }

    protected static <T> StringBuilder getDelete(TableModule tableModule, Condition condition) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM [" + tableModule.getTableName() + "] ");
        if (condition != null) {
            stringBuilder.append(condition.toString() + ";");
        }
        return stringBuilder;
    }

    protected static List<StringBuilder> getDelete(TableModule...tableModules) throws Exception {
        List<StringBuilder> moduleList = new ArrayList<>();
        if (tableModules != null && tableModules.length > 0) {
            for (TableModule obj : tableModules) {
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
