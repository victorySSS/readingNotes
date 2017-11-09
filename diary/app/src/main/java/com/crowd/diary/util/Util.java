package com.crowd.diary.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Util {

    public boolean checkPermission(String[] permissions, AppCompatActivity activity) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int permission_granted = PackageManager.PERMISSION_GRANTED;
            for (int i = 0; i < permissions.length; i++) {
                int checkPermission = ActivityCompat.checkSelfPermission
                        (activity, permissions[i]);
                if (permission_granted != checkPermission) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public AlertDialog getDialog(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }
}