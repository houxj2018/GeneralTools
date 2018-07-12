package com.houxj.generaltools.permit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created by 侯晓戬 on 2018/7/4.
 * 适配各机型的系统权限设置
 */

final class JPermissionsSystemSetting {
    //设置程序的权限设置
   static void settingPermission(Context context){
        if(Build.MANUFACTURER.equals("Huawei")){
            settingPermissionHuawei(context);
        }else if(Build.MANUFACTURER.equals("Meizu")){
            settingPermissionMeizu(context);
        }else if(Build.MANUFACTURER.equals("Xiaomi")){
            settingPermissionXiaomi(context);
        }else if(Build.MANUFACTURER.equals("Sony")){
            settingPermissionSony(context);
        }else if(Build.MANUFACTURER.equals("OPPO")){
            settingPermissionOppo(context);
        }else if(Build.MANUFACTURER.equals("LG")){
            settingPermissionLG(context);
        }else if(Build.MANUFACTURER.equals("vivo")){
            settingPermissionVivo(context);
        }else if(Build.MANUFACTURER.equals("samsung")){
            settingPermissionSamsung(context);
        }else if(Build.MANUFACTURER.equals("Letv")){
            settingPermissionLetv(context);
        }else if(Build.MANUFACTURER.equals("ZTE")){
            settingPermissionZTE(context);
        }else if(Build.MANUFACTURER.equals("YuLong")){
            settingPermissionYuLong(context);
        }else if(Build.MANUFACTURER.equals("LENOVO")){
            settingPermissionLENOVO(context);
        }else {
            settingPermissionAndroid(context);
        }
    }

    //原生系统的权限界面
    private static void settingPermissionAndroid(Context context){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName",
                    context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    //华为系统的权限界面
    private static void settingPermissionHuawei(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }

    //魅族系统的权限界面
    private static void settingPermissionMeizu(Context context){
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", context.getPackageName());
        context.startActivity(intent);
    }

    //小米系统的权限界面
    private static void settingPermissionXiaomi(Context context){
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", context.getPackageName());
        context.startActivity(intent);
    }

    //索尼系统的权限界面
    private static void settingPermissionSony(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.sonymobile.cta",
                "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }

    //oppo系统的权限界面
    private static void settingPermissionOppo(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.color.safecenter",
                "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }

    //LG系统的权限界面
    private static void settingPermissionLG(Context context){
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        context.startActivity(intent);
    }

    //vivo系统的权限界面
    private static void settingPermissionVivo(Context context){
        settingPermissionAndroid(context);
    }

    //三星系统的权限界面
    private static void settingPermissionSamsung(Context context){
        settingPermissionAndroid(context);
    }

    //乐视系统的权限界面
    private static void settingPermissionLetv(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        context.startActivity(intent);
    }

    //中兴系统的权限界面
    private static void settingPermissionZTE(Context context){
        settingPermissionAndroid(context);
    }

    //酷派系统的权限界面
    private static void settingPermissionYuLong(Context context){
        settingPermissionAndroid(context);
    }

    //联想系统的权限界面
    private static void settingPermissionLENOVO(Context context){
        settingPermissionAndroid(context);
    }
}
