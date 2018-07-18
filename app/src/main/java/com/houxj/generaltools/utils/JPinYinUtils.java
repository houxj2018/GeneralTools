package com.houxj.generaltools.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 侯晓戬 on 2018/7/13.
 * 汉字拼音处理
 */

public final class JPinYinUtils {
    private final static String DEF_SEPARATOR_VAL = " ";
    //TODO 获取中文字的全拼字符
    // bTone 是否带声调
    public static String getFullPinYin(String chinese,boolean separator, boolean bTone){
        try {
            HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
//            outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
            if(bTone) {
                outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
            }else{
                outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            }
            StringBuilder builder = new StringBuilder();
            for (int i=0; i< chinese.length(); i++){
                final String[] out = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(i), outputFormat);
                if(null != out && out.length > 0){
                    builder.append(out[0]);
                    builder.append(DEF_SEPARATOR_VAL);
                }
            }
            builder.deleteCharAt(builder.length() -1);
            return builder.toString();
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return chinese;
    }
    public static String getFullPinYin(String chinese){
        return getFullPinYin(chinese,true,false);
    }

    //TODO 获取首字母拼音
    public static String getFirstPinYin(String chinese){
        String py = getFullPinYin(chinese);
        String[] pinyin = py.split(DEF_SEPARATOR_VAL);
        StringBuilder builder = new StringBuilder();
        if(null != pinyin){
            for (String val:pinyin){
                builder.append(val.charAt(0));
            }
        }
        return builder.toString().toUpperCase();
    }

    //TODO 字母排序
    public static List<String> sortByFirstPinYin(List<String> lstString){
        Collections.sort(lstString, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                String fpy1 = getFirstPinYin(str1);
                String fpy2 = getFirstPinYin(str2);
                int len = fpy1.length();
                int value = 0;
                if(len > fpy2.length()){
                    len = fpy2.length();
                }
                for (int i = 0; i< len; i++){
                    if(fpy1.charAt(i) != fpy2.charAt(i)){
                        value = fpy1.charAt(i) - fpy2.charAt(i);
                        break;
                    }
                }
                return value;
            }
        });
        return lstString;
    }


}
