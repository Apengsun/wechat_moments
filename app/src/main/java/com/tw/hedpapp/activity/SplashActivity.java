package com.tw.hedpapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.tw.commonsdk.base.BaseActivity;
import com.tw.commonsdk.core.RouterHub;
import com.tw.commonsdk.utils.AppUtils;
import com.tw.commonsdk.utils.Constants;
import com.tw.commonsdk.utils.Logger;
import com.tw.commonsdk.utils.NavigationUtils;
import com.tw.hedpapp.R;

import org.simple.eventbus.Subscriber;

import androidx.annotation.Nullable;
import me.jessyan.autosize.AutoSizeConfig;

public class SplashActivity extends BaseActivity {

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucentForImageView();
        AutoSizeConfig.getInstance().setBaseOnWidth(false);
        //android每次点击桌面图标应用重启的解决办法
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            return Constants.FINISH_RETURN;
        }
        return R.layout.activity_splash;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        timer.start();
    }

    // 含有tag,当用户post事件时,只有指定了tag的事件才会触发该函数,执行在UI线程
    @Subscriber(tag = Constants.FINISH_SPLASH_ACTIVITY)
    public void finishSplashActivity(String s) {
        Logger.e("finishSplashActivity()");
        NavigationUtils.navigation(RouterHub.APP_WE_CHAT_ACTIVITY);
        finish();
    }

    private CountDownTimer timer = new CountDownTimer(1000, 500) {
        @SuppressLint({"ResourceAsColor", "SetTextI18n"})
        @Override
        public void onTick(long l) {
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onFinish() {
                    AppUtils.reStart(SplashActivity.this);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
