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
// * 拍照 fragment
// * @author guchenyang
// * @Date 2019/5/17
// * @time 10:30
// */
//public abstract class BaseTakePhotoFragment extends BaseFragment implements TakePhoto.TakeResultListener, InvokeListener {
//
//    private static final String TAG = BaseTakePhotoFragment.class.getName();
//    private InvokeParam invokeParam;
//    private TakePhoto takePhoto;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        getTakePhoto().onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        getTakePhoto().onSaveInstanceState(outState);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        getTakePhoto().onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
//    }
//
//    /**
//     * 获取TakePhoto实例
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
//        Toast.makeText(getActivity(),"用户取消操作",Toast.LENGTH_SHORT).show();
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
//    /**
//     * 成功回调
//     * @param result
//     */
//    protected abstract void onSuccess(TResult result);
//}
