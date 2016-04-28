package com.janyee.bladea.Dao.Factory;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kmlixh on 2016/4/28.
 */
public abstract class DataOpenHelperFactory {
    public abstract SQLiteOpenHelper getOpenHelper(Context context);
}
