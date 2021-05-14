package com.keepfun.aiservice.network;


import android.text.TextUtils;

import com.keepfun.aiservice.entity.ResponseData;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.blankj.util.JsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;


/**
 * 网络请求实例类的基类，所有具体的网络请求都是他的实现
 */

public abstract class HttpAdvancedListener{

    public abstract String getUrl();

    public HashMap<String, Object> getParams() {
        return null;
    }

    public HashMap<String, Object> getHeader() {
        return null;
    }


}
