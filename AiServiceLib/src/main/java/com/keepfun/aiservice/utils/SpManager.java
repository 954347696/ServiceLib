package com.keepfun.aiservice.utils;

import com.keepfun.blankj.util.JsonUtils;
import com.keepfun.blankj.util.SPUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.aiservice.constants.YLConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yang
 * @description
 * @date 2020/8/28 10:54 AM
 */
public class SpManager {

    private static final String FILE_NAME_HISTORY = "file_history";
    private static final String KEY_HISTORY = "history";
    private static final String FILE_NAME_CONFIG = "file_config";
    public static final String KEY_BROADCAST = "broadcast";
    public static final String KEY_AD_PIC = "ad_pic";
    public static final String KEY_SKIN = "select_skin";
    public static final String KEY_WELCOME_CONTENT = "welcome_content";
    public static final String KEY_UID = "uid";
    public static final String KEY_USERINFO = "userinfo";
    public static final String KEY_IM_USERINFO = "im_userinfo";
    public static final String KEY_ACCESS_TOKEN = "access_token";

    public static SPUtils getHistory() {
        return SPUtils.getInstance(FILE_NAME_HISTORY);
    }

    public static SPUtils getConfig() {
        return SPUtils.getInstance(FILE_NAME_CONFIG);
    }

    public static void saveHistory(String searchStr) {
        synchronized (SpManager.class) {
            List<String> history = getHistoryList();
            if (!history.contains(searchStr)) {
                if (history.size() >= YLConstant.HISTORY_MAX_COUNT) {
                    history.remove(0);
                }
                history.add(searchStr);
                getHistory().put(KEY_HISTORY, JsonUtils.encode(history));
            }
        }
    }

    public static List<String> getHistoryList() {
        String history = getHistory().getString(KEY_HISTORY);
        if (StringUtils.isEmpty(history)) {
            return new ArrayList<>();
        }
        return JsonUtils.jsonToStringList(history);
    }
}
