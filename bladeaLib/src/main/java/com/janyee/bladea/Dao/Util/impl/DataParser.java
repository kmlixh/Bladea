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
        int type=cursor.getType(index);
        if (!cursor.isNull(index)) {
            Object data=null;
            if(type==Cursor.FIELD_TYPE_FLOAT){
                data=cursor.getDouble(index);
            }
            if(type==Cursor.FIELD_TYPE_BLOB){
                data=cursor.getBlob(index);
            }
            if(type==Cursor.FIELD_TYPE_INTEGER){
                data=cursor.getLong(index);
            }
            if(type==Cursor.FIELD_TYPE_STRING){
                data=cursor.getString(index);
            }
            return Castor.convertObject(cellModule.getBoundField().getType(),data);
        } else {
            return null;
        }
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
