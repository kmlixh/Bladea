package com.janyee.bladea.Dao;

/**
 * Created by Administrator on 2014/12/2.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.janyee.bladea.Tools.FileManager;
import com.janyee.bladea.Tools.Md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    //数据库版本
    private static final int VERSION = 1;
    private static DBHelper instance = null;
    private static DBHelper instanceConfig = null;
    private Context context;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public static String getDbName(Context context) {
        String packs = context.getPackageName();
        String fileName = "bladea" + Md5.getMd5(packs + "bladea").substring(0, 10) + ".sqlite";
        return fileName;
    }
    public static String getConfigDbName(Context context){
        String packs = context.getPackageName();
        String fileName = "config" + Md5.getMd5(packs + "config").substring(0, 10) + ".sqlite";
        return fileName;
    }

    private DBHelper(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, name, factory, version);
        this.context = context;
    }
    private DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
        this.context = context;
    }

    private DBHelper(Context context, String name) {
        this(context, name, VERSION);
        this.context = context;
    }

    private DBHelper(Context context) {
        super(context, getDbName(context), null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public static DBHelper getConfigInstance(Context context){
        if (instanceConfig == null) {
            instanceConfig = new DBHelper(context,getConfigDbName(context));
        }
        return instanceConfig;
    }

    public static DBHelper initInstanceFromAssets(String fileName, Context context, boolean delExsit) {
        String aa = context.getDatabasePath(getDbName(context)).getPath();
        File ff = new File(aa);
        if (delExsit && ff.exists()) {
            ff.delete();
        }
        try {
            FileManager.PrepareDir(aa);
            ff.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStream ips;
        OutputStream ops;
        try {
            ops = new FileOutputStream(aa);
            ips = context.getAssets().open(fileName);
            byte[] buffer = new byte[1024];

            int length;

            while ((length = ips.read(buffer)) > 0) {

                ops.write(buffer, 0, length);

            }
            ops.flush();
            ops.close();
            ips.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return DBHelper.getInstance(context);
    }

    public static DBHelper initInstanceFromRaw(int resourceId, Context context, boolean delExsit) {
        String aa = context.getDatabasePath(getDbName(context)).getPath();
        File ff = new File(aa);
        if (delExsit && ff.exists()) {
            ff.delete();
        }
        try {
            FileManager.PrepareDir(aa);
            ff.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStream ips;
        OutputStream ops;
        try {
            ops = new FileOutputStream(aa);
            ips = context.getResources().openRawResource(resourceId);
            byte[] buffer = new byte[1024];
            int length = 10;

            while ((length = ips.read(buffer)) > 0) {

                ops.write(buffer, 0, length);

            }
            ops.flush();
            ops.close();
            ips.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return DBHelper.getInstance(context);
    }

    public static DBHelper initInstanceFromSdCard(String path, Context context, boolean delExsit) {
        String aa = context.getDatabasePath(getDbName(context)).getPath();
        File fs = new File(path);
        File ff = new File(aa);
        if (!FileManager.isSameFile(path, aa)) {
            if (delExsit && ff.exists()) {
                ff.delete();
            }
            try {
                FileManager.PrepareDir(aa);
                ff.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            InputStream ips;
            OutputStream ops;
            try {
                ops = new FileOutputStream(aa);
                ips = new FileInputStream(path);
                byte[] buffer = new byte[1024];
                int length = 10;

                while ((length = ips.read(buffer)) > 0) {

                    ops.write(buffer, 0, length);

                }
                ops.flush();
                ops.close();
                ips.close();
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        return DBHelper.getInstance(context);
    }
}