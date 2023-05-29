package com.autotest.photo.editor.support;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.autotest.photo.editor.Polish;
import com.autotest.photo.editor.activities.PolishHomeActivity;


public class MyExceptionHandlerPix implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandlerPix(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = null;
        if (activity != null) {
            intent = new Intent(activity, PolishHomeActivity.class);
        } else if (Polish.getContext() != null) {
            intent = new Intent(Polish.getContext(), PolishHomeActivity.class);
        }
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(Polish.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) Polish.getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pendingIntent);
        System.exit(2);
    }
}