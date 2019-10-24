package com.tw.commonsdk.photopop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.widget.Toast;

import com.tw.commonsdk.BuildConfig;
import com.tw.commonsdk.utils.AppUtils;
import com.tw.commonsdk.utils.Logger;

import java.io.File;

//在onActivityResult方法中根据requestCode和resultCode来获取当前拍照的图片地址。
//注意：这里有个问题，在有些机型当中（如SamsungI939、note2等）遇见了当拍照并存储之后，intent当中得到的data为空：

/**
 * data = null 的情况主要是由于拍照的时候横屏了,导致重新create,
 * 普通的解决方法可以在sharedpreference里面保存拍照文件的路径(onSaveInstance保存),
 * 在onRestoreSaveInstance里面在获取出来. 最简单的可以用fileUtil 里面的一个静态变量保存起来..
 */

public class PhotoPicker {

    private static final String IMAGE_TYPE = "image/*";

    /**
     * 打开照相机
     *
     * @param fragment    当前的fragment
     * @param requestCode 拍照成功时fragment forResult 的时候的requestCode
     * @param photoFile   拍照完毕时,图片保存的位置
     */
    public static void launchCamera(Fragment fragment, int requestCode,
                                    File photoFile) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(fragment.getContext(),
                    AppUtils.getPackageName(fragment.getContext()) + ".FileProvider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }
        fragment.startActivityForResult(intent, requestCode);

        //        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        //        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开照相机
     *
     * @param activity    当前的activity
     * @param requestCode 拍照成功时activity forResult 的时候的requestCode
     * @param photoFile   拍照完毕时,图片保存的位置
     */
    public static void launchCamera(Activity activity, int requestCode,
                                    File photoFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider",
                    photoFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(photoFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivityForResult(intent, requestCode);


        //        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        //        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 本地照片调用
     *
     * @param fragment
     * @param requestCode
     */
    public static void launchGallery(Fragment fragment, int requestCode) {
        if (launchSys(fragment, requestCode)
                && launch3partyBroswer(fragment, requestCode)
                && launchFinally(fragment))
            ;
    }

    /**
     * 本地照片调用
     *
     * @param activity
     * @param requestCode
     */
    public static void launchGallery(Activity activity, int requestCode) {
        if (launchSys(activity, requestCode)
                && launch3partyBroswer(activity, requestCode)
                && launchFinally(activity))
            ;
    }

    /**
     * PopupMenu打开本地相册.
     */
    private static boolean launchSys(Fragment fragment, int actResultCode) {
        Logger.e(">>>", "launchSys");
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        try {
            fragment.startActivityForResult(intent, actResultCode);

        } catch (android.content.ActivityNotFoundException e) {

            return true;
        }

        return false;
    }

    /**
     * PopupMenu打开本地相册.
     */
    private static boolean launchSys(Activity activity, int actResultCode) {
        Logger.e(">>>", "launchSys");
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        try {
            activity.startActivityForResult(intent, actResultCode);

        } catch (android.content.ActivityNotFoundException e) {

            return true;
        }

        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private static boolean launch3partyBroswer(Fragment fragment, int requestCode) {
        Logger.e(">>>", "launch3partyBroswer");
        Toast.makeText(fragment.getActivity(), "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE); // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            fragment.startActivityForResult(wrapperIntent, requestCode);
        } catch (android.content.ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private static boolean launch3partyBroswer(Activity activity, int requestCode) {
        Logger.e(">>>", "launch3partyBroswer");
        Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE); // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            activity.startActivityForResult(wrapperIntent, requestCode);
        } catch (android.content.ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    private static boolean launchFinally(Fragment fragment) {
        Toast.makeText(fragment.getActivity(), "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG)
                .show();
        return false;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    private static boolean launchFinally(Activity activity) {
        Toast.makeText(activity, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG)
                .show();
        return false;
    }

    /**
     * 获取从本地图库返回来的时候的URI解析出来的文件路径
     *
     * @return
     */
    public static String getPhotoPathByLocalUri(Context context, Intent data) {
        Uri selectedImage = data.getData();
        //TODO 小米手机在这里游标是null
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        Logger.e("cursor----->" + cursor);
        if (cursor == null) {
            return getPath(context, selectedImage);
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Logger.e(">>>", "selectedImage------>" + selectedImage);
        return picturePath;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     *
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * 剪切图片 在 onActivityResult的时候 从 返回的 Intent 里面获取 data 就是bitmap >>>> Bitmap
     * bitmap = data.getParcelableExtra("data");
     *
     * @param activity
     * @param imagePath   文件路径
     * @param requestCode 返回码
     */
    public static void startCrop(Activity activity, String imagePath,
                                 int requestCode, boolean isLarge) {
        File f = new File(imagePath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(f), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4); // 裁剪框比例 4 : 3
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", isLarge ? 400 : 200);
        intent.putExtra("outputY", isLarge ? 300 : 150);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true); // 设置为true 的时候才能有返回
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
    }

}