package com.janyee.bladea.Tools;

/**
 * Created by kmlixh on 2014/9/17.
 */
public class FlowUnitParser {
    public static String Parse(int info) {
        String result = "";
        double dd = 0;
        double foot = Math.log(info) / Math.log(2);
        if (foot >= 40) {
            dd = (double) ((int) (info / Math.pow(2, 40) * 100)) / 100;
            result = dd + "TB";
        } else if (foot < 40 && foot >= 30) {
            dd = (double) ((int) (info / Math.pow(2, 30) * 100)) / 100;
            result = dd + "GB";
        } else if (foot < 30 && foot >= 20) {
            dd = (double) ((int) (info / Math.pow(2, 20) * 100)) / 100;
            result = dd + "MB";
        } else if (foot < 20 && foot > 10) {
            dd = (double) ((int) (info / Math.pow(2, 10) * 100)) / 100;
            result = dd + "KB";
        } else {
            result = info + "B";
        }
        return result;
    }
}
