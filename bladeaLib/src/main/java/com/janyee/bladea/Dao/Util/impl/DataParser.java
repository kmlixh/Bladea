package com.janyee.bladea.Dao.Util.impl;

import android.database.Cursor;

import com.janyee.bladea.Cast.Castor;
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
    public Object read(Cursor cursor, CellModule cellModule) throws Exception {
        String cellName = cellModule.getCellName();
        int index = cursor.getColumnIndex(cellName);
        Class tclass=cellModule.getBoundField().getType();
        int type=cursor.getType(index);
        Object result=null;
        if (!cursor.isNull(index)) {
            if(tclass.equals(int.class)||tclass.equals(Integer.class)){
                result= cursor.getInt(index);
            }
            if(tclass.equals(long.class)||tclass.equals(Long.class)){
                result= cursor.getLong(index);
            }
            if(tclass.equals(short.class)||tclass.equals(Short.class)){
                result= cursor.getShort(index);
            }
            if(tclass.equals(byte.class)||tclass.equals(Byte.class)){
                result= (byte)cursor.getInt(index);
            }
            if(tclass.equals(float.class)||tclass.equals(Float.class)){
                result= cursor.getFloat(index);
            }
            if(tclass.equals(double.class)||tclass.equals(Double.class)){
                result= cursor.getDouble(index);
            }
            if(tclass.equals(boolean.class)||tclass.equals(Boolean.class)){
                result= cursor.getInt(index)>0;
            }
            if(tclass.equals(Date.class)){
                if(type==Cursor.FIELD_TYPE_STRING){
                    result= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").parse(cursor.getString(index));
                }
                if(type==Cursor.FIELD_TYPE_INTEGER){
                    Date date=new Date();
                    date.setTime(cursor.getLong(index));
                    result= date;
                }
            }
            if(tclass.equals(Calendar.class)){
                Date test=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").parse(cursor.getString(index));
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(test);
                result= calendar;
            }
            if(tclass.equals(String.class)){
                result=cursor.getString(index);
            }

        }
        return result;
    }

    @Override
    public SqlDataType getDataType(Field boundField) {
        return SqlDataType.getType(boundField.getType());
    }

    @Override
    public Object write(Object object) {
        return object;
    }


}
