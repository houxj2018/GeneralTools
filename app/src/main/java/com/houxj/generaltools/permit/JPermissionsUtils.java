package com.houxj.generaltools.permit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.houxj.generaltools.utils.JLogEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 侯晓戬 on 2018/7/4.
 * 权限相关工具类
 */

final class JPermissionsUtils {
    //危险权限定义，这些权限是需要运行时授权的
    private final static ArrayList<String> DANGER_AUTH = new ArrayList<String>(Arrays.asList(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            "android.permission.BODY_SENSORS",  //Manifest.permission.BODY_SENSORS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    ));

    //////////////////////////////////////////////////////////
    //获取APP申请的权限列表中的危险权限
    static List<String> getAppDangerPermissions(Context context){
        List<String> lst = new ArrayList<>();
        try {
            PackageManager pm = context.getPackageManager();
            String packName = context.getPackageName();
            PackageInfo info = pm.getPackageInfo(packName, PackageManager.GET_PERMISSIONS);
            if(null != info){
                for (String val:info.requestedPermissions){
                    if(DANGER_AUTH.indexOf(val) >= 0){
                        lst.add(val);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        JLogEx.d(lst.toString());
        return lst;
    }

    //查找未授权的运行时权限
    static List<String> findDeniedPermissions(Context context, List<String> permissions){
        List<String> denyPermissions = new ArrayList<>();
        int relust = 0;
        if(null != permissions) {
            for (String value : permissions) {
                relust = ContextCompat.checkSelfPermission(context, value);
                JLogEx.d("%s checkSelfPermission %d", value, relust);
                if (relust != PackageManager.PERMISSION_GRANTED) {//没有权限 就添加
                    denyPermissions.add(value);
                }
            }
        }
        return denyPermissions;
    }
    static List<String> findDeniedPermissions(Context context){
        return findDeniedPermissions(context,getAppDangerPermissions(context));
    }

    //TODO 检测这些权限中是否有 没有授权需要提示的
    //默认是false,但是只要请求过一次权限就会为true,
    //除非点了不再询问才会重新变为false
    private boolean shouldShowPermissions(Activity activity, String[] permission){
        for (String value : permission){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, value)){
                return true;
            }
        }
        return false;
    }
}
