package com.keepfun.aiservice.network;

import java.lang.reflect.Type;
import java.util.HashMap;


public abstract class HttpBasicRequest {

    public static final String LOG_TAG = "bihe0832 REQUEST";


    public static final String HTTP_REQ_ENTITY_MERGE = "=";
    public static final String HTTP_REQ_ENTITY_JOIN = "&";

    protected long requestTime = 0;
    public byte[] data = null;
    public HashMap<String, String> cookieInfo = new HashMap<>();

    public abstract String getUrl();

    public abstract boolean isArray();

    public abstract Class getReflectClass();
    public abstract Type getReflectType();

    public abstract HashMap<String, Object> getParams();

    public abstract HashMap<String, Object> getHeader();

    public abstract HttpResponseHandler getResponseHandler();

    public String requestMethod = BaseConnection.HTTP_REQ_METHOD_POST;

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

}
