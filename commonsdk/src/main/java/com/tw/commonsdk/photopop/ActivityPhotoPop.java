package com.tw.commonsdk.photopop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;

import com.tw.commonsdk.R;
import com.tw.commonsdk.base.BaseActivity;
import com.tw.commonsdk.utils.PermissionHelper;

import java.io.File;

//import com.guoxin.unlock.utils.GlideApp;

/**
 * @author jarrah
 */
public class ActivityPhotoPop extends BaseActivity implements ReqeustCode {

    protected File captureFile;
    private Dialog dialog;
    PermissionHelper mPHelper;

    @SuppressLint("InflateParams")
    protected void popup(final Context context) {
        if (mPHelper == null) {
            mPHelper = new PermissionHelper(this);
        }
        mPHelper.requestPermissions("请授予" + getResources().getString(R.string.app_name) + "[相机]，[读写]权限", new
                PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View holder = inflater.inflate(R.layout.view_popup_button, null, false);
                        View gallery = holder.findViewById(R.id.btnPhoto);
                        View capture = holder.findViewById(R.id.btnCapture);
                        View cancel = holder.findViewById(R.id.btnCanel);

                        ButtonClick click = new ButtonClick(ActivityPhotoPop.this);
                        gallery.setOnClickListener(click);
                        capture.setOnClickListener(click);
                        cancel.setOnClickListener(click);

                        dialog = PopupUtil.makePopup(context, holder);
                        dialog.show();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void doAfterDenied(String... permissions) {
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
                .READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == FROM_GALLERY) {
                if (data != null) {
                    String path = PhotoPicker
                            .getPhotoPathByLocalUri(this, data);
                    onGalleryComplete(path);
                }
            } else if (requestCode == FROM_CAPTURE) {

                onCaptureComplete(captureFile);

            } else if (requestCode == FROM_CROP) {
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    onCropComplete(bitmap);
                }
            }

        }
    }

    protected void onGalleryComplete(String path) {

    }

    protected void onCropComplete(Bitmap bitmap) {

    }

    protected void onCaptureComplete(File captureFile) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return 0;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    public class ButtonClick implements View.OnClickListener {

        private Activity activity;

        public ButtonClick(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {

            if (dialog != null) {
                dialog.dismiss();
            }

            if (v.getId() == R.id.btnPhoto) {
                PhotoPicker.launchGallery(activity, FROM_GALLERY);
            }

            if (v.getId() == R.id.btnCapture) {
                captureFile = FileUtil.getCaptureFile(activity);
                PhotoPicker.launchCamera(activity, FROM_CAPTURE, captureFile);
            }
        }
    }
}
