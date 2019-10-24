package com.tw.commonsdk.photopop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw.commonsdk.R;
import com.tw.commonsdk.base.BaseFragment;
import com.tw.commonsdk.utils.AppUtils;
import com.tw.commonsdk.utils.PermissionHelper;
import com.tw.commonsdk.utils.USDCard;

import java.io.File;

/**
 * @author jarrah
 */
public class FragmentPhtotoPop extends BaseFragment implements ReqeustCode {

    private Dialog dialog;

    protected File captureFile;

    PermissionHelper mPHelper;
    boolean isNeedCorp = false;

    @SuppressLint("InflateParams")
    protected void popup(final Context context) {
        if (mPHelper == null) {
            mPHelper = new PermissionHelper(this);
        }
        mPHelper.requestPermissions("请授予" + getResources().getString(R.string.app_name) + "[相机]，[读写]权限", new
                PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        LayoutInflater inflater =
                                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View holder = inflater.inflate(R.layout.view_popup_button, null, false);
                        View gallery = holder.findViewById(R.id.btnPhoto);
                        View capture = holder.findViewById(R.id.btnCapture);
                        View cancel = holder.findViewById(R.id.btnCanel);

                        ButtonClick click = new ButtonClick((Activity) context);
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

    @SuppressLint("InflateParams")
    protected void popup(final Context context, boolean isNeedCorp) {
        this.isNeedCorp = isNeedCorp;
        if (mPHelper == null) {
            mPHelper = new PermissionHelper(this);
        }
        mPHelper.requestPermissions("请授予" + getResources().getString(R.string.app_name) + "[相机]，[读写]权限", new
                PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        LayoutInflater inflater =
                                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View holder = inflater.inflate(R.layout.view_popup_button, null, false);
                        View gallery = holder.findViewById(R.id.btnPhoto);
                        View capture = holder.findViewById(R.id.btnCapture);
                        View cancel = holder.findViewById(R.id.btnCanel);

                        ButtonClick click = new ButtonClick((Activity) context);
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
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
        return null;
    }

    public class ButtonClick implements View.OnClickListener {

        private Activity activity;

        public ButtonClick(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {

            if (dialog != null) {
                //点击屏幕其他位置使其不见
                dialog.dismiss();
            }

            //从相册中获取照片
            if (v.getId() == R.id.btnPhoto) {
                PhotoPicker.launchGallery(FragmentPhtotoPop.this, FROM_GALLERY);
            }

            //从相机中获取照片
            if (v.getId() == R.id.btnCapture) {
                captureFile = FileUtil.getCaptureFile(activity);
                PhotoPicker.launchCamera(FragmentPhtotoPop.this, FROM_CAPTURE, captureFile);
            }
        }
    }

    private File file;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {

            if (requestCode == FROM_GALLERY) {
                if (data != null) {
                    String path = PhotoPicker
                            .getPhotoPathByLocalUri(getActivity(), data);
                    if (isNeedCorp) {
                        Uri oldUri = getReallyUri(new File(path));
                        file = new File(USDCard.getPath("pictures"),
                                System.currentTimeMillis() + ".png");
                        Uri newUri = Uri.fromFile(file);
                        ImageUtil.cropImageUri(FragmentPhtotoPop.this, oldUri, 240, 240, FROM_CROP, newUri);
                        return;
                    }
                    onGalleryComplete(path);
                }
            } else if (requestCode == FROM_CAPTURE) {
                if (isNeedCorp) {
                    Uri oldUri = getReallyUri(captureFile);
                    file = new File(USDCard.getPath("pictures"),
                            System.currentTimeMillis() + ".png");
                    Uri newUri = Uri.fromFile(file);
                    ImageUtil.cropImageUri(FragmentPhtotoPop.this, oldUri, 240, 240, FROM_CROP, newUri);
                    return;
                }

                onCaptureComplete(captureFile);

            } else if (requestCode == FROM_CROP) {
                if (data != null) {
                    onCropComplete(file);
                }
            }

        }
    }

    protected void onGalleryComplete(String path) {

    }

    protected void onCropComplete(File file) {

    }

    protected void onCaptureComplete(File captureFile) {

    }

    private Uri getReallyUri(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getContext(),
                    AppUtils.getPackageName(getContext()) + ".FileProvider", file);
            return contentUri;
        } else {
            return Uri.fromFile(file);
        }
    }

}
