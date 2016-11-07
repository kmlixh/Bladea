package com.janyee.bladea.Dao;

import android.content.Context;

import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Module.LinkModule;
import com.janyee.bladea.Dao.Module.TableModule;
import com.janyee.bladea.Dao.Pojo.TableVersion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kmlixh on 14/11/1.
 */
public class Dao {
    private SqliteEngine sqliteEngine;
    private DaoInitOptions options;
    private Context context;
    private static Map<Class, TableModule> tableMap = null;
    private static Map<String, TableModule> tableMap2 = null;
    private static Map<String, String> versionMap;



    private <T> TableModule<T> getTableModule(Class<T> tClass) {
        if (tableMap == null) {
            tableMap = new HashMap<>();
            tableMap2 = new HashMap<>();
        }
        if(tClass.equals(TableVersion.class)){
            TableModule<T> module=null;
            try {
                module=new TableModule(tClass);
            }catch (Exception e){

            }
            return module;
        }else{
            TableModule<T> module=tableMap.get(tClass);
            if(module!=null){
                return module;
            }else{
                try{
                    module=new TableModule(tClass);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                if(versionMap.containsKey(module.getTableName())){
                    if(versionMap.get(module.getTableName()).equals(module.getMd5())){//版本库中存在对应的表，且表结构相同
                        return module;
                    }else{//表结构不同，需要先取出数据
                        try {
                            List list=query(module,null);
                            dropTable(module);
                            create(module);
                            insert(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cacheTable(module);
                        return module;
                    }
                }else {//不存在数据，则说明此表是新表，需要新建
                    create(module);
                    cacheTable(module);
                    return module;
                }
            }
        }
    }
    private void cacheTable(TableModule module){
        versionMap.put(module.getTableName(),module.getMd5());
        tableMap.put(module.getBoundClass(),module);
        tableMap2.put(module.getTableName(),module);
        TableVersion version=new TableVersion(module.getTableName(),module.getMd5());
        save(version);
    }

    private Dao(Context context, DaoInitOptions options) {
        this.context = context;
        if (options != null) {
            this.options = options;
        } else {
            this.options = DaoInitOptions.getInstance();
        }
        sqliteEngine = new SqliteEngine(context);
        try {
            if (!sqliteEngine.checkTableExsist(getTableModule(TableVersion.class))) {
                create(TableVersion.class);
            }else if (versionMap == null || versionMap.size() == 0) {
                versionMap = new HashMap<>();
                List<TableVersion> tableVersionList = query(TableVersion.class);
                if (tableVersionList.size() > 0) {
                    for (TableVersion version : tableVersionList) {
                        versionMap.put(version.getTabs(), version.getVers());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dao getInstance(Context context) {
        return new Dao(context, null);
    }

    public static Dao getInstance(Context context, DaoInitOptions options) {
        return new Dao(context, options);
    }


    public long count(TableModule module, Condition condition) {
        try {
            return sqliteEngine.Count(module, condition);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    public <T> long count(Class<T> tClass){
        try {
            return sqliteEngine.Count(getTableModule(tClass), null);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    public <T> long count(Class<T> tClass, Condition condition){
        try {
            return sqliteEngine.Count(getTableModule(tClass), condition);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public <T> T queryLinks(T t) throws Exception {
        try {
            TableModule module=getTableModule(t.getClass());
            if(module.getLinkMap().size()>0){
                Map<String,LinkModule> map=module.getLinkMap();
                for(LinkModule temp:map.values()){
                    StringBuilder sql;
                    if(temp.getBoundField().getType().equals(Array.newInstance(temp.getDstModule().getBoundClass(), 0).getClass())||temp.getBoundField().getType().equals(List.class)){
                        sql=SqlFactory.getLinkQuery(t,temp);
                        List result=sqliteEngine.query(temp.getDstModule(),sql);
                        temp.getBoundField().setAccessible(true);
                        if(temp.getBoundField().getType().equals(List.class)){
                            temp.getBoundField().set(t,result);
                        }else{
                            Object arrays= Array.newInstance(temp.getDstModule().getBoundClass(), result.size());
                            temp.getBoundField().set(t,result.toArray((Object[]) arrays));
                        }
                    }else if(temp.getBoundField().getType().equals(temp.getDstModule().getBoundClass())){
                        sql=SqlFactory.getLinkFetch(t,temp);
                        List result=sqliteEngine.query(temp.getDstModule(),sql);
                        temp.getBoundField().setAccessible(true);
                        temp.getBoundField().set(t,result.get(0));
                    }
                }
            }
            return  t;
        }catch (Exception e){
            e.printStackTrace();
            return t;
        }

    }
    public <T> List<T> query(TableModule<T> tableModule,Condition condition){
        try {
            if (sqliteEngine.checkTableExsist(tableModule)) {
                StringBuilder sb = SqlFactory.getQuery(tableModule, condition);
                return sqliteEngine.query(tableModule, sb);
            } else {
                return new ArrayList<>();
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
    public <T> List<T> query(Class<T> tClass, Condition condition)  {
        return query(getTableModule(tClass),condition);
    }

    public <T> List<T> query(Class<T> tClass) {
        return query(tClass,null);
    }
    private <T> T fetch(TableModule<T> tableModule,Object id) {
        try {
            if (sqliteEngine.checkTableExsist(tableModule)) {
                StringBuilder sb = SqlFactory.getFetch(tableModule,id);
                List<T> tList = sqliteEngine.query(tableModule, sb);
                if (tList.size() > 0) {
                    return tList.get(0);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public <T> T fetch(Class<T> tClass,String id) {
        TableModule<T> tableModule=getTableModule(tClass);
        return fetch(tableModule,id);
    }
    public <T> T fetch(Class<T> tClass,int id)  {
        try {
            TableModule<T> tableModule=getTableModule(tClass);
            return fetch(tableModule,id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public <T> int insert(T t) {
        try {
            if (t != null) {
                StringBuilder sb = SqlFactory.getInsert(t);
                return sqliteEngine.Merge(getTableModule(t.getClass()), sb);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    public <T> int insert(T[] ts) {
        try {
            if (ts != null && ts.length > 0) {
                List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
                return sqliteEngine.TransactionMerge(getTableModule(ts[0].getClass()), stringBuilders);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    public <T> int insert(List<T> ts) {
        try {
            if (ts != null && ts.size() > 0) {
                List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
                return sqliteEngine.TransactionMerge(getTableModule(ts.get(0).getClass()), stringBuilders);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }


    public <T> int save(T t) {
       try{
           if (t != null) {
               StringBuilder sb = SqlFactory.getSave(t);
               return sqliteEngine.Merge(getTableModule(t.getClass()), sb);
           } else {
               return 0;
           }
       }catch (Exception e){
           e.printStackTrace();
           return -1;
       }
    }

    public <T> int save(List<T> tList) {
        try{
            if (tList != null && tList.size() > 0) {
                List<StringBuilder> builders = SqlFactory.getSave(tList.toArray());
                return sqliteEngine.TransactionMerge(getTableModule(tList.get(0).getClass()), builders);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public <T> int save(T[] ts){
        try{
            if (ts != null && ts.length > 0) {
                List<StringBuilder> builders = SqlFactory.getSave(ts);
                return sqliteEngine.TransactionMerge(getTableModule(ts[0].getClass()), builders);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public <T> int delete(Class<T> tClass,String id)
    {
        try{
            TableModule module=getTableModule(tClass);
            Condition condition=Condition.getPrimaryCondition(module,id);
            return delete(module,condition);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public  int delete(Class tClass,int id){
        try{
            TableModule module=getTableModule(tClass);
            Condition condition=Condition.getPrimaryCondition(module,id);
            return delete(module,condition);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public int delete(TableModule module, Condition condition){

        try{
            StringBuilder sb = SqlFactory.getDelete(module, condition);
            return sqliteEngine.Merge(module, sb);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public <T> int delete(Class<T> tClass) {
        try{
            StringBuilder sb = SqlFactory.getDelete(tClass);
            return sqliteEngine.Merge(getTableModule(tClass), sb);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public <T> int delete(T t) {
        try{
            StringBuilder sb = SqlFactory.getDelete(t);
            return sqliteEngine.Merge(getTableModule(t.getClass()), sb);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public <T> int delete(List<T> tList){

        try{
            if (tList != null && tList.size() > 0) {
                T[] builders = (T[]) tList.toArray();
                return delete(builders);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    public <T> int delete(T[] tList) {
        try{
            if (tList != null && tList.length > 0) {
                List<StringBuilder> moduleList = SqlFactory.getDelete(tList);
                return sqliteEngine.TransactionMerge(getTableModule(tList[0].getClass()), moduleList);
            } else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public <T> int dropTable(Class<T> tClass){
        TableModule module=getTableModule(tClass);
        return dropTable(module);
    }
    public int dropTable(TableModule module){
        StringBuilder sql = SqlFactory.getDrop(module);
        return sqliteEngine.Merge(module, sql);

    }


    public int create(Class tClass)  {
        return create(getTableModule(tClass));
    }
    public int create(TableModule module)  {
        try{
            StringBuilder sb = SqlFactory.getCreate(module);
            return sqliteEngine.Merge(module, sb);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    public int createIfNotExist(Class tClass){
        try{
            StringBuilder sm = SqlFactory.getCreateIfNotExists(getTableModule(tClass));
            return sqliteEngine.Merge(getTableModule(tClass), sm);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


}
