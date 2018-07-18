package com.houxj.generaltools.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.zip.CheckedOutputStream;

/**
 * Created by 侯晓戬 on 2018/7/16.
 * 文件类处理工具类
 */

public class JFileUtils {
    private final static String SHARD_FILE_PROVIDER_PATH = "file.provider";//这里要和AndroidManifest.xml 里的定义一样
    private final static Uri[] MEDIA_CONTENT_URI = {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI,
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
            MediaStore.Video.Media.INTERNAL_CONTENT_URI,
            MediaStore.Files.getContentUri("external"),
            MediaStore.Files.getContentUri("internal"),
    };

    //TODO 获取外部共享的地址(Uri)（兼容7.0以后的方法）
    public static Uri getFileShardUri(Context context, File file){
        Uri iRet = Uri.EMPTY;
        if (null != context && null != file){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                String authority = context.getPackageName() + "." + SHARD_FILE_PROVIDER_PATH;
                iRet = FileProvider.getUriForFile(context,authority, file);
            }else{
                iRet = Uri.fromFile(file);
            }
        }
        return iRet;
    }

    //TODO 获取临时目录地址
    public static String getTempPath(Context context){
        if(null != context) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//使用外部缓存
                File extCache = context.getExternalCacheDir();
                if (null != extCache) {
                    return extCache.getAbsolutePath();
                }
            } else {//使用app内部的缓存 /data/data/xxxxx/cache
                return context.getCacheDir().getAbsolutePath();
            }
        }
        return "";
    }

    //TODO 使用 Intent.ACTION_VIEW 打开某个类型文件，
    // 兼容7.0 系统
    public static void startActionFile(Context context, File file, String contentType){
        if(null != context){
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                intent.setDataAndType(getFileShardUri(context, file), contentType);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }catch (ActivityNotFoundException e){
                JLogEx.w(e.getMessage());
            }
        }
    }

    //TODO 获取文件扩展名称
    public static String getFileSuffixName(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    //TODO 将Bitmap保持成文件
    public static void saveBitmapFile(Bitmap bitmap, String filePath){
        File file = new File(filePath);
        FileOutputStream outputStream = null;
        ByteArrayOutputStream byteOutputStream = null;
        try {
            if(file.canWrite()){
                if(file.createNewFile()){
                    byteOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteOutputStream);
                    outputStream = new FileOutputStream(file);
                    outputStream.write(byteOutputStream.toByteArray());
                    outputStream.flush();
                }
            }
        }catch (IOException e){
            JLogEx.w(e.getMessage());
        }finally {
            if(null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != byteOutputStream){
                try {
                    byteOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //TODO Uri 转文件地址
    public static String getRealFilePath(Context context, Uri uri){
        String strRet = null;
        if(null != context && null != uri){
            String selection = "(" + MediaStore.MediaColumns._ID + "='" + uri.getLastPathSegment() + "')";
            ContentResolver resolver = context.getContentResolver();
            Cursor myCR = null;
            int _data_ind = -1;
            //从多媒体存储中查找
            for (Uri val : MEDIA_CONTENT_URI){
                myCR = queryFileFromProvider(resolver,val, selection);
                if(null != myCR ){
                    if(myCR.moveToFirst()){
                        _data_ind = myCR.getColumnIndex(MediaStore.MediaColumns.DATA);
                        if(_data_ind >= 0){
                            strRet = myCR.getString(_data_ind);
                            myCR.close();
                            break;
                        }
                    }
                    myCR.close();
                }
            }
            if(null == strRet && "file".equalsIgnoreCase(uri.getScheme())){
                strRet = uri.getPath();
            }
        }
        return strRet;
    }

    //TODO 文件地址转 Uri
    public static Uri getFileUri(Context context, String filePath){
        Uri iRet = Uri.EMPTY;
        if(null != filePath && null != context) {
            filePath = Uri.decode(filePath);
            String selection = "(" + MediaStore.MediaColumns.DATA + "='" + filePath + "')";
            ContentResolver resolver = context.getContentResolver();
            Cursor myCR = null;
            int _id_ind = -1;
            //从多媒体存储中查找
            for (Uri val : MEDIA_CONTENT_URI){
                myCR = queryFileFromProvider(resolver,val, selection);
                if(null != myCR ){
                    if(myCR.moveToFirst()){
                        _id_ind = myCR.getColumnIndex(MediaStore.MediaColumns._ID);
                        if(_id_ind >= 0){
                            iRet = Uri.parse(val.toString() + "/" + myCR.getInt(_id_ind));
                            myCR.close();
                            break;
                        }
                    }
                    myCR.close();
                }
            }
            if(iRet.equals(Uri.EMPTY)){//没有找到，则从直接转成file://的uri
                iRet = Uri.fromFile(new File(filePath));
            }
        }
        return iRet;
    }
    //TODO 从内容提供器中查询文件信息(用完记得关闭返回的cursor)
    // 参数说明 content 查询的 Provider 地址
    // selection 查询条件
    public static Cursor queryFileFromProvider(ContentResolver resolver,Uri content,String selection){
        if(null != resolver){
            Cursor  cr = resolver.query(content,
                    null,selection,
                    null, null);
            return cr;
        }
        return null;
    }



    //TODO 删除指定目录下文件
    //这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(null != files) {
                for (File item : files) {
                    item.delete();
                }
            }
        }
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

    //TODO 获取指定文件的大小（如果是目录则会递归计算目录下所有文件大小）
    public static long getFileSize(String file){
        return getFileSize(new File(file));
    }
    public static long getFileSize(File file){
        long size = 0;
        if(null != file && file.exists()){
            if(file.isDirectory()){
                File flist[] = file.listFiles();//文件夹目录下的所有文件
                if(null != flist){
                    for (File item : flist){
                        if (item.isDirectory()){
                            size = size + getFileSize(item);
                        }else{
                            size =size + getSize(item);
                        }
                    }
                }
            }else{
                size = getSize(file);
            }
        }
        return size;
    }

    private static long getSize(File file){
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
