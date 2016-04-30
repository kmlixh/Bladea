package com.janyee.bladea.Dao.Factory.extds;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.janyee.bladea.Dao.DBHelper;
import com.janyee.bladea.Dao.Factory.DataOpenHelperFactory;

/**
 * Created by kmlixh on 2016/4/28.
 */
public class ConfigDOHelperFactory extends DataOpenHelperFactory {
    @Override
    public SQLiteOpenHelper getOpenHelper(Context context) {
        return DBHelper.getConfigInstance(context);
    }
}
