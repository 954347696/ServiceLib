package com.keepfun.aiservice;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.AdAndNoticeResponse;
import com.keepfun.aiservice.entity.BaseResponse;
import com.keepfun.aiservice.entity.WelcomeContent;
import com.keepfun.aiservice.manager.ChatKit;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.skin.loader.CustomSDCardLoader;
import com.keepfun.aiservice.ui.act.ServiceHomeActivity;
import com.keepfun.aiservice.ui.act.ServiceListActivity;
import com.keepfun.aiservice.utils.SignUtils;
import com.keepfun.aiservice.utils.SpManager;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.ColorUtils;
import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.NetworkUtils;
import com.keepfun.blankj.util.ToastUtils;
import com.keepfun.blankj.util.Utils;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.entity.ChatLoginEntity;
import com.keepfun.aiservice.entity.ImUserInfo;
import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.aiservice.entity.YlAuthData;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.manager.ImClient;
import com.keepfun.aiservice.manager.ServiceManagerInterface;
import com.keepfun.aiservice.threads.YLPoolExecutor;
import com.keepfun.aiservice.threads.YLRegInfoTask;
import com.keepfun.aiservice.utils.AppDeviceUtil;
import com.keepfun.aiservice.utils.AppFrontBackHelper;
import com.keepfun.easyphotos.setting.Setting;
import com.keepfun.media.Logger;
import com.keepfun.media.MediaSdk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;

/**
 * @author yang
 * @description
 * @date 2020/10/22 9:47 AM
 */
public class ServiceManager implements ServiceManagerInterface, AppFrontBackHelper.OnAppStatusListener {
    private static Application mApplication;
    public volatile static boolean hasInit = false;
    private String appKey;
    private String appSecret;
    private String privateKey;

    public static Context getContext() {
        if (mApplication == null) {
            LogUtils.e("???????????????");
            return null;
        }
        return mApplication;
    }

    @Override
    public void initialize(Application application, String appKey, String appSecret, String privateKey) {
        mApplication = application;
        Utils.init(application);
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.privateKey = privateKey;

        ChatKit.init(application);

        GlobalDataHelper.getInstance().setAppKey(appKey);
        GlobalDataHelper.getInstance().setAppSecret(appSecret);
        GlobalDataHelper.getInstance().setPrivateKey(privateKey);

//        LogcatHelper.getInstance(application).start();

//        OkioUtils.init(application);

        Logger.setLogLevel(Logger.LogLevel.LOG_DEBUG);
        Logger.setDefaultHandler();
        MediaSdk.initialize(application);
        SkinCompatManager.withoutActivity(application)
                // ??????????????????????????????zip???????????????
                .addStrategy(new CustomSDCardLoader())
                // ???????????????????????????
                .addInflater(new SkinAppCompatViewInflater())
                .loadSkin();

        boolean initDataAvailable = checkInitDataAvailable();
        if (!initDataAvailable) {
            return;
        }
        //??????app??????????????????
        String processName = AppDeviceUtil.getCurProcessName(application);
        if (processName == null || !processName.equals(application.getPackageName())) {
            return;
        }
        if (hasInit) {
            LogUtils.e("???????????????,??????????????????!");
            return;
        }

        ToastUtils.setBgColor(ColorUtils.getColor(R.color.color_c4Black));
        ToastUtils.setMsgColor(ColorUtils.getColor(R.color.white));
        ToastUtils.setMsgTextSize(16);

        firstStep();
    }

    public void startService(String sourcePage) {
        if (initInfo(sourcePage)) return;
        ActivityUtils.startActivity(ServiceHomeActivity.class);
    }

    public void startServiceList(String sourcePage) {
        if (initInfo(sourcePage)) return;
        ActivityUtils.startActivity(ServiceListActivity.class);
    }

