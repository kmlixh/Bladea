package com.janyee.bladea.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Module.TableModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/29.
 */
class SqliteEngine {
    Context context;

    protected SqliteEngine(Context context) {
        this.context = context;
    }

    protected int Merge(TableModule module, StringBuilder sql) {
        int result = 0;
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        try {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            SQLiteStatement statement = database.compileStatement(sql.toString());
            result = statement.executeUpdateDelete();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        } finally {
            dbHelper.close();
        }
        return result;
    }

    protected long Count(TableModule module, Condition condition) {
        long result = 0;
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        try {
            StringBuilder sql = SqlFactory.getCount(module.getTableName(), condition);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            SQLiteStatement statement = database.compileStatement(sql.toString());
            result = statement.simpleQueryForLong();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        } finally {
            dbHelper.close();
        }
        return result;
    }

    protected int TransactionMerge(TableModule module, List<StringBuilder> sqls) {
        int count = 0;
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        SQLiteDatabase database = null;
        try {
            database = dbHelper.getWritableDatabase();
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

    protected void fastMerge(TableModule module, StringBuilder sql) {
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        try {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            SQLiteStatement statement = database.compileStatement(sql.toString());
            statement.execute();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        } finally {
            dbHelper.close();
        }
    }

    protected void FastTransactionMerge(TableModule module, StringBuilder... sqls) {
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        SQLiteDatabase database = null;
        try {
            database = dbHelper.getWritableDatabase();
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

    protected boolean  checkTableExsist(TableModule module){
        boolean result = false;
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        SQLiteDatabase database = null;
        try {
            database = dbHelper.getWritableDatabase();
            long count;
            String sql = "select count(*) from sqlite_master where type='table' and name ='" + module.getTableName() + "';";
            SQLiteStatement statement = database.compileStatement(sql);
            count = statement.simpleQueryForLong();
            if (count == 1) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return result;
    }

    protected <T> List<T> query(TableModule<T> module, StringBuilder sql) throws Exception {
        SQLiteOpenHelper dbHelper = module.getFactory().getOpenHelper(context);
        List<T> tlist = new ArrayList<T>();
        SQLiteDatabase database = null;
        try {
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql.toString(), null);
            while (cursor.moveToNext()) {
                T temp = module.bindValue(cursor);
                if (temp != null) {
                    tlist.add(temp);
                } else {
                    throw new DaoException("Errors when querying data and instance object");
                }
            }
            cursor.close();
        } catch (Exception e) {
            throw e;
        } finally {
            dbHelper.close();
        }
        return tlist;
    }
}
