package com.janyee.bladea.Dao.Util.impl;

import android.database.Cursor;

import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Module.CellModule;
import com.janyee.bladea.Dao.SqlDataType;
import com.janyee.bladea.Dao.Tuple.DataTuple;
import com.janyee.bladea.Dao.Util.IDataParser;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kmlixh on 2015/9/22.
 */
public class DataParser extends IDataParser<Object> {


    @Override
    public DataTuple<Object> pullData(Cursor cursor, CellModule cellModule) throws Exception {
        String cellName = cellModule.getCellName();
        int index = cursor.getColumnIndex(cellName);
        if (!cursor.isNull(index)) {
            if (cellModule.getBoundField().getType().equals(String.class) && cursor.getType(index) == Cursor.FIELD_TYPE_STRING) {
                DataTuple tuple = new DataTuple(String.class, cursor.getString(index));
                return tuple;
            }
            if (cellModule.getBoundField().getType().equals(float.class) && cursor.getType(index) == Cursor.FIELD_TYPE_FLOAT) {
                DataTuple tuple = new DataTuple(float.class, cursor.getFloat(index));
                return tuple;
            }
            if (cellModule.getBoundField().getType().equals(double.class) && cursor.getType(index) == Cursor.FIELD_TYPE_FLOAT) {
                DataTuple tuple = new DataTuple(double.class, cursor.getDouble(index));
                return tuple;
            }
            if(cellModule.getBoundField().getType().equals(int.class)&&cursor.getType(index)==Cursor.FIELD_TYPE_INTEGER){
                DataTuple tuple = new DataTuple(int.class, cursor.getInt(index));
                return tuple;
            }
            if(cellModule.getBoundField().getType().equals(long.class)&&cursor.getType(index)==Cursor.FIELD_TYPE_INTEGER){
                DataTuple tuple = new DataTuple(long.class, cursor.getInt(index));
                return tuple;
            }
            if(cellModule.getBoundField().getType().equals(byte.class)&&cursor.getType(index)==Cursor.FIELD_TYPE_INTEGER){
                DataTuple tuple = new DataTuple(byte.class, cursor.getInt(index));
                return tuple;
            }
            if(cellModule.getBoundField().getType().equals(byte[].class)&&cursor.getType(index)==Cursor.FIELD_TYPE_BLOB){
                DataTuple tuple=new DataTuple(byte[].class,cursor.getBlob(index));
                return tuple;
            }
            if (cellModule.getBoundField().getType().equals(Date.class)) {
                if (cursor.getType(index) == Cursor.FIELD_TYPE_STRING) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    DataTuple tuple = new DataTuple(Date.class, dateFormat.parse(cursor.getString(index)));
                    return tuple;
                }
                if (cursor.getType(index) == Cursor.FIELD_TYPE_INTEGER) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(cursor.getLong(index));
                    DataTuple tuple = new DataTuple(Date.class, cal.getTime());
                    return tuple;
                }
            }
            if (cellModule.getBoundField().getType().equals(Calendar.class)) {
                if (cursor.getType(index) == Cursor.FIELD_TYPE_STRING) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateFormat.parse(cursor.getString(index)));
                    DataTuple tuple = new DataTuple(Calendar.class, calendar);
                    return tuple;
                }
                if (cursor.getType(index) == Cursor.FIELD_TYPE_INTEGER) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(cursor.getLong(index));
                    DataTuple tuple = new DataTuple(Calendar.class, cal);
                    return tuple;
                }

            }
            if (cellModule.getBoundField().getType().equals(byte[].class) ) {
                if(cursor.getType(index) == Cursor.FIELD_TYPE_BLOB){
                    DataTuple tuple = new DataTuple(byte[].class, cursor.getBlob(index));
                    return tuple;
                }else if(cursor.getType(index) == Cursor.FIELD_TYPE_STRING){
                    DataTuple tuple=new DataTuple(byte[].class,cursor.getString(index).getBytes("utf-8"));
                    return tuple;
                }

            }
            throw new DaoException("can't bind value with name '" + cellName + "'");
        } else {
            return new DataTuple(cellModule.getBoundField().getType(),null);
        }
    }

    @Override
    public SqlDataType getDataType(Field boundField) {
        return SqlDataType.getType(boundField.getType());
    }

    @Override
    public Object pushData(Object object) {
        return object;
    }


}
