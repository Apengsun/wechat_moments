package com.tw.commonsdk.base;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tw.commonsdk.R;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PickGlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.public_icon_square_default)
                .error(R.mipmap.public_icon_square_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        if (path.startsWith("http")) {
            Glide.with(activity)
                    .load(path)
                    .apply(options)
                    .into(imageView);
        } else {
            Glide.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .apply(options)
                    .into(imageView);
        }
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.public_icon_square_default)
                .error(R.mipmap.public_icon_square_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (path.startsWith("http")) {
            Glide.with(activity).load(path)
                    .apply(options)
                    .into(imageView);
        } else {
            Glide.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .apply(options)
                    .into(imageView);
        }
    }

    @Override
    public void clearMemoryCache() {
    }
}
