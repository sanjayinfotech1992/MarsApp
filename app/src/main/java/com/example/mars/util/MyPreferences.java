package com.example.mars.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String IS_LOGIN = "isLogin";
    public static final String USER_IMAGE = "imag";
    public static final String USER_NAME = "name";
    public static final String Email = "emailKey";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }

    public void saveString(String key,String val){
        editor.putString(key, val);
        editor.commit();
    }
    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    public void saveBool(String key,boolean val){
        editor.putBoolean(key,val);
        editor.commit();
    }
    public boolean getBool(String key){
        return sharedPreferences.getBoolean(key,false);
    }

}
