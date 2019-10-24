package com.tw.commonsdk.utils;

import android.util.Log;

import com.tw.commonsdk.BuildConfig;

/**
 * author:gem
 * date:2019/3/26 15:42
 * desc:log日志工具类
 * version:
 */
public class Logger {
    public final static boolean debug = BuildConfig.LOG_DEBUG;

    public static void d(String msg) {
        if (debug)
            Log.d(Logger.class.getName(), msg);
    }
    public static void d(String tag,String msg) {
        if (debug)
            Log.d(tag, msg);
    }

    public static void i(String msg) {
        if (debug)
            Log.i(Logger.class.getName(), msg);

    }

    public static void e(String msg) {
        if (debug)
            Log.e(">>>>>>>", msg);
    }

    public static void e(String tag, String msg) {
        if (debug)
            Log.e(tag, msg);
    }

    public static void v(String msg) {
        if (debug)
            Log.v(Logger.class.getName(), msg);
    }

    public static void v(String tag, String msg) {
        if (debug)
            Log.v(tag, msg);
    }

    public static void w(String msg) {
        if (debug)
            Log.w(Logger.class.getName(), msg);
    }
    public static void w(String tag,String msg) {
        if (debug)
            Log.w(tag, msg);
    }


    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数

        if (debug) {
            int max_str_length = 2001 - tag.length();
            //大于4000时
            while (msg.length() > max_str_length) {
                Log.i(tag, msg.substring(0, max_str_length));
                msg = msg.substring(max_str_length);
            }
            //剩余部分
            Log.i(tag, msg);
        }
    }
}
