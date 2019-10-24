package com.tw.commonsdk.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tw.commonsdk.R;
import com.tw.commonsdk.utils.Logger;

import java.io.File;
import java.security.MessageDigest;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * 图片加载器
 *
 * @author guchenyang
 * @Date 2019/5/15
 * @time 18:05
 */
public class ImageLoader {
    private Context mContext;
    private RequestOptions requestOptions;

    private ImageLoader(Context context) {
        this.mContext = context.getApplicationContext();
        requestOptions = RequestOptions.errorOf(R.mipmap.public_avar_default)
                .placeholder(R.mipmap.public_avar_default)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
//                .apply(RequestOptions.bitmapTransform( new GlideRoundTransform())
                .centerCrop()
//                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//把图片缓存到本地
    }

    private volatile static ImageLoader mImageLoader = null;

    public static ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (mImageLoader == null) {
                    return mImageLoader = new ImageLoader(context);
                }
            }
        }
        return mImageLoader;
    }

    /**
     * 获取 矩形默认图option
     *
     * @return
     */
    public static RequestOptions getRectangleOption() {
        return RequestOptions.errorOf(R.mipmap.public_icon_square_default)
                .placeholder(R.mipmap.public_icon_square_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL);//把图片缓存到本地
    }

    /**
     * 获取 方形默认图option
     *
     * @return
     */
    public static RequestOptions getSquareOption() {
        return RequestOptions.errorOf(R.mipmap.public_logo)
                .placeholder(R.mipmap.public_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL);//把图片缓存到本地
    }


    /**
     * 加载网络图片
     *
     * @param url       地址
     * @param imageView
     */
    public void loadUrl(String url, ImageView imageView) {

        loadUrl(url, imageView, requestOptions);
    }


    /**
     * 加载网络图片
     *
     * @param url       地址
     * @param imageView
     */
    public void loadUrl(String url, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions())//默认动画
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        // Log the GlideException here (locally or with a remote logging framework):
                        Logger.e("Load failed" + e);

                        // You can also log the individual causes:
                        for (Throwable t : e.getRootCauses()) {
                            Logger.e("Caused by" + t);
                        }
                        // Or, to log all root causes locally, you can use the built in helper
                        // method:
                        e.logRootCauses("onLoadFailed");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource
                            , boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);

    }

    /**
     * 加载网络图片
     *
     * @param resourceId 地址
     * @param imageView
     */
    public void loadUrl(@RawRes @DrawableRes @Nullable Integer resourceId, ImageView imageView,
                        RequestOptions requestOptions) {

        Glide.with(mContext)
                .load(resourceId)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions())//默认动画
                .into(imageView);
    }

    /**
     * 加载资源ID图片
     *
     * @param resources 资源ID
     * @param imageView
     */
    public void loadResources(int resources, ImageView imageView) {
        loadResources(resources, imageView, requestOptions);
    }

    /**
     * 加载资源ID图片
     *
     * @param resources 资源ID
     * @param imageView
     */
    public void loadResources(int resources, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(mContext)
                .load(resources)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions())//默认动画
                .into(imageView);
    }


    /**
     * 加载file 文件 图片
     *
     * @param file      文件
     * @param imageView
     */
    public void loadFile(File file, ImageView imageView) {
        loadFile(file, imageView, requestOptions);
    }

    /**
     * 加载file 文件 图片
     *
     * @param file      文件
     * @param imageView
     */
    public void loadFile(File file, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(mContext)
                .load(file)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions())//默认动画
                .into(imageView);
    }

    /**
     * 取消图片加载
     *
     * @param imageView
     */
    public void cleanImage(ImageView imageView) {
        Glide.with(mContext).clear(imageView);
    }

}
class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;

    public GlideRoundTransform() {
        this( 4);
    }

    public GlideRoundTransform( int dp) {
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    public static float getRadius() {
        return radius;
    }

    //    public String getId() {
//        return getClass().getName() + Math.round(radius);
//    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}