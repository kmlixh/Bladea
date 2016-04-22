package com.janyee.bladea.Cast;

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
}
