/*
 * Copyright 2017 JessYan
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
package com.tw.commonsdk.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tw.commonsdk.base.delegate.IActivity;
import com.tw.commonsdk.utils.AppUtils;
import com.tw.commonsdk.utils.Constants;
import com.tw.commonsdk.utils.Logger;
import com.tw.commonsdk.utils.StatusBarUtil;
import com.lzy.okgo.OkGo;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * ================================================
 * ================================================
 */
public abstract class BaseActivity extends AppCompatActivity implements IActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (useEventBus()) {
            //注册消息尽量在前否则收不到消息
            EventBus.getDefault().register(this);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        try {
            int layoutResID = initView(savedInstanceState);
            if (layoutResID == Constants.FINISH_RETURN) {
                finish();
                return;
            }
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
                ARouter.getInstance().inject(this);
            }
        } catch (Exception e) {
            if (e instanceof InflateException)
                throw e;
            e.printStackTrace();
        }
        initData(savedInstanceState);
//        initToolbar();//应该在initData方法之后，设置标题栏
    }

    private void initToolbar() {
        if (!getIntent().getBooleanExtra("isInitToolbar", false)) {
            //由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
            //而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
            getIntent().putExtra("isInitToolbar", true);
            //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
            if (findViewByName(getApplicationContext(), this, "public_toolbar") != null) {

                setSupportActionBar(findViewByName(getApplicationContext(), this, "public_toolbar"));
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        if (findViewByName(getApplicationContext(), this, "public_toolbar_title") != null) {
            ((TextView) findViewByName(getApplicationContext(), this, "public_toolbar_title")).setText(getTitle());
        }
        if (findViewByName(getApplicationContext(), this, "public_toolbar_back") != null) {
            findViewByName(getApplicationContext(), this, "public_toolbar_back").setOnClickListener(v -> {
                onBackPressed();
            });
        }

    }

    /**
     * findview
     *
     * @param activity
     * @param viewName
     * @param <T>
     *
     * @return
     */
    public static <T extends View> T findViewByName(Context context, Activity activity, String viewName) {
        int id = getResources(context).getIdentifier(viewName, "id", context.getPackageName());
        T v = (T) activity.findViewById(id);
        return v;
    }

    /**
     * 获得资源
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        OkGo.getInstance().cancelTag(this);
    }

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 {@code true} (默认为 {@code true}), Arms 会自动注册 EventBus
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    // 含有tag,当用户post事件时,只有指定了tag的事件才会触发该函数,执行在UI线程
    @Subscriber(tag = Constants.FINISH_ACTIVITY)
    public void finishActivity(String s) {
        if (Constants.NOT_FINISH_ACTIVITY.equals(s) && getClass().toString().equals("class com.tw.loginregister" +
                ".login.activity.LoginVerificationCode1Activity")) {
            Logger.e("EventBus--->notFinishActivity()" + getClass());
            return;
        }
        Logger.e("EventBus--->finishActivity()" + getClass());
        if (!AppUtils.isActivityTop(getClass(), this)) {
            finish();
        }
    }

    /**
     * 这个 {@link Activity} 是否会使用 {@link Fragment}, 框架会根据这个属性判断是否注册
     * {@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回 {@code false}, 那意味着这个 {@link Activity} 不需要绑定 {@link Fragment}, 那你再在这个 {@link Activity} 中绑定继承于
     * {@link BaseFragment} 的 {@link Fragment} 将不起任何作用
     *
     * @return 返回 {@code true} (默认为 {@code true}), 则需要使用 {@link Fragment}
     */
    @Override
    public boolean useFragment() {
        return true;
    }
    public Fragment getFragMent(String path) {
        return (Fragment) ARouter.getInstance().build(path).navigation();
    }

    protected void setStatusBarTranslucentForImageView() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }
}
