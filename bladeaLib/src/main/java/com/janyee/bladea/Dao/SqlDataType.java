package com.janyee.bladea.Dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2014/11/24.
 */
public enum SqlDataType {
    INT(int.class), FLOAT(float.class), DOUBLE(double.class), TEXT(String.class), SMALLINI(short.class), BIGINT(long.class), TINYINT(byte.class,char.class), DATETIME(Date.class, Calendar.class),BLOB(byte[].class),NVARCHAR(String.class),CHAR(String.class),VARCHAR(String.class),NCHAR(String.class),MONEY(BigDecimal.class),  BOOLEAN(boolean.class,int.class,byte.class),AUTO(Object.class);
    private Class[] nameClass;

    private SqlDataType(Class... nameClass) {
        this.nameClass = nameClass;
    }

    public Class[] type() {
        return this.nameClass;
    }

    public static SqlDataType getType(Class typeClass) {
        for (SqlDataType type : SqlDataType.values()) {
            for (Class tt : type.type()) {
                if (tt.equals(typeClass)) {
                    return type;
                }
            }

        }
        return TEXT;
    }
    public boolean verifyClass(Class clazz){
        boolean result=false;
        for(Class temp:nameClass){
            if(temp.equals(clazz)){
                result=true;
                break;
            }
        }
        return result;
    }

}
