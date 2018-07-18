package com.houxj.generaltools.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by 侯晓戬 on 2018/7/9.
 * Gson的工具类，
 * 用来统一处理一些特殊情况
 * 以及生成Gson实例
 */

public final class JGsonUtils {
    //TODO 生成Gson实例
    public static Gson create(){
        return new GsonBuilder()
                .create();
    }
}
