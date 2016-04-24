package com.janyee.bladea.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Module.TableModule;
import com.janyee.bladea.Dao.Pojo.TableVersion;
import com.janyee.bladea.POJO.CacheModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by kmlixh on 14/11/1.
 */
public class Dao {
    private SqliteEngine sqliteEngine;
    private DaoInitOptions options;
    private Map<String, String> versionMap;
    private Context context;

    private Dao(Context context, DaoInitOptions options, Class... classes){
        this.context = context;
        this.options = options;
        if(options!=null){
            sqliteEngine = new SqliteEngine(context, options.getDbHelper());
        }else{
            sqliteEngine=new SqliteEngine(context,null);
        }
        try{
            init(classes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Dao getInstance(Context context) {
        return new Dao(context,null,null);
    }

    public static Dao getInstance(Context context, DaoInitOptions options, Class... classes) throws Exception {
       return new Dao(context, options, classes);
    }

    public void preCachingClass(Class... classes) throws Exception {
        init(classes);
    }

    private void init(Class... classes) throws Exception {
        if (!sqliteEngine.checkTableExsist(DBHelper.getConfigInstance(context), TableVersion.class)) {
            create(DBHelper.getConfigInstance(context), TableVersion.class);
        }
        if (versionMap == null) {
            versionMap = new HashMap<>();
            List<TableVersion> tableVersionList = query(DBHelper.getConfigInstance(context), TableVersion.class, false);
            if (tableVersionList.size() > 0) {
                for (TableVersion version : tableVersionList) {
                    versionMap.put(version.getTable(), version.getVersion());
                }
            }
        }
        if(classes==null){
            return;
        }
        for (Class classz : classes) {
            TableModule module = SqlFactory.getTableModule(classz);
            if (!classz.equals(TableVersion.class) && !classz.equals(CacheModule.class)) {
                if (sqliteEngine.checkTableExsist(module.getBoundClass())) {
                    if (versionMap.get(module.getTableName()) != null && !versionMap.get(module.getTableName()).equals(module.getMd5()) && options.isAutoUpdateTableStructure()) {
                        List info = query(module.getBoundClass(), false);
                        dropTable(module.getTableName());
                        create(module.getBoundClass());
                        save(info);
                    } else {
                        TableVersion version = new TableVersion();
                        version.setVersion(module.getMd5());
                        version.setTable(module.getTableName());
                        save(DBHelper.getConfigInstance(context), version);
                    }
                } else {
                    create(module.getBoundClass());
                    TableVersion version = new TableVersion();
                    version.setTable(module.getTableName());
                    version.setVersion(module.getMd5());
                    save(DBHelper.getConfigInstance(context), version);
                }
                versionMap.put(module.getTableName(), module.getMd5());
            }

        }
    }

    public <T> List<T> query(SQLiteOpenHelper helper, Class<T> tClass, Condition condition, boolean queryLink) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getQuery(tClass, condition);
        return sqliteEngine.query(helper, tClass, sb, queryLink);
    }

    public <T> List<T> query(Class<T> tClass, Condition condition, boolean queryLink) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getQuery(tClass, condition);
        return sqliteEngine.query(tClass, sb, queryLink);
    }

    public <T> List<T> query(SQLiteOpenHelper helper, Class<T> tClass, boolean queryLink) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getQuery(tClass);
        return sqliteEngine.query(helper, tClass, sb, queryLink);
    }

