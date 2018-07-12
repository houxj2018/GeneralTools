package com.houxj.generaltools.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 侯晓戬 on 2018/7/9.
 * 本地信息存储工具类
 */

public class JSharedP {
    private final static String KEY_SP_NAME = "JH_SP_PRIVATE";

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(KEY_SP_NAME,Context.MODE_PRIVATE);
    }

    //TODO 获取和设置 整型 数据
    public static void setIntValue(Context context,String key, int value){
        getSharedPreferences(context).edit().putInt(key,value).apply();
    }

    public static int getIntValue(Context context,String key){
        return getIntValue(context,key, 0);
    }

    public static int getIntValue(Context context,String key,int defValue){
        return getSharedPreferences(context).getInt(key, defValue);
    }

    //TODO 获取和设置 长整型 数据
    public static void setLongValue(Context context,String key, Long value){
        getSharedPreferences(context).edit().putLong(key,value).apply();
    }

    public static Long getLongValue(Context context,String key){
        return getLongValue(context,key, 0L);
    }

    public static Long getLongValue(Context context,String key,Long defValue){
        return getSharedPreferences(context).getLong(key, defValue);
    }

    //TODO 获取和设置 布尔 数据
    public static void setBooleanValue(Context context,String key, boolean value){
        getSharedPreferences(context).edit().putBoolean(key,value).apply();
    }

    public static boolean getBooleanValue(Context context,String key,boolean defValue){
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static boolean getBooleanValue(Context context,String key){
        return getBooleanValue(context,key,false);
    }

    //TODO 获取和设置 字符串 数据
    public static void setStringValue(Context context,String key, String value){
        getSharedPreferences(context).edit().putString(key,value).apply();
    }

    public static String getStringValue(Context context, String key, String defValue){
        return getSharedPreferences(context).getString(key,defValue);
    }

    public static String getStringValue(Context context,String key){
        return getStringValue(context,key,"");
    }

    //TODO 获取和设置 浮点数 数据
    public static void setFloatValue(Context context,String key, float value){
        getSharedPreferences(context).edit().putFloat(key,value).apply();
    }

    public static float getFloatValue(Context context, String key, float defValue){
        return getSharedPreferences(context).getFloat(key,defValue);
    }

    public static float getFloatValue(Context context,String key){
        return getFloatValue(context,key,0.0f);
    }


    //TODO 获取和设置 列表 数据
    public static void setListValue(Context context,String key, List value){
        String strVal =  JGsonUtils.create().toJson(value);
        getSharedPreferences(context).edit().putString(key, strVal).apply();
    }

    public static <T>  List<T> getListValue(Context context,String key,Class cls){
        String jsonVal = getStringValue(context,key,"");
        Type type = new JParameterizedTypeImpl(cls);
        return JGsonUtils.create().fromJson(jsonVal, type);
    }

    //TODO 获取和设置 对象 数据
    public static <T> void setValue(Context context,String key, T value){
        String strVal =  JGsonUtils.create().toJson(value);
        getSharedPreferences(context).edit().putString(key, strVal).apply();
    }

    public static <T>  T getValue(Context context,String key,Class<T> cls){
        String jsonVal = getStringValue(context,key,"");
        return JGsonUtils.create().fromJson(jsonVal, cls);
    }

    //TODO END
}
