package com.houxj.generaltools.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by 侯晓戬 on 2018/6/27.
 * App缓存管理器
 */

public  class JAppCache {

    //TODO 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
    public static void cleanInternalCache(Context context) {
        JFileUtils.deleteFilesByDirectory(context.getCacheDir());
    }
    //TODO 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
    public static void cleanDatabases(Context context) {
        JFileUtils.deleteFilesByDirectory(context.getDatabasePath("1").getParentFile());
    }
    //TODO 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
    public static void cleanSharedPreference(Context context) {
        File file = new File(context.getFilesDir().getParent() + "/shared_prefs");
        if(!file.exists()) {
            file = new File(context.getFilesDir().getParent() + "/shaders");
        }
        JFileUtils.deleteFilesByDirectory(file);
    }
    //TODO 清除/data/data/com.xxx.xxx/files下的内容
    public static void cleanFiles(Context context) {
        JFileUtils.deleteFilesByDirectory(context.getFilesDir());
    }
    //TODO 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            JFileUtils.deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
    //TODO 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
    public static void cleanCustomCache(File custom) {
        JFileUtils.deleteFilesByDirectory(custom);
    }
    //TODO 清除本应用所有的数据
    public static void cleanAppCacheData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }
    //TODO 获取本应用所有的数据空间大小
    public static long getAppCacheSize(Context context){
        File fileApp = context.getFilesDir().getParentFile();
        return JFileUtils.getFileSize(fileApp);
    }

}
