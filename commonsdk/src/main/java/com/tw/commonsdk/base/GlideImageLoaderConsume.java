package com.tw.commonsdk.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tw.commonsdk.R;
import com.tw.commonsdk.widget.NineGridView;

/**
 * author:gem
 * date:2019/5/21 10:35
 * desc:
 * version:
 */
public class GlideImageLoaderConsume implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.public_icon_square_default)
                .error(R.mipmap.public_icon_square_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);

    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
