//package com.tw.commonsdk.base;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.devio.takephoto.app.TakePhoto;
//import org.devio.takephoto.app.TakePhotoImpl;
//import org.devio.takephoto.model.InvokeParam;
//import org.devio.takephoto.model.TContextWrap;
//import org.devio.takephoto.model.TResult;
//import org.devio.takephoto.permission.InvokeListener;
//import org.devio.takephoto.permission.PermissionManager;
//import org.devio.takephoto.permission.TakePhotoInvocationHandler;
//
///**
// * 拍照activity
// * @author guchenyang
// * @Date 2019/5/17
// * @time 9:47
// */
//public abstract class BaseTakePhotoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
//    private static final String TAG = BaseTakePhotoActivity.class.getName();
//    private TakePhoto takePhoto;
//    private InvokeParam invokeParam;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        getTakePhoto().onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        getTakePhoto().onSaveInstanceState(outState);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        getTakePhoto().onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
//    }
//
//    /**
//     * 获取TakePhoto实例
//     *
//     * @return
//     */
//    public TakePhoto getTakePhoto() {
//        if (takePhoto == null) {
//            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
//        }
//        return takePhoto;
//    }
//
//    @Override
//    public void takeSuccess(TResult result) {
//        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
////        ArrayList<TImage> imagesTalk=result.getImages(); 接收
//        onSuccess(result);
//    }
//
//    @Override
//    public void takeFail(TResult result, String msg) {
//        Log.i(TAG, "takeFail:" + msg);
//    }
//
//    @Override
//    public void takeCancel() {
//        Toast.makeText(BaseTakePhotoActivity.this,"用户取消操作",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
//        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
//        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
//            this.invokeParam = invokeParam;
//        }
//        return type;
//    }
//
//    /**
//     * 成功回调
//     * @param result
//     */
//    protected abstract void onSuccess(TResult result);
//}
