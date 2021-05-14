package com.keepfun.aiservice.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.entity.ServiceEntity;
import com.keepfun.aiservice.entity.event.SetUserTopEvent;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;

import org.greenrobot.eventbus.EventBus;
/**
 * @author yang
 * @description
 * @date 2020/12/18 5:25 PM
 */
public class OnDutyDetailActivity extends PanActivity implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.service_activity_on_duty_detail;
    }

    private ImageView iv_avatar;
    private TextView tv_nickname;
    private Switch switch_top;

    private ServiceEntity mServiceEntity;

    @Override
    public void bindUI(View rootView) {
        iv_avatar = findViewById(R.id.iv_avatar);
        tv_nickname = findViewById(R.id.tv_nickname);
        switch_top = findViewById(R.id.switch_top);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mServiceEntity = (ServiceEntity) bundle.getSerializable(Arguments.DATA);
            Glide.with(this).load(mServiceEntity.getAvatarUrl()).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
            tv_nickname.setText(mServiceEntity.getAllName());
            switch_top.setChecked(mServiceEntity.isTop());
        }
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.layout_chat_history).setOnClickListener(new CheckClickListener(this));
        switch_top.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setServiceTop(mServiceEntity.getId());
        });
    }

    private void setServiceTop(long csUserId) {
        OkHttpUtils.postJson(ApiDomain.SET_CSUSER_TOP + "?serviceId=" + csUserId + "&optType=" + (mServiceEntity.isTop() ? 2 : 1), new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                setTopResult(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                setTopResult(result);
            }
        });
    }

    public void setTopResult(Boolean result) {
        if (result) {
            mServiceEntity.setTop(!mServiceEntity.isTop());
        }
        EventBus.getDefault().post(new SetUserTopEvent(String.valueOf(mServiceEntity.getId()), mServiceEntity.isTop()));
        switch_top.setChecked(mServiceEntity.isTop());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.layout_chat_history) {
            Bundle bundle = new Bundle();
            bundle.putString(Arguments.DATA, String.valueOf(mServiceEntity.getId()));
            bundle.putInt(Arguments.DATA1, 1);
            ActivityUtils.startActivity(bundle, ChatSearchActivity.class);
        }
    }
}
