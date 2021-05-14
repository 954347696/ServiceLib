package com.keepfun.aiservice.entity;

public class ResponseData {


    private String appCode;
    private String appMsg;
    private Object result;
    private boolean success;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppMsg() {
        return appMsg;
    }

    public void setAppMsg(String appMsg) {
        this.appMsg = appMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "appCode='" + appCode + '\'' +
                ", appMsg='" + appMsg + '\'' +
                ", result=" + result +
                ", success=" + success +
                '}';
    }
}
