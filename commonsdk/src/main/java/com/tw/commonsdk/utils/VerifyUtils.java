package com.tw.commonsdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Sunzhipeng
 * @Date 2019/4/24
 * @Time 9:53
 */
public class VerifyUtils {
    public static final String TELREGEX = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";//正宗

    public static final String REG_MOBILE_CN_WO_CC = "^1[3-9]\\d{9}$";
//    private static final Pattern PTN_MOBILE_CN_WO_CC = Pattern.compile("^1[3-9]\\d{9}$");

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile(REG_MOBILE_CN_WO_CC);
        m = p.matcher(str);
        return m.matches();
    }
}
