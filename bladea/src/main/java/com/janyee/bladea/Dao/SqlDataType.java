package com.janyee.bladea.Dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kmlixh on 2014/11/24.
 */
public enum SqlDataType {
    INT(int.class,Integer.class), FLOAT(float.class,Float.class), DOUBLE(double.class,Double.class), TEXT(String.class), SMALLINI(short.class,Short.class), BIGINT(long.class,Long.class), TINYINT(byte.class,char.class,Byte.class,Character.class), DATETIME(Date.class, Calendar.class),BLOB(byte[].class,Byte[].class),NVARCHAR(String.class),CHAR(String.class),VARCHAR(String.class),NCHAR(String.class),MONEY(BigDecimal.class),  BOOLEAN(boolean.class,int.class,byte.class),AUTO(Object.class);
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
