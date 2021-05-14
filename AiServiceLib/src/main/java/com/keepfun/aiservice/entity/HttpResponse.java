package com.keepfun.aiservice.entity;

public class HttpResponse {


    private int httpCode;
    private String result;

    public HttpResponse(int httpCode, String result) {
        this.httpCode = httpCode;
        this.result = result;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
