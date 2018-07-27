package com.houxj.generaltools.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.WindowManager;

import com.houxj.generaltools.permit.JRunTimePermissions;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by 侯晓戬 on 2018/7/23.
 * 手机设备信息类
 * 后去运行手机的相关数据信息
 */

public final class JPhoneUtils {
    private JPhoneUtils() {};
    //TODO 获取手机所使用安卓版本
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    //TODO 手机（系统）OS的版本号
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    //TODO 手机生产厂家的信息
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    //TODO 手机型号（包括厂家定义）
    public static String getModel() {
        return Build.MODEL;
    }

    //TODO 手机WIFI的MAC地址（需要开启WIFI，否则返回null）
    @SuppressLint("HardwareIds")
    public static String getWifiMac(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            WifiManager wifi = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (null != wifi) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            }
        } else {
            try {
                byte[] address = NetworkInterface.getByName("wlan0").getHardwareAddress();
                return JStringUtils.byte2hex(address, ":").toUpperCase();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return "0.0.0.0";
    }

    //TODO 获取当前ip地址
    public static String getWifiIP(){
        try {
            NetworkInterface myface = NetworkInterface.getByName("wlan0");
            Enumeration<InetAddress>  allip =  myface.getInetAddresses();
            for (Enumeration<InetAddress> enumIp = myface.getInetAddresses(); enumIp
                    .hasMoreElements(); ){
                InetAddress inetAddress = enumIp.nextElement();
                if(!inetAddress.isLinkLocalAddress()){
                    return inetAddress.getHostAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    //TODO 本机号码（由于这个不是必备数据，有可能无法获取到号码）
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getPhoneNumber(Context context) {
        if (JRunTimePermissions.hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(
                        Context.TELEPHONY_SERVICE);
                if (null != tm) {
                    JLogEx.d();
                    return tm.getLine1Number();
                }
            } catch (Exception e) {
                JLogEx.w(e.getMessage());
            }
        }
        return "";
    }

    //TODO 获取设备串号
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getSerial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    //TODO 营运商名称（无卡则没有)
    public static String getOperatorName(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (null != tm) {
            return tm.getNetworkOperatorName();
        }
        return "unknown";
    }

    //TODO 国际移动用户识别码（IMSI）
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMSI(Context context){
        if(JRunTimePermissions.hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            if (null != tm) {
                return tm.getSubscriberId();
            }
        }
        return "";
    }

    //TODO IMEI号码（国际移动设备识别码 GSM手机的 IMEI 和 CDMA手机的 MEID）
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI(Context context) {
        if(JRunTimePermissions.hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            if (null != tm) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return tm.getImei();
                } else {
                    return tm.getDeviceId();
                }
            }
        }
        return "";
    }

    //TODO 移动国家码和移动网络码(MCC+MNC)
    public static String getMccMnc(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (null != tm) {
            return tm.getNetworkOperator();
        }
        return "";
    }
    public static String getMcc(Context context){
        String mccmnc = getMccMnc(context);
        if(null != mccmnc && mccmnc.length() >= 5){
            return mccmnc.substring(0,3);
        }
        return "";
    }
    public static String getMnc(Context context){
        String mccmnc = getMccMnc(context);
        if(null != mccmnc && mccmnc.length() >= 5){
            return mccmnc.substring(3);
        }
        return "";
    }

    //TODO SIM卡的国家标识(中国cn其他待验证)
    public static String getSimCountry(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (null != tm) {
            return tm.getSimCountryIso();
        }
        return "";
    }

    //TODO SIM卡的网络制式（判断是否GSM网络）
    public static boolean isGsm(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        return null != tm && (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM);
    }

    //TODO 是否漫游状态(仅GSM下有效果，CDMA无效)
    public static boolean isRoaming(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        return null != tm && tm.isNetworkRoaming();
    }

    //TODO SIM卡状态 0
    public static int getSimState(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (null != tm) {
            return tm.getSimState();
        }
        return TelephonyManager.SIM_STATE_UNKNOWN;
    }

    //TODO 手机总内存大小（byte）
    public static long getTotalMemSize(Context context){
        ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(null != am) {
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memInfo);
            return memInfo.totalMem;
        }
        return 0;
    }

    //TODO 手机可用内存大小
    public static long getAvailMemSize(Context context){
        ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(null != am) {
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memInfo);
            return memInfo.availMem;
        }
        return 0;
    }

    //TODO 外部存储（SD卡）空间信息
    public static long[] getExternalStorageSize(){
        long[] sdCardInfo=new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }
    //TODO 总外部存储空间大小
    public static long getTotalExternalSize(){
        long[] sdinfo = getExternalStorageSize();
        if(null != sdinfo && sdinfo.length >= 1){
            return sdinfo[0];
        }
        return 0;
    }

    //TODO 手机可用外部存储空间大小
    public static long getAvailExternalSize(){
        long[] sdinfo = getExternalStorageSize();
        if(null != sdinfo && sdinfo.length >= 2){
            return sdinfo[1];
        }
        return 0;
    }

}
