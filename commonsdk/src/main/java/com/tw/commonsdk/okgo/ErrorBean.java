package com.tw.commonsdk.okgo;

/**
 * @Author: Sunzhipeng
 * @Date 2019/5/29
 * @Time 11:30
 */
public class ErrorBean {

    /**
     * timestamp : 2019-05-29T11:29:32.862+0800
     * status : 400
     * error : Bad Request
     * message : 账号或密码错误
     * path : /api/login
     */

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
