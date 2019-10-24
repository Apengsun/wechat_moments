package com.tw.commonsdk.utils;

import androidx.annotation.StringRes;

import com.tw.commonsdk.base.BaseApplication;

/**
 * @Author: Sunzhipeng
 * @Date 2019/4/9
 * @Time 14:08
 */
public class ResUtils {

    public static String resString(@StringRes int resid) {
        return BaseApplication.getAppContext().getString(resid);
    }

}
