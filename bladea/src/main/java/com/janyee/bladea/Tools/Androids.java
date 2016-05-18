package com.janyee.bladea.Tools;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2014/12/4.
 */
public class Androids {
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

}
