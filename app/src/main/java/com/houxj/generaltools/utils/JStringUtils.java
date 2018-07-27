package com.houxj.generaltools.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 侯晓戬 on 2018/7/19.
 * 字符处理工具类
 */

public final class JStringUtils {
    private JStringUtils(){};
    //TODO 国内手机号码有效性检查
    public static boolean isPhoneNumber(String str){
        if(!isEmpty(str)){
            Pattern pattern = Pattern.compile("^\\+?(86)?0?1[3|4|5|7|8|9]{1}[0-9]{9}");
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
        return false;
    }

    //TODO 密码强度检查 (必须是字母和数字的二种的组合（不支持特殊符号）,长度大于等于8位)
    public static boolean checkPassWordStrength(String pwd){
        if(!isEmpty(pwd) && pwd.trim().length() >= 8){
            Pattern pattern = Pattern.compile("^([\\da-zA-Z]*\\d+[a-zA-Z]+[\\da-zA-Z]*)|([\\da-zA-Z]*[a-zA-Z]+\\d+[\\da-zA-Z]*)$");
            Matcher matcher = pattern.matcher(pwd);
            return matcher.matches();
        }
        return false;
    }

    //TODO 是否中文字符串检查
    public static boolean isChinese(String str){
        if(!isEmpty(str)){
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]*");
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
        return false;
    }

    //TODO 检测是否是数组字符串
    public static boolean isNumber(String str){
        if(!isEmpty(str)){
            Pattern pattern = Pattern.compile("[\\d|.]*");
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
        return false;
    }

    //TODO 检测是否是纯字母的字符串（不含特殊符号）
    public static boolean isEnglish(String str){
        if(!isEmpty(str)){
            Pattern pattern = Pattern.compile("[a-zA-Z]*");
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
        return false;
    }

    //TODO 检测是否是Ascii码字符串
    public static boolean isAscii(String str){
        return !isEmpty(str) && str.getBytes().length == str.length();
    }

    //TODO 检测是否空字符
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    //TODO 获取字符串存储长度（存储的byte长度）
    public static int getMemoryLength(String str){
        if(!isEmpty(str)) {
            return str.getBytes().length;
        }
        return 0;
    }

    //TODO 检测是否包含特殊符号
    public static boolean checkSpecialSymbol(String str){
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    //TODO 数字字符串转整型
    public static Number str2Int(String str){
        if(!isEmpty(str)){
            try {
                String number = extractNumber(str);
                Pattern pattern = Pattern.compile(".");
                Matcher matcher = pattern.matcher(number);
                if(matcher.find()){//带小数
                    return Double.parseDouble(number);
                }
                return Integer.parseInt(number);
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    //TODO 提取字符串中的数字串
    public static String extractNumber(String str){
        Pattern pattern = Pattern.compile("[\\d.]+");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group();
        }
        return "";
    }

    public static boolean equalsIgnoreCase(String src, String des){
        return !isEmpty(src) && !isEmpty(des) && src.toUpperCase().equals(des.toUpperCase());
    }

    //TODO byte 转hex 字符
    public static String byte2hex(byte[] bytes,String separator){
        StringBuilder builder = new StringBuilder();
        if(null != bytes) {
            for (byte value : bytes) {
                builder.append(String.format(Locale.CHINA, "%02x", (value & 0xFF)));
                builder.append(separator);
            }
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }
}
