package com.janyee.bladea.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;


/**
 * Created by frank on 2015/10/16.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    State wifiState = null;
    State mobileState = null;
    public static String action = "com.aierxin.www.net.state";
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取手机的连接服务管理器，这里是连接管理器类
        if(action.equals(intent.getAction())){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
                //ToastUtils.showToast(context, "网络连接成功！");
            } else if (wifiState != null && mobileState != null && State.CONNECTED == wifiState && State.CONNECTED != mobileState) {
               // ToastUtils.showToast(context, "WIFI连接成功！");
            } else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
                //ToastUtils.showToast(context, "没有任何网络");
            }
        }
    }
}