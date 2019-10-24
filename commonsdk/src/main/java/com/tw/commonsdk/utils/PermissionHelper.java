package com.tw.commonsdk.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.tw.commonsdk.R;
import com.tw.commonsdk.base.BaseActivity;
import com.tw.commonsdk.base.BaseApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author deadline
 * @time 2016-10-28
 * @usage android >=M 的权限申请统一处理
 * <p>
 * notice:
 * 很多手机对原生系统做了修改，比如小米4的6.0的shouldShowRequestPermissionRationale
 * 就一直返回false，而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了, 因此有了
 * void doAfterDenied(String... permission);
 */

public class PermissionHelper {

    private static final int REQUEST_PERMISSION_CODE = 1000;

    private Object mContext;

    private PermissionListener mListener;

    private List<String> mPermissionList;
    private Map<String, String> mPermissionMap;

    /**
     * group:android.permission-group.CONTACTS
     * permission:android.permission.WRITE_CONTACTS
     * permission:android.permission.GET_ACCOUNTS
     * permission:android.permission.READ_CONTACTS
     * <p>
     * group:android.permission-group.PHONE
     * permission:android.permission.READ_CALL_LOG
     * permission:android.permission.READ_PHONE_STATE
     * permission:android.permission.CALL_PHONE
     * permission:android.permission.WRITE_CALL_LOG
     * permission:android.permission.USE_SIP
     * permission:android.permission.PROCESS_OUTGOING_CALLS
     * permission:com.android.voicemail.permission.ADD_VOICEMAIL
     * <p>
     * group:android.permission-group.CALENDAR
     * permission:android.permission.READ_CALENDAR
     * permission:android.permission.WRITE_CALENDAR
     * <p>
     * group:android.permission-group.CAMERA
     * permission:android.permission.CAMERA
     * <p>
     * group:android.permission-group.SENSORS
     * permission:android.permission.BODY_SENSORS
     * <p>
     * group:android.permission-group.LOCATION
     * permission:android.permission.ACCESS_FINE_LOCATION
     * permission:android.permission.ACCESS_COARSE_LOCATION
     * <p>
     * group:android.permission-group.STORAGE
     * permission:android.permission.READ_EXTERNAL_STORAGE
     * permission:android.permission.WRITE_EXTERNAL_STORAGE
     * <p>
     * group:android.permission-group.MICROPHONE
     * permission:android.permission.RECORD_AUDIO
     * <p>
     * group:android.permission-group.SMS
     * permission:android.permission.READ_SMS
     * permission:android.permission.RECEIVE_WAP_PUSH
     * permission:android.permission.RECEIVE_MMS
     * permission:android.permission.RECEIVE_SMS
     * permission:android.permission.SEND_SMS
     * permission:android.permission.READ_CELL_BROADCASTS
     *
     * @param object
     */
    public PermissionHelper(@NonNull Object object) {
        checkCallingObjectSuitability(object);
        this.mContext = object;
        mPermissionMap = new HashMap<>();
        mPermissionMap.put(Manifest.permission.READ_CONTACTS, "通信录");
        mPermissionMap.put(Manifest.permission.READ_PHONE_STATE, "电话");
        mPermissionMap.put(Manifest.permission.READ_CALENDAR, "日历");
        mPermissionMap.put(Manifest.permission.CAMERA, "相机");
        mPermissionMap.put(Manifest.permission.BODY_SENSORS, "传感器");
        mPermissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION, "您的位置");
        mPermissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "存储");
        mPermissionMap.put(Manifest.permission.RECORD_AUDIO, "麦克风");
        mPermissionMap.put(Manifest.permission.READ_SMS, "短信");


    }


    /**
     * 权限授权申请
     *
     * @param hintMessage 要申请的权限的提示
     * @param permissions 要申请的权限
     * @param listener    申请成功之后的callback
     */
    public void requestPermissions(@NonNull CharSequence hintMessage,
                                   @Nullable PermissionListener listener,
                                   @NonNull final String... permissions) {

        if (listener != null) {
            mListener = listener;
        }

        mPermissionList = Arrays.asList(permissions);

        //没全部权限
        if (!hasPermissions(mContext, permissions)) {
//        if (true) {
            if (!isRequesting) {
                isRequesting = true;
                //需要向用户解释为什么申请这个权限
                boolean shouldShowRationale = false;
                for (String perm : permissions) {
                    //一个是true全是true
                    shouldShowRationale =
                            shouldShowRationale || shouldShowRequestPermissionRationale(mContext, perm);
                }

                final String[] permissionsNew = neeedPermissionsKey(mPermissionList).split("，");
                if (shouldShowRationale) {
                    hintMessage = "我们需要" + neeedPermissions(mPermissionList) + "权限\n才能正常使用";
                    showMessageOKCancel(hintMessage, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executePermissionsRequest(mContext, permissionsNew,
                                    REQUEST_PERMISSION_CODE);

                        }
                    });
                } else {
                    executePermissionsRequest(mContext, permissionsNew,
                            REQUEST_PERMISSION_CODE);
                }
            }

        } else if (mListener != null) { //有全部权限
            mListener.doAfterGrand(permissions);
//            executePermissionsRequest(mContext, permissions,
//                    REQUEST_PERMISSION_CODE);
        }
    }

    private String neeedPermissions(List<String> strings) {
        StringBuilder content = new StringBuilder();
        if (strings != null && strings.size() > 0) {
            for (String permission : strings) {
                if (!hasPermissions(mContext, permission)) {
                    if (mPermissionMap.containsKey(permission)) {
                        content.append(mPermissionMap.get(permission) + "，");
                    }
                }
            }
        }
        String str = "";
        if (content.toString().length() > 1) {
            str = content.toString().substring(0, content.toString().length() - 1);
        }
        return str;
    }

    private String neeedPermissionsKey(List<String> strings) {
        StringBuilder content = new StringBuilder();
        if (strings != null && strings.size() > 0) {
            for (String permission : strings) {
                if (!hasPermissions(mContext, permission)) {
                    content.append(permission + "，");
                }
            }
        }
        String str = "";
        if (content.toString().length() > 1) {
            str = content.toString().substring(0, content.toString().length() - 1);
        }
        return str;
    }


    /**
     * 处理onRequestPermissionsResult
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                boolean allGranted = true;
                for (int grant : grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }

                if (allGranted && mListener != null) {
                    mListener.doAfterGrand((String[]) mPermissionList.toArray());
                } else if (!allGranted && mListener != null) {
                    mListener.doAfterDenied((String[]) mPermissionList.toArray());
                    String hintMessage = "我们需要" + neeedPermissions(mPermissionList) + "权限\n才能正常使用\n请您开启";
                    showMessageOKCancel(hintMessage, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + ((BaseActivity) mContext).getPackageName()));
                            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                            ((BaseActivity) mContext).startActivityForResult(intent, REQUEST_PERMISSION_CODE);

                        }
                    });
                }
                isRequesting = false;
                break;
        }
    }

    /**
     * 判断是否具有某权限
     *
     * @param object
     * @param perms
     *
     * @return
     */
    public static boolean hasPermissions(@NonNull Object object, @NonNull String... perms) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        Logger.e("Build.VERSION.SDK_INT"+ Build.VERSION.SDK_INT);

        for (String perm : perms) {
            boolean hasPerm = checkPermission(perm);
//            boolean hasPerm = (ContextCompat.checkSelfPermission(getActivity(object), perm) ==
//                    PackageManager.PERMISSION_GRANTED);
//            boolean hasPerm = (PermissionChecker.checkSelfPermission(getActivity(object), perm) ==
//                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }

        }

        return true;
    }

    private static  boolean checkPermission(String permission)
    {

//        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = BaseApplication.getAppContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * 兼容fragment
     *
     * @param object
     * @param perm
     *
     * @return
     */
    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(@NonNull Object object, @NonNull String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    private boolean isRequesting = false;

    /**
     * 执行申请,兼容fragment
     *
     * @param object
     * @param perms
     * @param requestCode
     */
    @TargetApi(23)
    private void executePermissionsRequest(@NonNull Object object, @NonNull String[] perms, int requestCode) {
            if (object instanceof android.app.Activity) {
                ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(perms, requestCode);
            } else if (object instanceof android.app.Fragment) {
                ((android.app.Fragment) object).requestPermissions(perms, requestCode);
            }

    }

    /**
     * 检查传递Context是否合法
     *
     * @param object
     */
    private void checkCallingObjectSuitability(@Nullable Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof android.app.Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        if (!(isSupportFragment || isActivity || (isAppFragment && isNeedRequest()))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }


    @TargetApi(11)
    private static Activity getActivity(@NonNull Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    public static boolean isNeedRequest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void showMessageOKCancel(CharSequence message, DialogInterface.OnClickListener okListener) {
     new AlertDialog.Builder(getActivity(mContext))
                .setTitle("提示！")
                .setMessage(message)
                .setPositiveButton(R.string.confirm, okListener)
                .setCancelable(false)//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
                .create()
                .show();
    }

    public interface PermissionListener {

        void doAfterGrand(String... permission);

        void doAfterDenied(String... permission);
    }
}