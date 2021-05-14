package com.keepfun.aiservice.ui.presenter;

import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.YLConstant;
import com.keepfun.aiservice.entity.GroupDetail;
import com.keepfun.aiservice.entity.event.DeleteGroupEvent;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.act.GroupChatDetailActivity;
import com.keepfun.base.PanPresenter;

import org.greenrobot.eventbus.EventBus;


/**
 * @author yang
 * @description
 * @date 2020/12/14 11:16 AM
 */
public class GroupChatDetailPresenter extends PanPresenter<GroupChatDetailActivity> {

    public void getDetailInfo(long groupId) {
        String url =  ApiDomain.GET_GROUP_INFO + "?" + Arguments.ID + "=" + groupId;
        OkHttpUtils.get(url, new GsonResponseHandler<GroupDetail>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().refreshUI(new GroupDetail());
            }

            @Override
            public void onSuccess(GroupDetail groupDetail) {
                if (groupDetail != null) {
                    getV().refreshUI(groupDetail);
                } else {
                    getV().refreshUI(new GroupDetail());
                }
            }
        });
    }

    public void setGroupTop(long groupId) {
        OkHttpUtils.get(ApiDomain.SET_GROUP_TOP + "?" + Arguments.ID + "=" + groupId, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                getV().setTopResult(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                getV().setTopResult(result);
            }
        });
    }

    public void deleteGroup(String groupId) {
        String url = ApiDomain.DELETE_GROUP + "?" + Arguments.ID + "=" + groupId + "&imToken=" + GlobalDataHelper.getInstance().getImToken();

        OkHttpUtils.get(url, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result != null && result) {
                    EventBus.getDefault().post(new DeleteGroupEvent(groupId));
                }
            }
        });

    }
}