    public <T> List<T> query(Class<T> tClass, boolean queryLink) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getQuery(tClass);
        return sqliteEngine.query(tClass, sb, queryLink);
    }

    public <T> T fetch(Class<T> tClass, Condition condition, boolean queryLink) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getFetch(tClass, condition);
        List<T> tList = sqliteEngine.query(tClass, sb, queryLink);
        if (tList.size() > 0) {
            return tList.get(0);
        } else {
            return null;
        }
    }

    public <T> T fetch(SQLiteOpenHelper helper, Class<T> tClass, Condition condition, boolean queryLink) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getFetch(tClass, condition);
        List<T> tList = sqliteEngine.query(helper, tClass, sb, queryLink);
        if (tList.size() > 0) {
            return tList.get(0);
        } else {
            return null;
        }
    }

    public <T> int insert(T t) throws Exception {
        if(t!=null){
            init(t.getClass());
            StringBuilder sb = SqlFactory.getInsert(t);
            return sqliteEngine.Merge( sb);
        }else{
            return 0;
        }
    }

    public <T> int insert(SQLiteOpenHelper helper, T t) throws Exception {
        if(t!=null){
            init(t.getClass());
            StringBuilder sb = SqlFactory.getInsert(t);
            return sqliteEngine.Merge(helper, sb);
        }else{
            return 0;
        }
    }

    public <T> int insert(T[] ts) throws Exception {
        if(ts!=null&&ts.length>0){
            init(ts[0].getClass());
            List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
            return sqliteEngine.TransactionMerge( stringBuilders);
        }else{
            return 0;
        }
    }

    public <T> int insert(SQLiteOpenHelper helper, T[] ts) throws Exception {
        if(ts!=null&&ts.length>0){
            init(ts[0].getClass());
            List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
            return sqliteEngine.TransactionMerge(helper, stringBuilders);
        }else{
            return 0;
        }
    }

    public <T> int insert(List<T> ts) throws Exception {
        if(ts!=null&&ts.size()>0){
            init(ts.get(0).getClass());
            List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
            return sqliteEngine.TransactionMerge( stringBuilders);
        }else{
            return 0;
        }
    }

    public <T> int insert(SQLiteOpenHelper helper, List<T> ts) throws Exception {
        if(ts!=null&&ts.size()>0){
            init(ts.get(0).getClass());
            List<StringBuilder> stringBuilders = SqlFactory.getInsert(ts);
            return sqliteEngine.TransactionMerge(helper, stringBuilders);
        }else{
            return 0;
        }
    }

    public <T> int save(T t) throws Exception {
        if(t!=null){
            init(t.getClass());
            StringBuilder sb = SqlFactory.getSave(t);
            return sqliteEngine.Merge( sb);
        }else{
            return 0;
        }
    }

    public <T> int save(SQLiteOpenHelper helper, T t) throws Exception {
        if(t!=null){
            init(t.getClass());
            StringBuilder sb = SqlFactory.getSave(t);
            return sqliteEngine.Merge(helper, sb);
        }else{
            return 0;
        }
    }

    public <T> int save(List<T> tList) throws Exception {
        if (tList != null && tList.size() > 0) {
            init(tList.get(0).getClass());
            List<StringBuilder> builders = SqlFactory.getSave(tList.toArray());
            return sqliteEngine.TransactionMerge(builders);
        } else {
            return 0;
        }
    }

    public <T> int save(SQLiteOpenHelper helper, List<T> tList) throws Exception {
        if (tList != null && tList.size() > 0) {
            init(tList.get(0).getClass());
            List<StringBuilder> builders = SqlFactory.getSave(tList.toArray());
            return sqliteEngine.TransactionMerge(helper, builders);
        } else {
            return 0;
        }
    }

    public <T> int save(T[] ts) throws Exception {
        if (ts != null && ts.length > 0) {
            init(ts[0].getClass());
            List<StringBuilder> builders = SqlFactory.getSave(ts);
            return sqliteEngine.TransactionMerge(builders);
        } else {
            return 0;
        }
    }

    public <T> int save(SQLiteOpenHelper helper, T[] ts) throws Exception {
        if (ts != null && ts.length > 0) {
            init(ts[0].getClass());
            List<StringBuilder> builders = SqlFactory.getSave(ts);
            return sqliteEngine.TransactionMerge(helper, builders);
        } else {
            return 0;
        }

    }

    public <T> int delete(Class<T> tClass, Condition condition) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getDelete(tClass, condition);
        return sqliteEngine.Merge(sb);
    }

    public <T> int delete(SQLiteOpenHelper helper, Class<T> tClass, Condition condition) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getDelete(tClass, condition);
        return sqliteEngine.Merge(helper, sb);
    }

    public <T> int delete(Class<T> tClass) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getDelete(tClass);
        return sqliteEngine.Merge(sb);
    }

    public <T> int delete(SQLiteOpenHelper helper, Class<T> tClass) throws Exception {
        init(tClass);
        StringBuilder sb = SqlFactory.getDelete(tClass);
        return sqliteEngine.Merge(helper, sb);
    }

    public <T> int delete(T t) throws Exception {
        init(t.getClass());
        StringBuilder sb = SqlFactory.getDelete(t);
        return sqliteEngine.Merge(sb);
    }

    public <T> int delete(SQLiteOpenHelper helper, T t) throws Exception {
        init(t.getClass());
        StringBuilder sb = SqlFactory.getDelete(t);
        return sqliteEngine.Merge(helper, sb);
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

    public <T> int delete(SQLiteOpenHelper helper, List<T> tList) throws Exception {
        if (tList != null && tList.size() > 0) {
            init(tList.get(0).getClass());
            T[] builders = (T[]) tList.toArray();
            return delete(helper, builders);
        } else {
            return 0;
        }
    }

    public <T> int delete(T[] tList) throws Exception {
        if (tList != null && tList.length > 0) {
            init(tList[0].getClass());
            List<StringBuilder> moduleList = SqlFactory.getDelete(tList);
            return sqliteEngine.TransactionMerge(moduleList);
        } else {
            return 0;
        }
    }

    public <T> int delete(SQLiteOpenHelper helper, T[] tList) throws Exception {
        if (tList != null && tList.length > 0) {
            init(tList[0].getClass());
            List<StringBuilder> moduleList = SqlFactory.getDelete(tList);
            return sqliteEngine.TransactionMerge(helper, moduleList);
        } else {
            return 0;
        }
    }

    public int dropTable(String tableName) throws Exception {
        StringBuilder sql = SqlFactory.getDrop(tableName);
        return sqliteEngine.Merge(sql);
    }

    public int dropTable(SQLiteOpenHelper helper, String tableName) throws Exception {
        StringBuilder sql = SqlFactory.getDrop(tableName);
        return sqliteEngine.Merge(helper, sql);
    }

    public <T> int dropTable(Class<T> tClass) throws Exception {
        StringBuilder sqlModuleList = SqlFactory.getDrop(tClass);
        return sqliteEngine.Merge(sqlModuleList);
    }

    public <T> int dropTable(SQLiteOpenHelper helper, Class<T> tClass) throws Exception {
        StringBuilder sqlModuleList = SqlFactory.getDrop(tClass);
        return sqliteEngine.Merge(helper, sqlModuleList);
    }

    public int create(Class tClass) throws Exception {
        StringBuilder sb = SqlFactory.getCreate(tClass);
        return sqliteEngine.Merge(sb);
    }

    public int create(SQLiteOpenHelper helper, Class tClass) throws Exception {
        StringBuilder sb = SqlFactory.getCreate(tClass);
        return sqliteEngine.Merge(helper, sb);
    }

    public int createIfNotExist(Class tClass) throws Exception {
        init(tClass);
        StringBuilder sm = SqlFactory.getCreateIfNotExists(tClass);
        return sqliteEngine.Merge(sm);
    }

    public int createIfNotExist(SQLiteOpenHelper helper, Class tClass) throws Exception {
        init(tClass);
        StringBuilder sm = SqlFactory.getCreateIfNotExists(tClass);
        return sqliteEngine.Merge(helper, sm);
    }

}
