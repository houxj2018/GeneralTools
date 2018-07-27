package com.houxj.generaltools.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by 侯晓戬 on 2018/7/13.
 * 时间日期工具类
 * 时间日期格式转换
 * 时间计算
 */

public final class JDateTimeUtils {
    public static final String DEF_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEF_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEF_TIME_FORMAT = "HH:mm:ss";
    private static String mDateFormat = DEF_DATE_FORMAT;
    private static String mDateTimeFormat = DEF_DATE_TIME_FORMAT;
    // TODO 设置统一的日期显示格式
    public static void setDateFormat(String format){
        mDateFormat = format;
    }
    public static void setDateTimeFormat(String format){
        mDateTimeFormat = format;
    }
    //TODO 获取日期，时间格式化字符串
    public static String getFormatString(Date date,String format){
        if(null == date){
            return "";
        }
        if(null == format && format.length() == 0){
            format = DEF_DATE_TIME_FORMAT;
        }
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);
    }
    //TODO 获取日期字符
    //获取 默认格式日期字符串
    public static String getDay(){
        return getDate(new Date());
    }
    public static String getDate(Date date){
        return getFormatString(date, mDateFormat);
    }
    //TODO 获取时间字符
    public static String getDateTime(){
        return getDateTime(new Date());
    }
    public static String getDateTime(Date dateTime){
        return getFormatString(dateTime, mDateTimeFormat);
    }
    public static String getTime(){
        return getFormatString(new Date(), DEF_TIME_FORMAT);
    }
    public static String getTime(Date time){
        return getFormatString(time, DEF_TIME_FORMAT);
    }
    //TODO 将长时间格式字符串转换为时间 (yyyy-MM-dd HH:mm:ss)
    public static Date getDateForString(String strDate){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DEF_DATE_TIME_FORMAT,Locale.CHINA);
            return formatter.parse(strDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //TODO 网络返回的格林威治时间戳字符 转成时间类
    // /Date(-62135596800000)/ ; 6213559680 ; -62135596800000
    public static Date getGMTString(String timer){
        Calendar calendar = Calendar.getInstance();
        try {
            String timestamp="";
            if(timer.contains("Date(")){//先判断前缀是否符合
                timestamp = timer.substring(6, timer.length() - 2);
            }else if(timer.length() <= 10){//没有前缀的时间戳,需要*1000
                timestamp=timer+"000";
            }else{
                timestamp=timer;
            }
            long now = Long.parseLong(timestamp);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            calendar.setTimeInMillis(now);
            return calendar.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //TODO 计算时间长度
    public static long calTimeLong(TimeUnit unit, Date date1, Date date2){
        long timesec = date1.getTime() - date2.getTime();//差额之毫秒
        if(unit == TimeUnit.SECONDS) {
            return TimeUnit.MILLISECONDS.toSeconds(timesec);  //秒
        }else if(unit == TimeUnit.MINUTES){
            return TimeUnit.MILLISECONDS.toMinutes(timesec);  //分钟
        }else if(unit == TimeUnit.HOURS){
            return TimeUnit.MILLISECONDS.toHours(timesec);//小时
        }else if(unit == TimeUnit.DAYS){
            TimeUnit.MILLISECONDS.toDays(timesec);//天
        }
        return timesec;
    }

    //TODO 获取日期所在周的周一
    public static Date getWeekMondayForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return cal.getTime();
    }

    //TODO 获取日期所在周的周日
    public static Date getWeekSundayForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    //TODO 将秒数转换成文字描述
    public static String secondsToString(int timeSec) {
        if (timeSec <= 0)
            return "00:00:00";
        long hour = timeSec / 3600;
        timeSec = timeSec % 3600;
        long min = timeSec / 60;
        timeSec = timeSec % 60;
        String result = "";
        if (hour < 10) {
            result = "0" + hour + ":";
        } else {
            result = String.valueOf(hour) + ":";
        }

        if (min < 10) {
            result += "0" + min + ":";
        } else {
            result += min + ":";
        }

        if (timeSec < 10) {
            result += "0" + timeSec;
        } else {
            result += String.valueOf(timeSec);
        }
        return result;
    }
}
