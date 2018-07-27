package com.houxj.generaltools.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 侯晓戬 on 2018/7/24.
 * 软键盘控制检查工具类
 */

public final class JSoftKeyboardUtils {

    //TODO 检测软键盘状态（开启 or 关闭）
    public static boolean checkSoftKeyboardState(@NonNull Activity activity){
        JDisplayUtils.JSize srcSize = JDisplayUtils.getScreenSize(activity);
        Rect winSize = JDisplayUtils.getWindowsSize(activity);
        return srcSize.height - winSize.bottom - JDisplayUtils.getNaviBarHeight(activity) != 0;
    }

    //TODO 关闭软键盘
    public static void hideSoftKeyboard(@NonNull Activity activity){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(null != imm && imm.isActive()&&activity.getCurrentFocus()!=null){
            if(activity.getCurrentFocus().getWindowToken() != null){
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //TODO 开启软键盘
    public static void showSoftKeyboard(@NonNull Activity activity){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(null != imm && imm.isActive()&&activity.getCurrentFocus()!=null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    public static void showSoftKeyboard(@NonNull final View view){
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager)context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(null != imm && imm.isActive(view)) {
            if (view.getWindowToken() != null) {
                imm.toggleSoftInputFromWindow(view.getWindowToken(),
                        0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    public static void showSoftKeyboardDelayed(@NonNull final View view, int delaymillis){
        //先获取焦点
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard(view);
            }
        },delaymillis);
    }

}
