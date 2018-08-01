package com.houxj.generaltools.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 侯晓戬 on 2018/7/24.
 * 第三方应用程序处理工具
 * 检查是否安装有指定应用
 * 启动第三方应用程序
 */

public final class JThirdAppUtils {
    private JThirdAppUtils(){};

    //TODO 重新启动应用本身
    public static void rebootAppSelf(@NonNull Context context, Class<?> launcherCls){
        Intent mStartActivity = new Intent(context, launcherCls);
        int mPendingIntentId = 0x2015;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context,
                mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(null != am) {
            am.set(AlarmManager.RTC, System.currentTimeMillis() + 100,
                    mPendingIntent);
            System.exit(0);
        }
    }

    //TODO 检测包名是否安装可用
    public static boolean checkAvilibleForPackage(@NonNull Context context, String packageName){
        if(!JStringUtils.isEmpty(packageName)) {
            PackageManager pm = context.getPackageManager();
            try {
                if (null != pm) {
                    ApplicationInfo info = pm.getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
                    return (null != info);
                }
            }catch (PackageManager.NameNotFoundException e){
                JLogEx.w(e.getMessage());
            }
        }
        return false;
    }

    //TODO 检测微信App是否安装可用
    public static boolean isWeixinAvilible(Context context){
        return checkAvilibleForPackage(context, "com.tencent.mm");
    }

    //TODO 检测是否安装了支付宝程序
    public static boolean isZhiFuBaoAvilible(Context context){
        return checkAvilibleForPackage(context, "com.eg.android.AlipayGphone");
    }

    //TODO 检测QQAPP是否安装可用
    public static boolean isQQClientAvailable(Context context){
        return checkAvilibleForPackage(context, "com.tencent.qqlite")
                || checkAvilibleForPackage(context,"com.tencent.mobileqq");
    }

    //TODO 启动指定包名的APP（没有安装返回false）
    public static boolean bootThirdApp(Context context, String packageName){
        if(!JStringUtils.isEmpty(packageName) && checkAvilibleForPackage(context, packageName)) {
            PackageManager pm = context.getPackageManager();
            if(null != pm){
                Intent intent = pm.getLaunchIntentForPackage(packageName);
                if(null != intent){
                    context.startActivity(intent);
                    return true;
                }
            }
        }
        return false;
    }

    //TODO 获取所有安装的应用程序信息
    public static List<MyAppInfo> getAvilibleApplication(@NonNull Context context){
        List<MyAppInfo> lstApp = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        if(null != pm){
            List<PackageInfo> lstInfo = pm.getInstalledPackages(0);
            if(null != lstInfo){
                for (PackageInfo info:lstInfo){
                    MyAppInfo myapp = new MyAppInfo();
                    myapp.setAppLable(info.applicationInfo.loadLabel(pm).toString());
                    myapp.setAppPackage(info.packageName);
                    myapp.setAppVersion(info.versionName);
                    myapp.setAppVerNumber(info.versionCode);
                    myapp.setAppIcon(info.applicationInfo.loadIcon(pm));
                    lstApp.add(myapp);
                }
            }
        }
        return lstApp;
    }

    public static class MyAppInfo{
        private String appLable;    //标题名字
        private String appPackage;  //包名
        private String appVersion;  //版本名
        private int appVerNumber;   //版本号码
        private Drawable appIcon;   //图标

        public String getAppLable() {
            return appLable;
        }

        public void setAppLable(String appLable) {
            this.appLable = appLable;
        }

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public int getAppVerNumber() {
            return appVerNumber;
        }

        public void setAppVerNumber(int appVerNumber) {
            this.appVerNumber = appVerNumber;
        }
    }
}
