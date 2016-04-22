package com.janyee.bladea.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by kmlixh on 2014/8/28.
 */
public class AlertDialogShower extends AlertDialog {
    private View InfoView;

    private AlertDialogShower(Context context) {
        super(context);
        requestWindowFeature(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static AlertDialogShower getInstance(Context context) {
        return new AlertDialogShower(context);
    }

    public static void show(Context context, View view) {

        AlertDialogShower alertDialogShower = AlertDialogShower.getInstance(context);
        alertDialogShower.show();
        alertDialogShower.setContentView(view);
    }

    public void show(View view) {
        this.show();
        this.setContentView(view);
    }
}