    private boolean initInfo(String sourcePage) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("??????????????????????????????");
            return true;
        }
        if (!checkInitDataAvailable()) {
            LogUtils.e("?????????????????????sdk");
            return true;
        }
        if (!hasInit) {
            firstStep();
        }
        if (!hasInit) {
            LogUtils.e("?????????????????????sdk");
            return true;
        }

        if (!checkAccessTokenAvailable()) {
            return true;
        }
        if (!checkIsUserInit()) {
            return true;
        }
        GlobalDataHelper.getInstance().setSourcePage(sourcePage);
        return false;
    }

    public void setServiceHomeShow(boolean isShow) {
        GlobalDataHelper.getInstance().setShowHome(isShow);
    }

    public void setUserInfo(ServiceUser serviceUser) {
        GlobalDataHelper.getInstance().setuUid(serviceUser.getUserUid());
        regInfo(serviceUser);
        connectIm();
        secondStep();
    }

    protected boolean checkInitDataAvailable() {
        if (mApplication == null) {
            LogUtils.e("Application?????????NULL");
            return false;
        }
        if (TextUtils.isEmpty(appKey)) {
            LogUtils.e("appKey????????????");
            return false;
        }
        if (TextUtils.isEmpty(appSecret)) {
            LogUtils.e("appSecret????????????");
            return false;
        }
        if (TextUtils.isEmpty(privateKey)) {
            LogUtils.e("privateKey????????????");
            return false;
        }

        return true;
    }

    protected boolean checkAccessTokenAvailable() {
        if (TextUtils.isEmpty(GlobalDataHelper.getInstance().getAccessToken())) {
            LogUtils.e("appKey????????????");
            return false;
        }
        return true;
    }

    protected boolean checkIsUserInit() {
        if (GlobalDataHelper.getInstance().getUserInfo() == null) {
            LogUtils.e("????????????????????????");
            return false;
        }
        return true;
    }

    //TODO ?????????????????? ?????????????????? ??????????????????
    public void firstStep() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appKey", GlobalDataHelper.getInstance().getAppKey());
        params.put("appSecret", GlobalDataHelper.getInstance().getAppSecret());
        params.put("timestamp", System.currentTimeMillis());
        params.put("sign", SignUtils.sign(SignUtils.getSignContent(params), GlobalDataHelper.getInstance().getPrivateKey()));
        OkHttpUtils.postJson(ApiDomain.GET_ACCESS_TOKEN, params, new GsonResponseHandler<YlAuthData>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(YlAuthData ylAuthData) {
                if (ylAuthData != null) {
                    GlobalDataHelper.getInstance().setAccessToken(ylAuthData.getAccessToken());
                    realInit();
                    hasInit = true;
                    thirdStep();
                }
            }
        });
    }

    private void secondStep() {
        OkHttpUtils.get(ApiDomain.NOTICE_AND_AD_PIC_LIST, null, new GsonResponseHandler<AdAndNoticeResponse>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(AdAndNoticeResponse adAndNoticeResponse) {
                SpManager.getConfig().put(SpManager.KEY_BROADCAST, JsonUtils.encode(adAndNoticeResponse.getNoticeList()));
                SpManager.getConfig().put(SpManager.KEY_AD_PIC, JsonUtils.encode(adAndNoticeResponse.getAdPicList()));
            }
        });
    }

    private void thirdStep() {
        OkHttpUtils.getClient().get()
                .url(YLConstant.BASE_URL + ApiDomain.WELCOME_CONTENT + "?type=1")
                .headers(OkHttpUtils.getHeaders())
                .enqueue(new GsonResponseHandler<WelcomeContent>() {
                    @Override
                    public void onFailure(String statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(WelcomeContent welcomeContent) {
                        if (welcomeContent != null) {
                            SpManager.getConfig().put(SpManager.KEY_WELCOME_CONTENT, welcomeContent.getContent());
                        }
                    }
                });
    }

    private void regInfo(ServiceUser serviceUser) {
        if (!checkAccessTokenAvailable()) {
            firstStep();
            return;
        }
        FutureTask<ServiceUser> futureTask = new FutureTask<>(new YLRegInfoTask(serviceUser));
        YLPoolExecutor.getInstance().execute(futureTask);
        try {
            ServiceUser user = futureTask.get();
            if (user != null) {
                GlobalDataHelper.getInstance().setUserInfo(user);
            }
        } catch (Exception e) {
            LogUtils.e("????????????????????????,??????????????????????????? \nERROR=" + e.getMessage());
        }
    }

    public void connectIm() {
        getImToken();
    }

    private synchronized void getImToken() {
        if (!checkAccessTokenAvailable()) {
            return;
        }
        OkHttpUtils.postJson(ApiDomain.IM_LOGIN + "?" + Arguments.DEVICE + "=" + 1, new GsonResponseHandler<ChatLoginEntity>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(ChatLoginEntity user) {
                if (user != null) {
                    GlobalDataHelper.getInstance().setImToken(user.getToken());
                    GlobalDataHelper.getInstance().setImUserInfo(user.getUserInfoVo());

                    ImClient.getInstance().initChatClient(user.getUserInfoVo(), user.getToken());
                }
            }
        });
    }

    public void realInit() {
        //??????????????????
//        YLNetworkManager.getInstance().register();
        //???????????????????????????
        AppFrontBackHelper.getInstance().register(mApplication, this);
        LogUtils.e("???????????????");
    }


    @Override
    public void onRelease() {
        GlobalDataHelper.getInstance().setAccessToken("");
        AppFrontBackHelper.getInstance().unRegister(mApplication);
        hasInit = false;
    }

    @Override
    public void onFront() {
        //?????????????????????
        if (GlobalDataHelper.getInstance().getBackTime() == -1) {
            LogUtils.e("------???????????????APP------");
            return;
        }
    }

    @Override
    public void onBack() {
        GlobalDataHelper.getInstance().setBackTime(System.currentTimeMillis());
    }

}
