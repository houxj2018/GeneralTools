package com.houxj.generaltools.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by 侯晓戬 on 2018/7/9.
 * MD5加密处理类
 */

public class JMd5Tools {

    //TODO MD5-32位加密方法
    public static String getMd5(String src){
        StringBuilder md5StrBuff = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(src.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            for (byte value: byteArray){
                md5StrBuff.append(String.format(Locale.CHINA,"%02x", (value & 0xFF)));
            }
        } catch (NoSuchAlgorithmException e) {
            JLogEx.w(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            JLogEx.w(e.getMessage());
        }
        return md5StrBuff.toString();
    }
    //TODO 获取MD5 的 byte 值
    public static byte[] getMd5ForByte(String src){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(src.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[]{0};
    }

    //TODO java String 转化成 MD5 byte[]
    public static byte[] hexStringToByte(String hex){
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

}
