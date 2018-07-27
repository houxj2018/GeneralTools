package com.houxj.generaltools.utils;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.houxj.generaltools.permit.JRunTimePermissions;

/**
 * Created by 侯晓戬 on 2018/7/19.
 * 网络信息处理类
 * 网络状态（是否有网络，使用的什么网络）
 * 网速情况
 * GPS定位开关，定位状态（是否定位，定位的值）
 */

public class JNetworkUtils {
    //TODO 获取当前网络连接类型 （WiFi 或者移动数据）
    // TYPE_NONE        = -1; TYPE_MOBILE      = 0; TYPE_WIFI        = 1; TYPE_MOBILE_MMS  = 2;
    public static int getNetworkType(Context context){
        final String permission = Manifest.permission.ACCESS_NETWORK_STATE;
        if (context != null && JRunTimePermissions.hasPermission(context,permission)) {
            //获取手机所有连接管理对象
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if(null != cm){//获取NetworkInfo对象
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {//返回NetworkInfo的类型
                    return networkInfo.getType();
                }
            }
        }
        return -1;
    }

    //TODO 检测是否wifi网络
    public static boolean isWifi(Context context){
        return getNetworkType(context) == ConnectivityManager.TYPE_WIFI;
    }

    //TODO 检测是否移动网络
    public static boolean isMobile(Context context){
        return getNetworkType(context) == ConnectivityManager.TYPE_MOBILE;
    }

    //TODO 获取当前的网络状态（无网络 -1，WIFI 0,4G 1，3G 2，2G 3）
    public static int getMobileType(Context context){
        int nRet = -1;
        int type = getNetworkType(context);
        if(ConnectivityManager.TYPE_WIFI == type){
            nRet = 0;
        }else{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if(null != cm){//获取NetworkInfo对象
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {//返回NetworkInfo的类型
                    int nSubType = networkInfo.getSubtype();
                    TelephonyManager telm = (TelephonyManager) context.getSystemService(
                            Context.TELEPHONY_SERVICE);
                    if(null != telm && !telm.isNetworkRoaming()){//不在漫游状态
                        switch (nSubType){
                            case TelephonyManager.NETWORK_TYPE_LTE:{//4G
                                nRet = 1;
                            }
                            break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_UMTS:{//3G
                                nRet = 2;
                            }
                            break;
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_GPRS:{//2G
                                nRet = 3;
                            }
                            break;
                            default:{
                                nRet = 3;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return nRet;
    }

    //TODO 获取本机IP地址（有wifi或者移动网络时）

    //TODO 获取本机WIFI的MAC地址（WIFI必须开启）

}
