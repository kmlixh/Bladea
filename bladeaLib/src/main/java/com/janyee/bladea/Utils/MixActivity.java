package com.janyee.bladea.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2014/12/4.
 */
public abstract class MixActivity extends Activity {
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            HandleMessages(msg);
        }
    };

    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public void sendMessage(int i) {
        handler.sendEmptyMessage(i);
    }

    public abstract void HandleMessages(Message msg);

    public void JumpTo(Class<Activity> classz) {
        Intent i = new Intent();
        i.setClass(getApplicationContext(), classz);
        startActivity(i);
    }

    public void TostShort(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void TostLong(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    public int boundThis(){
        Method[] methods=this.getClass().getMethods();
        return methods.length;
    }

}
