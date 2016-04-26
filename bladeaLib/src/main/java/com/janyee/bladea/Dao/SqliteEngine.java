package com.janyee.bladea.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Module.LinkModule;
import com.janyee.bladea.Dao.Module.TableModule;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/29.
 */
class SqliteEngine {
    Context context;
    SQLiteOpenHelper dbHelper;
    protected SqliteEngine(Context context,SQLiteOpenHelper dbHelper) {
        this.context = context;
        if(dbHelper!=null){
            this.dbHelper=dbHelper;
        }else{
            this.dbHelper=DBHelper.getInstance(context);
        }
    }

    protected int Merge(StringBuilder sql){
        return Merge(dbHelper,sql);
    }
    protected int Merge(SQLiteOpenHelper dbHelper,StringBuilder sql) {
        int result = 0;
        try {
            SQLiteDatabase database=dbHelper.getWritableDatabase();
            SQLiteStatement statement = database.compileStatement(sql.toString());
            result = statement.executeUpdateDelete();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }finally {
            dbHelper.close();
        }
        return result;
    }
    protected long Count(StringBuilder sql){
        return Count(dbHelper,sql);
    }
    protected long Count(SQLiteOpenHelper dbHelper,StringBuilder sql) {
        long result = 0;
        try {
            SQLiteDatabase database=dbHelper.getWritableDatabase();
            SQLiteStatement statement = database.compileStatement(sql.toString());
            result =  statement.simpleQueryForLong();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }finally {
            dbHelper.close();
        }
        return result;
    }
    protected int TransactionMerge(List<StringBuilder> sqls){
        return TransactionMerge(dbHelper,sqls);
    }
    protected int TransactionMerge(SQLiteOpenHelper dbHelper,List<StringBuilder> sqls) {
        int count = 0;
        SQLiteDatabase database=null;
        try {
            database=dbHelper.getWritableDatabase();
            database.beginTransaction();
            for (StringBuilder sql : sqls) {
                SQLiteStatement sqLiteStatement = database.compileStatement(sql.toString());
                count += sqLiteStatement.executeUpdateDelete();
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            database.endTransaction();
            dbHelper.close();
        }
        return count;
    }
    protected void fastMerge(StringBuilder sql) {
        fastMerge(dbHelper,sql);
    }
    protected void fastMerge(SQLiteOpenHelper dbHelper,StringBuilder sql) {
        try {
            SQLiteDatabase database=dbHelper.getWritableDatabase();
            SQLiteStatement statement = database.compileStatement(sql.toString());
            statement.execute();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }finally {
            dbHelper.close();
        }
    }
    protected void FastTransactionMerge(StringBuilder... sqls) {
        FastTransactionMerge(dbHelper,sqls);
    }
    protected void FastTransactionMerge(SQLiteOpenHelper dbHelper,StringBuilder... sqls) {
        SQLiteDatabase database=null;
        try {
            database=dbHelper.getWritableDatabase();
            database.beginTransaction();
            for (StringBuilder sql : sqls) {
                SQLiteStatement sqLiteStatement = database.compileStatement(sql.toString());
                sqLiteStatement.execute();
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            database.endTransaction();
            dbHelper.close();
        }
    }
    protected boolean checkTableExsist(String tableName) throws Exception {
        return checkTableExsist(dbHelper,tableName);
    }
    protected boolean checkTableExsist(SQLiteOpenHelper dbHelper,String tableName) throws Exception {
        boolean result = false;
        SQLiteDatabase database=null;
        try {
            database=dbHelper.getWritableDatabase();
            long count;
                String sql = "select count(*) from sqlite_master where type='table' and name ='"+tableName+"';";
                SQLiteStatement statement = database.compileStatement(sql);
                count = statement.simpleQueryForLong();
            if (count == 1) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            dbHelper.close();
        }
        return result;
    }
    protected <T> boolean checkTableExsist(Class<T> tClass) throws Exception {
        return checkTableExsist(dbHelper,tClass);
    }
    protected <T> boolean checkTableExsist(SQLiteOpenHelper dbHelper,Class<T> tClass) throws Exception {
        return checkTableExsist(dbHelper,SqlFactory.getTableModule(tClass).getTableName());
    }
    protected <T> List<T> query(Class<T> tClass, StringBuilder sql, boolean queryLink) throws Exception {
        return query(dbHelper,tClass,sql,queryLink);
    }
    protected <T> List<T> query(SQLiteOpenHelper dbHelper,Class<T> tClass, StringBuilder sql, boolean queryLink) throws Exception {
        List<T> tlist = new ArrayList<T>();
        SQLiteDatabase database=null;
        try{
            database=dbHelper.getReadableDatabase();
            TableModule<T> tableModule = SqlFactory.getTableModule(tClass);
            Cursor cursor = database.rawQuery(sql.toString(), null);
            while (cursor.moveToNext()) {
                T temp = tableModule.bindValue(cursor);
                if (temp != null) {
                    tlist.add(temp);
                } else {
                    throw new DaoException("Errors when querying data and instance object");
                }
            }
            cursor.close();
            if(queryLink&&tableModule.getLinkMap().size()>0){
                for(T t:tlist){
                    for(LinkModule<T> module:tableModule.getLinkMap().values()){
                        StringBuilder sql_link;
                        if(module.getBoundField().getType().equals(Array.newInstance(module.getBoundClass(), 0).getClass())||module.getBoundField().getType().newInstance() instanceof List){//判断是否为List或者数组
                            sql_link=SqlFactory.getLinkQuery(t,module);
                            List links=query(module.getBoundClass(),sql_link,true);
                            if(module.getBoundField().getType().equals(Array.newInstance(module.getBoundClass(), 0).getClass())){
                                module.bindField(t,links.toArray());
                            }else{
                                module.bindField(t,links);
                            }
                        }else{
                            sql_link =SqlFactory.getLinkFetch(t,module);
                            List links=query(module.getBoundClass(),sql_link,true);
                            module.bindField(t,links.get(0));
                        }
                    }
                }
            }
        }catch (Exception e){
            throw  new DaoException(e.getMessage());
        }finally {
            dbHelper.close();
        }
        return tlist;
    }
}
