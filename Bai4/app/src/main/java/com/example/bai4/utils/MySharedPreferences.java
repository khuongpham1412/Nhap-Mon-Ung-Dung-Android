package com.example.bai4.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static MySharedPreferences myPreferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    public static MySharedPreferences getPreferences(Context context) {
        if (myPreferences == null) myPreferences = new MySharedPreferences(context);
        return myPreferences;
    }
    public void setValue(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }

    public int getValue(String key){
        return sharedPreferences.getInt(key, -1);
    }
}
