package com.example.tonymurchison.illuminandus;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreEditor {
    public static final String PREFS_NAME = "AOP_PREFS";

    public void saveInt(Context context, String Key, int Int) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putInt(Key, Int); //3

        editor.commit(); //4
    }

    public int getValue(Context context, String valueName) {
        SharedPreferences settings;
        int value;
        
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        value = settings.getInt(valueName, 0);
        return value;
    }
}
