package com.janyee.bladea.Cast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2014/11/26.
 */
public class DaoCastor {
    public static String ObjectToString(Object obj) {
        String result = "";
        if(obj==null){
            result="NULL";
        }
        if (obj instanceof Date) {
            DateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            result = "'" + formate.format(obj) + "'";
        } else if (obj instanceof Calendar) {
            Date date = ((Calendar) obj).getTime();
            DateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            result = "'" + formate.format(date) + "'";
        } else if (obj instanceof String) {
            result = "'" + obj + "'";
        } else if(Castor.isNumberic(obj)) {
            result = String.valueOf(obj);
        }else if(obj.getClass().equals(boolean.class)){
            result=((boolean)obj)?"1":"0";
        }
        return result;
    }

    public static String ArrayToString(Object[] objects) {
        if (objects != null && objects.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object obj : objects) {
                String value = ObjectToString(obj);
                stringBuilder.append(value + ",");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            return stringBuilder.toString();
        } else {
            return null;
        }

    }

    public static String ListToString(List list) {
        if (list != null & list.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                String value = ObjectToString(obj);
                stringBuilder.append(value + ",");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            return stringBuilder.toString();
        } else {
            return null;
        }
    }
}
