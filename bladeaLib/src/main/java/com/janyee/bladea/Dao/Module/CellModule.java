package com.janyee.bladea.Dao.Module;

import android.database.Cursor;

import com.janyee.bladea.Cast.Castor;
import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.SqlDataType;
import com.janyee.bladea.Dao.Tuple.DataTuple;
import com.janyee.bladea.Dao.Util.IDataParser;
import com.janyee.bladea.Dao.Util.impl.DataParser;
import com.janyee.bladea.Dao.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by �˻� on 2015/8/27.
 */
public class CellModule {
    Field boundField;
    IDataParser parser;
    SqlDataType dataType;
    int length;
    ID id;
    Column column;
    Unique unique;
    NotNull notNull;
    OnConflict onConflict;

    public CellModule(Field boundField) {
        this.boundField = boundField;
        DataParseBy dataParseBy=boundField.getAnnotation(DataParseBy.class);
        if (dataParseBy!=null) {
            Class parser = dataParseBy.value();
            try {
                this.parser = (IDataParser) parser.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        id=boundField.getAnnotation(ID.class);
        if(id!=null){
            dataType=id.type();
            length=id.length();
        }else if((column=boundField.getAnnotation(Column.class))!=null){
            dataType=column.type();
            length=column.length();
        }else{
            throw new DaoException("no column or id find  for this column!");
        }

        unique=boundField.getAnnotation(Unique.class);
        notNull=boundField.getAnnotation(NotNull.class);
        onConflict=boundField.getAnnotation(OnConflict.class);
        if (parser == null) {
            parser = new DataParser();
        }
        if(dataType==SqlDataType.AUTO){
            dataType = parser.getDataType(boundField);
        }else{//检查数据类型匹配关系
            if(!dataType.verifyClass(boundField.getType())){
                throw new DaoException("Invalid Data type! we can not convert '"+dataType.name()+"' to '"+boundField.getType().getCanonicalName()+"'");
            }
        }
    }

    public Field getBoundField() {
        return boundField;
    }


    public String getCellName() {
        String result = null;
        if (boundField != null) {
            result = boundField.getName();
        }
        if(id!=null&&!id.value().equals("")){
            result=id.value();
        }else if(column!=null&&!column.value().equals("")){
            result=column.value();
        }
        return result;
    }

    /**
     *
     * @param obj 用于抽取数据的对象
     * @param cursor 抽取过程中用到的
     * @throws Exception
     */
    public void bindField(Object obj, Cursor cursor) throws Exception {
        if (cursor.getColumnIndex(getCellName())!=-1) {
            Object data = parser.read(cursor, this);
            boundField.setAccessible(true);
            boundField.set(obj,data);
        } else {
            boundField.setAccessible(true);
            if(Castor.isNumberic(boundField.getType())){
                boundField.set(obj, 0);
            }else{
                boundField.set(obj, null);
            }
        }
    }
    public void bindField(Object obj,Object value) throws IllegalAccessException {
        boundField.setAccessible(true);
        boundField.set(obj,value);
    }
    public Object getFieldValue(Object obj) {
        Object obbs = null;
        try {
            if (obj!=null) {
                boundField.setAccessible(true);
                Object temp = boundField.get(obj);
                if(Castor.isNumberic(boundField.getType())&&temp==null){
                    temp=0;
                }
                obbs=parser.write(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obbs;
    }

    public String getCreateCellString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'"+this.getCellName() + "' " + dataType.name());
        if(length>0){
            stringBuilder.append("("+length+")");
        }
        if(id!=null){
            stringBuilder.append(" PRIMARY KEY");
        }
        if(unique!=null){
            stringBuilder.append(" UNIQUE");
        }
        if(notNull!=null){
            stringBuilder.append(" NOT NULL");
        }
        if(onConflict!=null){
            stringBuilder.append(" ON CONFLICT "+onConflict.value().name());
        }
        return stringBuilder.toString();
    }
    @Override
    public String toString(){
        return getCellName()+":"+getBoundField().getType().getCanonicalName()+","+dataType.name()+","+length+","+(unique!=null?"unique":"")+","+(notNull!=null?"notnull":"")+","+(onConflict!=null?onConflict.value().name():"");
    }

}

