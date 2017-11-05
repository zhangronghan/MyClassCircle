package com.example.jia.classcircle.activity.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jia on 2017/9/14.
 */

public class SharePreferenceUtil {
    //用于存储判断是否为第一次登录
    private final static String FIRST_SP="FIRST_SP";
    private final static String FIRST_RUN="FIRST_RUN";
    public static boolean getIsFirstRun(Context context){
        SharedPreferences preferences=context.getSharedPreferences(FIRST_SP,Context.MODE_PRIVATE);
        return preferences.getBoolean(FIRST_RUN,true);
    }
    public  static void setFirstRun(Context context,boolean b){
        SharedPreferences preferences=context.getSharedPreferences(FIRST_SP,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(FIRST_RUN,b);
        editor.commit();

    }

    //夜间模式
    private static final String NIGHT_MODE="night_mode";
    private static final String IS_NIGHT_MODE="sp_night_mode";

    public static boolean getNightMode(Context context){
        SharedPreferences sp=context.getSharedPreferences(NIGHT_MODE,context.MODE_PRIVATE);
        return sp.getBoolean(IS_NIGHT_MODE,false);
    }

    public static void setNightMode(Context context, boolean b) {
        SharedPreferences sp=context.getSharedPreferences(NIGHT_MODE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(IS_NIGHT_MODE,b);
        editor.commit();
    }




}
