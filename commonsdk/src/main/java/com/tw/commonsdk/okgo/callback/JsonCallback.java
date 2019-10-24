package com.tw.commonsdk.okgo.callback;

import com.tw.commonsdk.core.RouterHub;
import com.tw.commonsdk.utils.Constants;
import com.tw.commonsdk.utils.NavigationUtils;
import com.tw.commonsdk.utils.ToastUtils;
import com.lzy.okgo.callback.AbsCallback;

import org.simple.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.Response;

/**
 * @Author: Sunzhipeng
 * @Date 2019/5/29
 * @Time 9:48
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {
    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    @Override
    public void onError(com.lzy.okgo.model.Response response) {
        super.onError(response);
        Throwable throwable = response.getException();
        if (throwable != null) {
            if (throwable instanceof IllegalStateException) {
                String message = throwable.getMessage();
                if ("认证失败".equals(message)) {
                    NavigationUtils.navigation(RouterHub.LOG_LOGIN_ACTIVITY);
                    EventBus.getDefault().post("", Constants.FINISH_ACTIVITY);
                    ToastUtils.showToast(throwable.getMessage());
                    return;
                }
                if ("获取Token失败".equals(message)) {
                    ToastUtils.showToast("未能成功连接聊天服务器，请尝试重新登录。");
                }else {
                    ToastUtils.showToast(throwable.getMessage());
                }
            } else if (throwable instanceof UnknownHostException) {
                ToastUtils.showToast("未检测到网络...");
            } else {
                ToastUtils.showToast("code=" + response.code() + "   " + throwable.getMessage());
            }
        }
//        EventBus.getDefault().post("", Constants.FINISH_SPLASH_ACTIVITY);
//        EventBus.getDefault().post("", Constants.FINISH_DIALOG_CALLBACK);
    }
}
