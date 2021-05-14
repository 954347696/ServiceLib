package com.keepfun.aiservice.threads;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.entity.event.SessionEndEvent;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.aiservice.constants.YLConstant;
import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 后台服务,app启动时开启.
 *
 * @author yang
 */

public class SessionCounterService {

    private static SessionCounterService pushService;

    /**
     * Timer不保证任务执行的十分精确。
     * Timer类的线程安全的。
     */
    private Timer timer = new Timer();
    private TimerTask task;
    private static boolean timerHasCanceled = false;

    private void startTimer() {
        task = new TimerTask() {
            @Override
            public void run() {
                LogUtils.e("定时器触发上传------");
                stopSession();
            }
        };
        if (timerHasCanceled) {
            timer = new Timer();
            timerHasCanceled = false;
        }
        timer.schedule(task, YLConstant.SESSION_DISCONNECT_DELAY_TIME);
    }

    public synchronized void refreshTimer() {
//        stopCounterService();
//        startCounterService();
    }

    public void stopSession() {
        Long groupId = (Long) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID);
        if (groupId == null || groupId == -1) {
            return;
        }
        boolean isAi = GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE) != null && (Boolean) GlobalDataHelper.getInstance().getData(ChatConst.SESSION_TYPE);
        String url=ApiDomain.FINISH_CHAT + "?" + Arguments.GROUP_ID + "=" + GlobalDataHelper.getInstance().getData(ChatConst.SESSION_ID)
                + "&botOrHuman=" + (isAi ? 0 : 1);
        OkHttpUtils.postJson(url, new GsonResponseHandler<String>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                EventBus.getDefault().post(new SessionEndEvent());
            }

            @Override
            public void onSuccess(String response) {
                EventBus.getDefault().post(new SessionEndEvent());
            }
        });
    }

    /**
     * 停止 推送 服务
     */
    public void stopCounterService() {
//        if (task != null && timer != null) {
//            timer.cancel();
//            task.cancel();
//            timerHasCanceled = true;
//        }
    }

    /**
     * 启动 定时推送服务
     */
    public synchronized void startCounterService() {
        getSingleInstance().startTimer();
    }

    /**
     * 获取 推送服务 单例
     *
     * @return
     */
    public static SessionCounterService getSingleInstance() {

        if (pushService == null) {
            //双重检查
            synchronized (SessionCounterService.class) {
                if (pushService == null) {
                    pushService = new SessionCounterService();
                }
            }
        }
        return pushService;
    }
}
