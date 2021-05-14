package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.entity.SkinEntity;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.SkinListActivity;
import com.keepfun.base.PanPresenter;

import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/9/15 7:37 PM
 */
public class SkinPresenter extends PanPresenter<SkinListActivity> {

    public synchronized void getSkinList() {
        OkHttpUtils.get(ApiDomain.SKIN_LIST, new GsonResponseHandler<List<SkinEntity>>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().getSkinFailed("");
            }

            @Override
            public void onSuccess(List<SkinEntity> list) {
                getV().getSkinSuccess(list);
            }
        });
    }
}
