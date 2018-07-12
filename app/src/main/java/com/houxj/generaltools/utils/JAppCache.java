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
        deleteFilesByDirectory(context.getCacheDir());
    }
    //TODO 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(context.getDatabasePath("1").getParentFile());
    }
    //TODO 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
    public static void cleanSharedPreference(Context context) {
        File file = new File(context.getFilesDir().getParent() + "/shared_prefs");
        if(!file.exists()) {
            file = new File(context.getFilesDir().getParent() + "/shaders");
        }
        deleteFilesByDirectory(file);
    }
    //TODO 清除/data/data/com.xxx.xxx/files下的内容
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }
    //TODO 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
    //TODO 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
    public static void cleanCustomCache(File custom) {
        deleteFilesByDirectory(custom);
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
        return getDirectorySize(fileApp);
    }
    //TODO 存储空间单位转换
    public static String fomatFileSize(long size){
        final long KB_SIZE = (1024*1024);
        final long MB_SIZE = (1024*1024*1024);
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (size == 0) {
            return wrongSize;
        }
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < KB_SIZE) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < MB_SIZE) {
            fileSizeString = df.format((double) size / KB_SIZE) + "MB";
        } else {
            fileSizeString = df.format((double) size / MB_SIZE) + "GB";
        }
        return fileSizeString;
    }

    // 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(null != files) {
                for (File item : files) {
                    item.delete();
                }
            }
        }
    }

    //获取目录空间大小
    public static long getDirectorySize(File file){
        long size = 0;
        if(null != file && file.exists() && file.isDirectory()){
            File flist[] = file.listFiles();//文件夹目录下的所有文件
            if(null != flist){
                for (File item : flist){
                    if (item.isDirectory()){
                        size = size + getDirectorySize(item);
                    }else{
                        size =size + getFileSize(item);
                    }
                }
            }
        }
        return size;
    }

    //获取文件大小
    private static long getFileSize(File file){
        long size = 0;
        if (null != file && file.exists() && file.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);//使用FileInputStream读入file的数据流
                size = fis.available();//文件的大小
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(null != fis) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }
}
