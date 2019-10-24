package com.tw.commonsdk.utils;

import android.view.View;

/**
 * 防止快速点击事件工具类
 */
public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 500;
    private final static int INTERVAL_TIME = 2000;

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime >
                SPACE_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }

    public synchronized static boolean isRepeatClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime >
                INTERVAL_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }
    /**
     * 重复点击
     * @param view
     * @return
     */
    public static boolean avoidRepeatClick(View view){
        boolean flag = false;
        long lastTime = view.getTag(-1)==null?0:(long)view.getTag(-1);
        if (System.currentTimeMillis()-lastTime>1000){
            flag = true;
        }
        view.setTag(-1,System.currentTimeMillis());
        return flag;
    }

}
