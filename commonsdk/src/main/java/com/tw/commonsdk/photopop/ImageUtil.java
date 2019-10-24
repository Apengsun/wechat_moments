package com.tw.commonsdk.photopop;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.tw.commonsdk.utils.Logger;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图像处理的工具
 */
public class ImageUtil {

    public static void cropImageUri(Fragment fragment, Uri uri, int outputX, int outputY, int requestCode,
                                    Uri resultUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection


        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 将onPreviewFrame()中的data[]转换成Bitmap
     *
     * @param data
     * @param width
     * @param height
     *
     * @return
     */
    public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }

        Bitmap bmp = Bitmap.createBitmap(width, height, Config.RGB_565);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    /**
     * 保存方法
     */
    public static void saveBitmap(Bitmap bm, String path, String filename) {
        //TODO
        Logger.e("保存图片>>>>" + path + filename);
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();//如果文件夹不存在，则创建
        }
        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bm != null) {
                bm.compress(Bitmap.CompressFormat.JPEG, 70, out);
            }
            out.flush();
            out.close();
            Logger.e("已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * Utility method for downsampling images.
     *
     * @param path    the file path
     * @param data    if file path is null, provide the image data directly
     * @param target  the target dimension
     * @param isWidth use width as target, otherwise use the higher value of height
     *                or width
     * @param round   corner radius
     *
     * @return the resized image
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getResizedImage(String path, byte[] data, int target,
                                         boolean isWidth, int round) {

        Options options = null;

        if (target > 0) {

            Options info = new Options();
            info.inJustDecodeBounds = true;
            //设置这两个属性可以减少内存损耗
            info.inInputShareable = true;
            info.inPurgeable = true;

            //TODO
            decode(path, data, info);

            int dim = info.outWidth;
            if (!isWidth)
                dim = Math.max(dim, info.outHeight);
            int ssize = sampleSize(dim, target);

            options = new Options();
            options.inSampleSize = ssize;

        }

        Bitmap bm = null;
        try {
            bm = decode(path, data, options);
        } catch (OutOfMemoryError e) {
            L.red(e.toString());
            e.printStackTrace();
        }

        if (round > 0) {
            bm = getRoundedCornerBitmap(bm, round);
        }

        return bm;

    }

    private static Bitmap decode(String path, byte[] data,
                                 Options options) {

        Bitmap result = null;

        if (path != null) {

            result = decodeFile(path, options);

        } else if (data != null) {

            result = BitmapFactory.decodeByteArray(data, 0, data.length,
                    options);

        }

        if (result == null && options != null && !options.inJustDecodeBounds) {
            //TODO L.red("decode image failed" + path);
            L.red("decode image failed" + path);
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    private static Bitmap decodeFile(String path, Options options) {

        Bitmap result = null;

        if (options == null) {
            options = new Options();
        }

        options.inInputShareable = true;
        options.inPurgeable = true;

        FileInputStream fis = null;

        try {

            fis = new FileInputStream(path);

            FileDescriptor fd = fis.getFD();

            result = BitmapFactory.decodeFileDescriptor(fd, null, options);

        } catch (IOException e) {
            //            L.red(e.toString());
            //TODO  decodeFile(String path, Options options)
            Logger.e(" decodeFile(String path, Options options)>>>" + e.getMessage());
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    private static int sampleSize(int width, int target) {

        int result = 1;

        for (int i = 0; i < 10; i++) {

            if (width < target * 2) {
                break;
            }

            width = width / 2;
            result = result * 2;

        }

        return result;
    }

    /**
     * 获取圆角的bitmap
     *
     * @param bitmap
     * @param pixels
     *
     * @return
     */
    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * auto fix the imageOrientation
     *
     * @param bm   source
     * @param iv   imageView  if set invloke it's setImageBitmap() otherwise do nothing
     * @param uri  image Uri if null user path
     * @param path image path if null use uri
     */
    public static Bitmap autoFixOrientation(Bitmap bm, ImageView iv, Uri uri, String path) {
        int deg = 0;
        try {
            ExifInterface exif = null;
            if (uri == null) {
                exif = new ExifInterface(path);
            } else if (path == null) {
                exif = new ExifInterface(uri.getPath());
            }

            if (exif == null) {
                L.red("exif is null check your uri or path");
                return bm;
            }

            String rotate = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int rotateValue = Integer.parseInt(rotate);
            System.out.println("orientetion : " + rotateValue);
            switch (rotateValue) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    deg = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    deg = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    deg = 270;
                    break;
                default:
                    deg = 0;
                    break;
            }
        } catch (Exception ee) {
            Logger.d("catch img error", "return");
            if (iv != null)
                iv.setImageBitmap(bm);
            return bm;
        }
        Matrix m = new Matrix();
        m.preRotate(deg);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

        //bm = Compress(bm, 75);
        if (iv != null)
            iv.setImageBitmap(bm);
        return bm;
    }

    /**
     * @param bmp
     *
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 13; // 值越小图片会越亮，越大则越暗 

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Logger.d("may", "used time=" + (end - start));
        return bitmap;
    }

    public static class L {

        public static void red(Object o) {
            Logger.e(">>>>>>>ImageUtil", o.toString());
        }

    }

    /**
     * 为图片着色
     *
     * @param drawable
     * @param colors
     *
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotatingImageView(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }
}