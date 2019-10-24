package com.tw.hedpapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tw.commonres.widget.LoadingLayout;
import com.tw.commonsdk.base.BaseActivity;
import com.tw.commonsdk.core.NetConstant;
import com.tw.commonsdk.core.RouterHub;
import com.tw.commonsdk.glide.ImageLoader;
import com.tw.commonsdk.okgo.callback.JsonCallback;
import com.tw.commonsdk.utils.AppUtils;
import com.tw.commonsdk.utils.Logger;
import com.tw.commonsdk.utils.ResUtils;
import com.tw.hedpapp.R;
import com.tw.hedpapp.adapter.TweetsAdapter;
import com.tw.hedpapp.bean.Tweet;
import com.tw.hedpapp.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

@Route(path = RouterHub.APP_WE_CHAT_ACTIVITY)
public class WeChatActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.srf)
    SmartRefreshLayout srf;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.abl)
    AppBarLayout abl;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.public_toolbar)
    LinearLayout tb;
    @BindView(R.id.public_toolbar_title)
    TextView publicToolbarTitle;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    private long mPressedTime;
    private List<Tweet> itemsBeanListAll = new ArrayList<>();
    private List<Tweet> itemsBeanList = new ArrayList<>();
    private TweetsAdapter tweetsAdapter;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_we_chat;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucentForImageView();
        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { // 折叠状态
                    tb.setVisibility(View.VISIBLE);
                    tvNick.setVisibility(View.GONE);
                    ivAvatar.setVisibility(View.GONE);
                } else { // 非折叠状态
                    tb.setVisibility(View.GONE);
                    tvNick.setVisibility(View.VISIBLE);
                    ivAvatar.setVisibility(View.VISIBLE);
                }
            }
        });
        loading.showContent();
        tweetsAdapter = new TweetsAdapter(itemsBeanList);
        LinearLayoutManager manager =
                new LinearLayoutManager(WeChatActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,
                R.drawable.public_shape_list_divider));
        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(tweetsAdapter);
        srf.setEnableLoadMore(true);
        srf.setEnableRefresh(true);
        srf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserInfo();
                getTweets();
            }
        });

        srf.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getTweets(itemsBeanList.size());
            }
        });

        loading.showLoading();
        getUserInfo();
        getTweets();
    }

    private void getUserInfo() {
        OkGo.<UserInfo>get(NetConstant.URL_USER_INFO)
                .tag(this)
                .execute(new JsonCallback<UserInfo>() {
                    @Override
                    public void onSuccess(Response<UserInfo> response) {
                        if (response.code() == 200) {
                            UserInfo userInfo = response.body();
                            Logger.e("  " + userInfo);
                            ImageLoader.getImageLoader
                                    (WeChatActivity.this).loadUrl(AppUtils
                                            .getImageURL(userInfo.getProfileimage()),
                                    ivProfile, ImageLoader
                                            .getRectangleOption());
                            ImageLoader.getImageLoader(WeChatActivity.this).loadUrl(AppUtils.getImageURL(userInfo.getAvatar()),
                                    ivAvatar);
                            tvNick.setText(userInfo.getNick());
                        }
                    }
                });
    }

    private void getTweets() {
        OkGo.<List<Tweet>>get(NetConstant.URL_TWEETS)
                .tag(this)
                .execute(new JsonCallback<List<Tweet>>() {
                    @Override
                    public void onSuccess(Response<List<Tweet>> response) {
                        loading.showContent();
                        if (response.code() == 200) {
                            itemsBeanListAll.clear();
                            itemsBeanList.clear();
                            List<Tweet> items = response.body();
                            Logger.e("  " + items.toString());
                            for (Tweet tweet : items) {
                                if (tweet == null) {
                                    continue;
                                }
                                if (tweet.getImages() == null || tweet.getImages().size() == 0) {
                                    if (tweet.getContent() == null || tweet.getContent().isEmpty()) {
                                        continue;
                                    }
                                }
                                itemsBeanListAll.add(tweet);
                            }
                            getTweets(0);

                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);
                        loading.showError();
                        loading.setRetryListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loading.showLoading();
                                getUserInfo();
                                getTweets();
                            }
                        });
                    }

                });
    }

    private void getTweets(int index) {
        if (itemsBeanListAll.size() < index) {
            srf.finishLoadMoreWithNoMoreData();
            return;
        }
        int end = 0;
        if (itemsBeanListAll.size() > index + 5) {
            end = index + 5;

        } else {
            end = itemsBeanListAll.size();
            srf.finishLoadMoreWithNoMoreData();
        }
        for (int i = index; i < end; i++) {
            itemsBeanList.add(itemsBeanListAll.get(i));
        }
        tweetsAdapter.notifyDataSetChanged();
        if (index > 0) {
            srf.finishLoadMore();
        }
        if (index == 0) {
            srf.finishRefresh();
        }
    }


    @Override
    public void onBackPressed() {
        //获取第一次按键时间
        long mNowTime = System.currentTimeMillis();
        //比较两次按键时间差
        if ((mNowTime - mPressedTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    ResUtils.resString(R.string.app_press_exit_again), Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            } catch (Exception e) {
                onBackPressed();
            }
        }
    }
}
