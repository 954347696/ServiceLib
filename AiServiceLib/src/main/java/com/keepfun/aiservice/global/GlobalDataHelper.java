package com.keepfun.aiservice.global;

import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.aiservice.utils.JsonUtil;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.blankj.util.SPUtils;
import com.keepfun.blankj.util.SpanUtils;

import java.util.HashMap;
import java.util.Map;

public class GlobalDataHelper {


    private static volatile GlobalDataHelper mInstance;
    private long backTime = -1;
    private String appKey;
    private String appSecret;
    private String privateKey;
    private String sourcePage;
    private boolean isShowHome = true;
    private ServiceUser mUserInfo;
    private ImUserInfo mImUserInfo;
    private boolean logEnable = false;
    private Map<String, Object> globalData;

    public static GlobalDataHelper getInstance() {
        if (mInstance == null) {
            synchronized (GlobalDataHelper.class) {
                if (mInstance == null) {
                    mInstance = new GlobalDataHelper();
                }
            }
        }
        return mInstance;
    }

    public GlobalDataHelper() {
        globalData = new HashMap<>();
    }

    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }

    public long getBackTime() {
        return backTime;
    }

    public void setBackTime(long backTime) {
        this.backTime = backTime;
    }

    public String getAccessToken() {
        return SpManager.getConfig().getString(SpManager.KEY_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        SpManager.getConfig().put(SpManager.KEY_ACCESS_TOKEN, accessToken);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getuUid() {
        return SpManager.getConfig().getString(SpManager.KEY_UID, "");
    }

    public void setuUid(String uUid) {
        SpManager.getConfig().put(SpManager.KEY_UID, uUid);
    }

    public ServiceUser getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        mUserInfo = JsonUtil.decode(SpManager.getConfig().getString(SpManager.KEY_USERINFO, ""), ServiceUser.class);
        return mUserInfo;
    }

    public void setUserInfo(ServiceUser mUserInfo) {
        this.mUserInfo = mUserInfo;
        SpManager.getConfig().put(SpManager.KEY_USERINFO, JsonUtil.encode(mUserInfo));
    }

    public ImUserInfo getImUserInfo() {
        if (mImUserInfo != null) {
            return mImUserInfo;
        }
        mImUserInfo = JsonUtil.decode(SpManager.getConfig().getString(SpManager.KEY_IM_USERINFO, ""), ImUserInfo.class);
        return mImUserInfo;
    }

    public void setImUserInfo(ImUserInfo mImUserInfo) {
        this.mImUserInfo = mImUserInfo;
        SpManager.getConfig().put(SpManager.KEY_IM_USERINFO, JsonUtil.encode(mImUserInfo));
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public boolean isShowHome() {
        return isShowHome;
    }

    public void setShowHome(boolean showHome) {
        isShowHome = showHome;
    }

    public void setData(String key, Object value) {
        globalData.put(key, value);
    }

    public void setImToken(String value) {
        SpManager.getConfig().put(ChatConst.CHAT_TOKEN, value);
    }

    public String getImToken() {
        return SpManager.getConfig().getString(ChatConst.CHAT_TOKEN, "");
    }

    public Object getData(String key) {
        if (globalData.containsKey(key)) {
            return globalData.get(key);
        }
        return null;
    }
}
