package com.janyee.bladea.Cast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kmlixh on 2016/2/15.
 */
public class Castor {
    public static boolean isNumberic(Object object){
        Class tcs=object.getClass();
        return isNumberic(tcs);
    }
    public static boolean isNumberic(Class tcs){
        if(tcs.equals(int.class)||tcs.equals(short.class)||tcs.equals(byte.class)
                ||tcs.equals(float.class)||tcs.equals(double.class)||tcs.equals(Integer.class)||tcs.equals(Short.class)
                ||tcs.equals(Byte.class)||tcs.equals(Float.class)||tcs.equals(Double.class)){
            return true;
        }else {
            return false;
        }
    }
    public static <T> T convertObject(Class<T> tClass,Object data){
        if(data!=null){
            if(tClass.equals(int.class)||tClass.equals(long.class)||tClass.equals(float.class)||tClass.equals(double.class)||tClass.equals(byte.class)||tClass.equals(short.class)||
                    tClass.equals(Integer.class)||tClass.equals(Long.class)||tClass.equals(Float.class)||tClass.equals(Double.class)||tClass.equals(Byte.class)||tClass.equals(Short.class)){
                if(isNumberic(data)){
                    return (T) data;
                }
            }
            if(tClass.equals(Date.class)&&(data.getClass().equals(long.class)||data.getClass().equals(Long.class))){
                Date date=new Date();
                date.setTime((long) data);
                return (T) date;
            }
            if(tClass.equals(Calendar.class)&&(data.getClass().equals(long.class)||data.getClass().equals(Long.class))){
                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis((Long) data);
                return (T) cal;
            }
            if(tClass.equals(String.class)){
                return (T) String.valueOf(data);
            }
            return null;
        }else{
            return null;
        }
    }

}
