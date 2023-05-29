package com.autotest.photo.editor;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;


public class Polish extends Application {
    private static Polish queShot;

    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke( null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Context getContext() {
        return queShot.getContext();
    }

}
