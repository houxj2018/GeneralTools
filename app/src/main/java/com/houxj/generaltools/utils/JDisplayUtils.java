package com.houxj.generaltools.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by 侯晓戬 on 2017/7/24.
 * 尺寸单位转换工具
 */

public class JDisplayUtils {

    //TODO 根据手机的分辨率从 dip 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //TODO 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //TODO 将px值转换为sp值，保证文字大小不变
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //TODO 将sp值转换为px值，保证文字大小不变
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //TODO 屏幕分辨率
    public static JSize getScreenSize(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            if(null != wm) {
                wm.getDefaultDisplay().getRealMetrics(dm);
                return new JSize(dm.widthPixels, dm.heightPixels);
            }
        }else {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return new JSize(dm.widthPixels, dm.heightPixels);
        }
        return new JSize(0, 0);
    }

    //TODO 屏幕密度
    public static float getScreenDensity(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            if(null != wm) {
                wm.getDefaultDisplay().getRealMetrics(dm);
                return dm.density;
            }
        }else {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.density;
        }
        return 0;
    }

    //TODO 屏幕密度DPI
    public static int getScreenDensityDpi(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            if(null != wm) {
                wm.getDefaultDisplay().getRealMetrics(dm);
                return dm.densityDpi;
            }
        }else {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.densityDpi;
        }
        return 0;
    }

    //TODO 窗口的大小（窗口和屏幕大小在有导航栏是由差异）
    public static Rect getWindowsSize(@NonNull Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect;
    }

    //TODO 导航栏高度
    public static int getNaviBarHeight(@NonNull Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        JSize sizeSrceen = getScreenSize(activity);
        if(sizeSrceen.height > usableHeight){
            return sizeSrceen.height - usableHeight;
        }
        return 0;
    }

    public static class JSize{
        int width;
        int height;
        JSize(int w, int h){
            this.width = w;
            this.height = h;
        }

        @Override
        public String toString() {
            return "width=" + width + " height=" + height;
        }
    }
}
