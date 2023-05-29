package com.autotest.photo.editor.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static final String QUSHOT = "QUSHOT";

    public static void setKeyboard(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(QUSHOT, 0).edit();
        edit.putInt("height_of_keyboard", i);
        edit.apply();
    }

    public static int getKeyboard(Context context) {
        return context.getSharedPreferences(QUSHOT, 0).getInt("height_of_keyboard", -1);
    }

    public static void setHeightOfNotch(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(QUSHOT, 0).edit();
        edit.putInt("height_of_notch", i);
        edit.apply();
    }

    public static int getHeightOfNotch(Context context) {
        return context.getSharedPreferences(QUSHOT, 0).getInt("height_of_notch", -1);
    }

    public static boolean isRated(Context context) {
        return context.getSharedPreferences(QUSHOT, 0).getBoolean("is_rated_2", false);
    }

    public static void setRated(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(QUSHOT, 0).edit();
        edit.putBoolean("is_rated_2", z);
        edit.apply();
    }

    public static void increateCounter(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(QUSHOT, 0).edit();
        edit.putInt("counter", getCounter(context) + 1);
        edit.apply();
    }

    public static int getCounter(Context context) {
        return context.getSharedPreferences(QUSHOT, 0).getInt("counter", 1);
    }
}
