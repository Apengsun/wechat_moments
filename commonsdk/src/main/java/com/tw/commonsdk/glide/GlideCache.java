package com.tw.commonsdk.glide;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * 首页-专题
 *
 * @author guchenyang
 * @Date 2019/5/15
 * @time 17:29
 */
@GlideModule
public class GlideCache extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        //设置缓存的大小为100M
        int cacheSize =500 * 1024 * 1024;
        //设置缓存路径
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,cacheSize));
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
        );
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}
