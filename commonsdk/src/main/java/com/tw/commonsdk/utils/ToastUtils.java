package com.tw.commonsdk.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tw.commonsdk.R;
import com.tw.commonsdk.base.BaseApplication;

/**
 * Toast消息提示公共类
 *
 * @author tw
 */

public class ToastUtils {

    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;


    /**
     * 显示Toast
     *
     * @param message
     */
    public static Toast showToast(String message) {
        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.toast_text, null);
        //加載layout下的布局
        TextView text = view.findViewById(R.id.tv);
        text.setText(message); //toast内容
        Toast toast = new Toast(BaseApplication.getAppContext());
        //        toast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android
        // :layout_gravity
        toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
        //        toast = Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_SHORT);
        //        toast.show();
        //            oneTime = System.currentTimeMillis();
        //        } else {
        //            twoTime = System.currentTimeMillis();
        //            if (message.equals(oldMsg)) {
        //                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
        //                    toast.show();
        //                }
        //            } else {
        //                oldMsg = message;
        //                toast.setText(message);
        //                toast.show();
        //            }
        //        }
        //        oneTime = twoTime;
        return toast;
    }

    /**
     * 子线程中调用toast
     */
    public static void getToast(String message) {
        try {
            if (toast != null) {
                toast.setText(message);
                toast.show();
            } else {
                toast = Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }


    /**
     * 显示Toast
     *
     * @param content 内容
     */
    public static void toastShowShort(String content) {
        Toast.makeText(BaseApplication.getAppContext(), content, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resid) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(resid);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(imageView);
        toast.show();
    }

    public static void showToastSuccess(String message) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()
        // 来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化

        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.toast_success_layout, null);
        //加載layout下的布局
        TextView text = view.findViewById(R.id.tv);
        text.setText(message); //toast内容
        Toast toast = new Toast(BaseApplication.getAppContext());
        toast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android
        // :layout_gravity
        toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

    public static void showToastFailure(String message) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()
        // 来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化

        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.toast_failure_layout, null);
        //加載layout下的布局
        TextView text = view.findViewById(R.id.tv);
        text.setText(message); //toast内容
        Toast toast = new Toast(BaseApplication.getAppContext());
        toast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android
        // :layout_gravity
        toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

    //    public static void showToastNew(String message) {
    //        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()
    //        // 来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
    //
    //        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.layout_course, null);
    //        //加載layout下的布局
    //        TextView text = view.findViewById(R.id.toastMsg);
    //        text.setText(message); //toast内容
    //        Toast toast = new Toast(BaseApplication.getAppContext());
    //        toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android
    //        // :layout_gravity
    //        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
    //        toast.setView(view); //添加视图文件
    //        toast.show();
    //    }

    /**
     * 显示Toast
     *
     * @param content 内容
     */
    public static void toastShowLong(String content) {
        Toast.makeText(BaseApplication.getAppContext(), content, Toast.LENGTH_LONG).show();
    }
}
