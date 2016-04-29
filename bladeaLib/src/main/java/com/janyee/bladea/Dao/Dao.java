package com.janyee.bladea.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Module.LinkModule;
import com.janyee.bladea.Dao.Module.TableModule;
import com.janyee.bladea.Dao.Pojo.TableVersion;
import com.janyee.bladea.Dao.Cache.CacheInfo;
import com.janyee.bladea.Dao.annotation.Link;

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

    private Dao(Context context, DaoInitOptions options, Class[] classes) {
        this.context = context;
        if (options != null) {
            this.options = options;
        } else {
            this.options = DaoInitOptions.getInstance();
        }
        sqliteEngine = new SqliteEngine(context);
        try {
            if (!sqliteEngine.checkTableExsist(SqlFactory.getTableModule(TableVersion.class))) {
                create(TableVersion.class);
            }
            if (SqlFactory.versionMap == null || SqlFactory.versionMap.size() == 0) {
                SqlFactory.versionMap = new HashMap<>();
                List<TableVersion> tableVersionList = query(TableVersion.class, false);
                if (tableVersionList.size() > 0) {
                    for (TableVersion version : tableVersionList) {
                        SqlFactory.versionMap.put(version.getTabs(), version.getVers());
                    }
                }
            }
            init(classes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dao getInstance(Context context) {
        return new Dao(context, null, null);
    }

    public static Dao getInstance(Context context, DaoInitOptions options, Class... classes) throws Exception {
        return new Dao(context, options, classes);
    }

    public void preCachingClass(Class... classes) throws Exception {
        init(classes);
    }

    private synchronized void init(Class... classes) throws Exception {

        if (classes == null) {
            return;
        }
        for (Class classz : classes) {
            if (!classz.equals(TableVersion.class)) {
                TableModule module = SqlFactory.getTableModule(classz);
                if (sqliteEngine.checkTableExsist(module)) {
                    if (SqlFactory.versionMap.get(module.getTableName()) != null && !SqlFactory.versionMap.get(module.getTableName()).equals(module.getMd5()) && options.isAutoUpdateTableStructure()) {
                        List info = sqliteEngine.query(module, SqlFactory.getQuery(classz), false);
                        dropTable(classz);
                        create(module.getBoundClass());
                        save(info);

                    } else {
                        TableVersion version = new TableVersion();
                        version.setVers(module.getMd5());
                        version.setTabs(module.getTableName());
                        save(version);
                    }
                } else {
                    create(module.getBoundClass());
                    TableVersion version = new TableVersion();
                    version.setTabs(module.getTableName());
                    version.setVers(module.getMd5());
                    save(version);
                }
                SqlFactory.versionMap.put(module.getTableName(), module.getMd5());
            }

        }
    }

    public long count(TableModule module, Condition condition) throws Exception {
        return sqliteEngine.Count(module, condition);
    }

    public <T> long count(Class<T> tClass) throws Exception {
        return sqliteEngine.Count(SqlFactory.getTableModule(tClass), null);
    }

    public <T> long count(Class<T> tClass, Condition condition) throws Exception {
        return sqliteEngine.Count(SqlFactory.getTableModule(tClass), condition);
    }

    public <T> T queryLinks(T t) throws Exception {
        TableModule module=SqlFactory.getTableModule(t);
            if(module.getLinkMap().size()>0){
                Map<String,LinkModule> map=module.getLinkMap();
                for(LinkModule temp:map.values()){
                    StringBuilder sql;
                    if(temp.getBoundField().getType().equals(Array.newInstance(temp.getDstModule().getBoundClass(), 0).getClass())||temp.getBoundField().getType().equals(List.class)){
                        sql=SqlFactory.getLinkQuery(t,temp);
                        List result=sqliteEngine.query(temp.getDstModule(),sql,false);
                        temp.getBoundField().setAccessible(true);
                        if(temp.getBoundField().getType().equals(List.class)){
                            temp.getBoundField().set(t,result);
                        }else{
                            Object arrays= Array.newInstance(temp.getDstModule().getBoundClass(), result.size());
                            temp.getBoundField().set(t,result.toArray((Object[]) arrays));
                        }
                    }else if(temp.getBoundField().getType().equals(temp.getDstModule().getBoundClass())){
                        sql=SqlFactory.getLinkFetch(t,temp);
                        List result=sqliteEngine.query(temp.getDstModule(),sql,false);
                        temp.getBoundField().setAccessible(true);
                        temp.getBoundField().set(t,result.get(0));
                    }
                }
            }
        return  t;
    }

    public <T> List<T> query(Class<T> tClass, Condition condition, boolean queryLink) throws Exception {
        if (sqliteEngine.checkTableExsist(SqlFactory.getTableModule(tClass))) {
            StringBuilder sb = SqlFactory.getQuery(tClass, condition);
            return sqliteEngine.query(SqlFactory.getTableModule(tClass), sb, queryLink);
        } else {
            create(tClass);
            return new ArrayList<>();
        }
    }

    public <T> List<T> query(Class<T> tClass, boolean queryLink) throws Exception {
        if (sqliteEngine.checkTableExsist(SqlFactory.getTableModule(tClass))) {
            StringBuilder sb = SqlFactory.getQuery(tClass);
            return sqliteEngine.query(SqlFactory.getTableModule(tClass), sb, queryLink);
        } else {
            create(tClass);
            return new ArrayList<>();
        }
    }

    public <T> T fetch(Class<T> tClass, Condition condition, boolean queryLink) throws Exception {
        if (sqliteEngine.checkTableExsist(SqlFactory.getTableModule(tClass))) {
            StringBuilder sb = SqlFactory.getQuery(tClass, condition);
            List<T> tList = sqliteEngine.query(SqlFactory.getTableModule(tClass), sb, queryLink);
            if (tList.size() > 0) {
                return tList.get(0);
            } else {
                return null;
            }
        } else {
            create(tClass);
            return null;
        }
    }

    public <T> int insert(T t) throws Exception {
        if (t != null) {
            init(t.getClass());
            StringBuilder sb = SqlFactory.getInsert(t);
            return sqliteEngine.Merge(SqlFactory.getTableModule(t.getClass()), sb);
        } else {
            return 0;
        }
    }

    public <T> int insert(T[] ts) throws Exception {
        if (ts != null && ts.length > 0) {
            init(ts[0].getClass());
            List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
            return sqliteEngine.TransactionMerge(SqlFactory.getTableModule(ts[0].getClass()), stringBuilders);
        } else {
            return 0;
        }
    }

    public <T> int insert(List<T> ts) throws Exception {
        if (ts != null && ts.size() > 0) {
            init(ts.get(0).getClass());
            List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
            return sqliteEngine.TransactionMerge(SqlFactory.getTableModule(ts.get(0).getClass()), stringBuilders);
        } else {
            return 0;
        }
    }


    public <T> int save(T t) throws Exception {
        if (t != null) {
            init(t.getClass());
            StringBuilder sb = SqlFactory.getSave(t);
            return sqliteEngine.Merge(SqlFactory.getTableModule(t.getClass()), sb);
        } else {
            return 0;
        }
    }

    public <T> int save(List<T> tList) throws Exception {
        if (tList != null && tList.size() > 0) {
            init(tList.get(0).getClass());
            List<StringBuilder> builders = SqlFactory.getSave(tList.toArray());
            return sqliteEngine.TransactionMerge(SqlFactory.getTableModule(tList.get(0).getClass()), builders);
        } else {
            return 0;
        }
    }

    public <T> int save(T[] ts) throws Exception {
        if (ts != null && ts.length > 0) {
            init(ts[0].getClass());
            List<StringBuilder> builders = SqlFactory.getSave(ts);
            return sqliteEngine.TransactionMerge(SqlFactory.getTableModule(ts[0].getClass()), builders);
        } else {
            return 0;
        }
    }

    public <T> int delete(Class<T> tClass, Condition condition) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getDelete(tClass, condition);
        return sqliteEngine.Merge(SqlFactory.getTableModule(tClass), sb);
    }

    public <T> int delete(Class<T> tClass) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getDelete(tClass);
        return sqliteEngine.Merge(SqlFactory.getTableModule(tClass), sb);
    }

    public <T> int delete(T t) throws Exception {
        init(t.getClass());
        StringBuilder sb = SqlFactory.getDelete(t);
        return sqliteEngine.Merge(SqlFactory.getTableModule(t.getClass()), sb);
    }

    public <T> int delete(List<T> tList) throws Exception {
        if (tList != null && tList.size() > 0) {
            init(tList.get(0).getClass());
            T[] builders = (T[]) tList.toArray();
            return delete(builders);
        } else {
            return 0;
        }
    }


    public <T> int delete(T[] tList) throws Exception {
        if (tList != null && tList.length > 0) {
            init(tList[0].getClass());
            List<StringBuilder> moduleList = SqlFactory.getDelete(tList);
            return sqliteEngine.TransactionMerge(SqlFactory.getTableModule(tList[0].getClass()), moduleList);
        } else {
            return 0;
        }
    }

    public <T> int dropTable(Class<T> tClass) throws Exception {
        StringBuilder sqlModuleList = SqlFactory.getDrop(tClass);
        return sqliteEngine.Merge(SqlFactory.getTableModule(tClass), sqlModuleList);
    }


    public int create(Class tClass) throws Exception {
        StringBuilder sb = SqlFactory.getCreate(tClass);
        return sqliteEngine.Merge(SqlFactory.getTableModule(tClass), sb);
    }


    public int createIfNotExist(Class tClass) throws Exception {
        StringBuilder sm = SqlFactory.getCreateIfNotExists(tClass);
        return sqliteEngine.Merge(SqlFactory.getTableModule(tClass), sm);
    }


}
