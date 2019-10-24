package com.tw.commonsdk.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.tw.commonsdk.R;
import com.tw.commonsdk.utils.Logger;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: Sunzhipeng
 * @Date 2019/6/10
 * @Time 16:11
 */
public class LottieClassicsHeader extends RelativeLayout implements RefreshHeader {
    private LottieAnimationView animationView;

    public LottieClassicsHeader(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("WrongConstant")
    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.loading_lottie, this);
        animationView = (LottieAnimationView) view.findViewById(R.id.loading_lottie);
    }

    public LottieClassicsHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LottieClassicsHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LottieClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (0.3 < percent && percent <= 1.3) {
            animationView.setProgress((float) ((float) (percent - 0.3) * (1.0 / 4.0)));
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Logger.e("onReleased()--->height=" + height + " maxDragHeight=" + maxDragHeight);
        animationView.setMinAndMaxProgress((float) (1.0 / 4.0), 1);
        animationView.loop(true);
        animationView.playAnimation();
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 750;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState,
                               @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                Logger.e("----->PullDownToRefresh");
                animationView.cancelAnimation();
                animationView.setMinAndMaxProgress(0, (float) (1.0 / 4.0));
                animationView.loop(false);
                break;
            case Refreshing:
                Logger.e("----->Refreshing");
                break;
            case ReleaseToRefresh:
                Logger.e("----->ReleaseToRefresh");
                break;
        }

    }
}
