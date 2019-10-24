/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tw.commonsdk.okgo.callback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.commonsdk.R;
import com.tw.commonsdk.utils.Constants;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    //    private ProgressDialog dialog;
    private Dialog dialog;
    private String title = "";

    private void initDialog(Activity activity, String title) {
        dialog = createLoadingDialog(activity, title);
        this.title = title;
        EventBus.getDefault().register(this);
    }
    //    private void initDialog(Activity activity, String title) {
    //        dialog = new ProgressDialog(activity);
    //        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //        dialog.setCanceledOnTouchOutside(false);
    //        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    //        dialog.setMessage("" + title);
    //        this.title = title;
    //    }

    public DialogCallback(Activity activity, String title) {
        super();
        initDialog(activity, title);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onError(Response response) {
        super.onError(response);
        if (Constants.LOGINING.equals(title)) {
            dialog.dismiss();
        }
    }

    @Override
    public void onFinish() {
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing() && !Constants.LOGINING.equals(title)) {
            EventBus.getDefault().unregister(this);
            dialog.dismiss();
        }
    }


    // 含有tag,当用户post事件时,只有指定了tag的事件才会触发该函数,执行在UI线程
    @Subscriber(tag = Constants.FINISH_DIALOG_CALLBACK)
    public void finishDialog(String s) {
        EventBus.getDefault().unregister(this);
        dialog.dismiss();
    }


    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     *
     * @return
     */
    private Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        return loadingDialog;

    }

}
