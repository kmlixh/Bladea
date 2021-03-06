package com.janyee.bladea.Dao.Util;

import android.database.Cursor;

import com.janyee.bladea.Dao.Module.CellModule;
import com.janyee.bladea.Dao.SqlDataType;

import java.lang.reflect.Field;
import java.text.ParseException;

/**
 * Created by kmlixh on 2015/9/22.
 */
public abstract  class  IDataParser<T> {
    public abstract Object read(Cursor cursor, CellModule cell) throws ParseException, Exception;
    public abstract SqlDataType getDataType(Field boundField);
    public abstract Object write(T object);
}
