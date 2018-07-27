package com.houxj.generaltools.permit;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.houxj.generaltools.utils.JLogEx;
import com.houxj.generaltools.utils.JPinYinUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 侯晓戬 on 2018/7/3.
 * 运行时权限处理类
 */

public class JRunTimePermissions {

    private static Map<Integer, JRunTimePermissions> mRTPermissionMap = new HashMap<>();

    private final Activity mActivity;
    private IPermissionCallBack mResultCallBack;
    private ArrayList<String> mRunTimePermissions = new ArrayList<>();

    private JRunTimePermissions(Activity activity){
        mActivity = activity;
    }

    public static JRunTimePermissions with(Activity activity){
        return new JRunTimePermissions(activity);
    }

    //TODO 设置权限信息
    public JRunTimePermissions setPermissions(List<String> permissions){
        mRunTimePermissions.clear();
        if(null != permissions){
            mRunTimePermissions.addAll(permissions);
        }
        return this;
    }
    public JRunTimePermissions setPermissions(String[] permissions){
        mRunTimePermissions.clear();
        if(null != permissions){
            mRunTimePermissions.addAll(Arrays.asList(permissions));
        }
        return this;
    }
    //TODO 检查运行时权限
    public void check(IPermissionCallBack callBack){
        mResultCallBack = callBack;
        checkPermission();
    }

    /////////////////////////////////////////////////////////////
    //检测运行时权限
    private void checkPermission(){
        if(mRunTimePermissions.size() == 0){//如果没有设置权限则获取app需要运行授权的危险权限
            setPermissions(JPermissionsUtils.findDeniedPermissions(mActivity));
        }
        if(mRunTimePermissions.size() == 0){
            callBackResult(IPermissionCallBack.PERMISSION_RELUST_OK);
        }else{
            mRTPermissionMap.put(12,this);
            JPermissionFragment.newInstant(mRunTimePermissions,12)
                    .requestPermission(mActivity.getFragmentManager());
        }
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

    //回调结果
    private void callBackResult(int result){
        if(null != mResultCallBack){
            mResultCallBack.onResult(result);
        }
    }

    private void permissionResultHandler(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults){
        if(verifyPermissions(grantResults)){
            callBackResult(IPermissionCallBack.PERMISSION_RELUST_OK);
        }else{
            callBackResult(IPermissionCallBack.PERMISSION_RELUST_FAIL);
        }
    }

    //TODO 判断请求权限是否成功
    private boolean verifyPermissions(int[] grantResults){
        if(null != grantResults) {
            JLogEx.d("%d", grantResults.length);
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    static void onPermissionResult(int requestCode,
                                   @NonNull String[] permissions,
                                   @NonNull int[] grantResults){
        JRunTimePermissions permit = mRTPermissionMap.remove(requestCode);
        if(null != permit){
            permit.permissionResultHandler(requestCode,permissions,grantResults);
        }
        JLogEx.d("%d", mRTPermissionMap.size());
    }

    //TODO 检查是否有权限
    public static boolean hasPermission(Context context, String permission){
        return hasPermission(context, new String[]{permission});
    }
    public static boolean hasPermission(Context context, String[] permissions){
        if(checkPermissionsInManifest(context, permissions)) {//先判断有定义在文件中
            List<String> list = Arrays.asList(permissions);
            List<String> denied = JPermissionsUtils.findDeniedPermissions(context, list);
            if (null != denied && denied.size() <= 0) {
                return true;
            }
        }
        return false;
    }

    //TODO 进入系统权限设置界面
    public static void goSystemPermissionSetting(Context context){
        JPermissionsSystemSetting.settingPermission(context);
    }

    //TODO 检查权限是否在 AndroidManifest.xml 中定义
    public static boolean checkPermissionsInManifest(Context context, String[] permission){
        boolean bRet = false;
        List<String> manifest = JPermissionsUtils.getAppDangerPermissions(context);
        bRet = manifest.containsAll(Arrays.asList(permission));
        return bRet;
    }
}
