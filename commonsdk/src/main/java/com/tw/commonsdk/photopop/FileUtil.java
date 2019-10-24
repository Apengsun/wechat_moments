package com.tw.commonsdk.photopop;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;

import com.tw.commonsdk.R;
import com.tw.commonsdk.utils.Logger;

import java.io.File;


public class FileUtil {
    private static final String CAPTURE_FILE_NAME = "_capture.jpg";

    /**
     * 获取应用通过相机保存图片的位置
     *
     * @param context
     *
     * @return
     */
    public static String getStoragePathIfMounted(Context context) {

        File dir = context.getFilesDir();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory();
        }

        //App的名字
        String name = context.getString(R.string.app_name);
        //相机图片保存的文件夹路径
        File storageFolder = buildPath(dir, name);
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }

        Logger.e("storage path", storageFolder.getAbsolutePath());

        return storageFolder.getAbsolutePath();
    }

    //获得照片的名字
    public static File getCaptureFile(Context context) {
        String name = SystemClock.elapsedRealtime() + CAPTURE_FILE_NAME;
        File captureFile = new File(getStoragePathIfMounted(context), name);
        return captureFile;
    }

    public static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (cur == null) {
                cur = new File(segment);
            } else {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }
}
