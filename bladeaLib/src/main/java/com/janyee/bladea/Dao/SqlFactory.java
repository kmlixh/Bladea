package com.janyee.bladea.Dao;

import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Module.LinkModule;
import com.janyee.bladea.Dao.Module.TableModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kmlixh on 2015/10/8.
 */
public class SqlFactory {
    static Map<Class, TableModule> tableMap = null;
    static Map<String, TableModule> tableMap2 = null;

    public static Map<Class, TableModule> getTableMap() {
        return tableMap;
    }

    public static <T> TableModule<T> getTableModule(Class<T> tClass) throws Exception {
        if (tableMap == null) {
            tableMap = new HashMap<>();
            tableMap2 = new HashMap<>();
        }

        TableModule module = tableMap.get(tClass);
        if (module == null) {
            module = new TableModule(tClass);
            tableMap.put(tClass, module);
            if (tableMap2.get(module.getTableName()) != null) {
                throw new DaoException("Dumplicate Table '" + module.getTableName() + "' was find!");
            } else {
                tableMap2.put(module.getTableName(), module);
            }
        }
        return module;
    }

    public static <T> TableModule<T> getTableModule(T t) throws Exception {
        TableModule module = getTableModule(t.getClass());
        module.setBoundValue(t);
        return module;
    }

    public static TableModule getTableModule(String tableName) {
        if (tableMap2 != null) {
            TableModule module = tableMap2.get(tableName);
            return module;
        } else {
            return null;
        }
    }

    protected static <T> StringBuilder getLinkQuery(T t, LinkModule<T> linkModule) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(linkModule.getTableName() + "WHERE " + linkModule.getPrimaryCell().getCellName() + "='" + linkModule.getParent().getFieldValue(t, linkModule.getField()) + "';");
        List<Object> values = new ArrayList<>();
        values.add(linkModule.getParent().getFieldValue(t, linkModule.getField()));
        return stringBuilder;
    }

    protected static <T> StringBuilder getLinkFetch(T t, LinkModule<T> linkModule) throws Exception {
        StringBuilder stringBuilder = getLinkQuery(t, linkModule);
        stringBuilder.replace(stringBuilder.lastIndexOf(";"), stringBuilder.length(), "").append("LIMIT 0,1");
        return stringBuilder;
    }

    protected static <T> StringBuilder getQuery(Class<T> tClass) throws Exception {
        return getQuery(tClass, null);
    }
    protected static <T> StringBuilder getQuery(Class<T> tClass, Condition condition) throws Exception {
        TableModule tableModule = getTableModule(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append("["+tableModule.getTableName()+"]");
        if (condition != null) {
            stringBuilder.append(condition.toString());
        }
        stringBuilder.append(";");
        return stringBuilder;

    }

    protected static <T> StringBuilder getFetch(Class<T> tClass, Condition condition) throws Exception {
        condition.Pager(0, 1);
        return getQuery(tClass, condition);
    }

    protected static StringBuilder getFetch(Object obj) throws Exception {
        Condition condition = Condition.getPrimaryCondition(obj);
        condition.Pager(0, 1);
        return getQuery(obj.getClass(), condition);
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
            TableModule tableModule = getTableModule(obj);
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

    protected static <T> StringBuilder getDelete(Class<T> tClass) throws Exception {
        return getDelete(tClass, null);
    }

    protected static <T> StringBuilder getDelete(Class<T> tClass, Condition condition) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        TableModule tableModule = getTableModule(tClass);
        stringBuilder.append("DELETE FROM [" + tableModule.getTableName() + "] ");
        if (condition != null) {
            stringBuilder.append(condition.toString() + ";");
        }
        return stringBuilder;
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
            for (Object obj : objs) {
                moduleList.add(getDelete(obj));
            }
        }
        return moduleList;
    }

    protected static <T> StringBuilder getCreate(Class<T> tClass) throws Exception {
        TableModule tableModule = getTableModule(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE [" + tableModule.getTableName() + "] (");
        stringBuilder.append(tableModule.getCreateList() + ") ");
        return stringBuilder;
    }

    //CREATE TABLE IF NOT EXISTS
    protected static <T> StringBuilder getCreateIfNotExists(Class<T> tClass) throws Exception {
        TableModule tableModule = getTableModule(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS [" + tableModule.getTableName() + "] (");
        stringBuilder.append(tableModule.getCreateList() + ") ");
        return stringBuilder;
    }

    protected static <T> StringBuilder getDrop(Class<T> tClass) throws Exception {
        TableModule tableModule = getTableModule(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE IF EXISTS [" + tableModule.getTableName() + "];");
        return stringBuilder;
    }

    protected static StringBuilder getDrop(String table) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE IF EXISTS [" + table + "];");
        return stringBuilder;
    }

}
