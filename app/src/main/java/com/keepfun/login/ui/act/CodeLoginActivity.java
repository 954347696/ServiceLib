package com.keepfun.login.ui.act;

import android.Manifest;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.aiservice.threads.YLPoolExecutor;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.aiservice.utils.TimeCount;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.login.R;
import com.keepfun.login.entity.GlCountryEntity;
import com.keepfun.login.entity.UserBasicInfoBean;
import com.keepfun.login.net.KeepfunGetSmsTask;
import com.keepfun.login.net.KeepfunSmsCodeTask;
import com.keepfun.login.net.KeepfunUserInfoTask;
import com.keepfun.aiservice.ServiceSystem;
import com.keepfun.login.ui.act.PwdLoginActivity;

import java.util.concurrent.FutureTask;


/**
 * @author yang
 * @description
 * @date 2020/9/5 4:16 PM
 */
public class CodeLoginActivity extends PanActivity implements View.OnClickListener {

    EditText et_login_user;
    TextView tv_login_code;
    EditText et_login_code;

    ImageView iv_clear;
    TextView tv_country_code;

    private static final int REQUEST_COUNTRY_CODE = 0x11;
    private GlCountryEntity glCountryEntity;

    private TimeCount timeCount;

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_code_login;
    }

    @Override
    public void bindUI(View rootView) {
        et_login_user = findViewById(R.id.et_login_user);
        tv_login_code = findViewById(R.id.tv_login_code);
        et_login_code = findViewById(R.id.et_login_code);

        iv_clear = findViewById(R.id.iv_clear);
        tv_country_code = findViewById(R.id.tv_country_code);
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_login).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_login_code).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_pwd).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.iv_clear).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_country_code).setOnClickListener(new CheckClickListener(this));
        et_login_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = et_login_user.getText().toString();
                iv_clear.setVisibility(StringUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        timeCount = new TimeCount(60 * 1000, 1000, tv_login_code);
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_login_code.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                String user = et_login_user.getText().toString().trim();
                if (StringUtils.isEmpty(user)) {
                    showToast("请输入用户手机号");
                    return;
                }
                String pwd = et_login_code.getText().toString().trim();
                if (StringUtils.isEmpty(pwd)) {
                    showToast("请输入验证码");
                    return;
                }
                loginSmsCode(user, pwd);
                break;
            case R.id.tv_login_code:
                String phone = et_login_user.getText().toString().trim();
                if (StringUtils.isEmpty(phone)) {
                    showToast("请输入用户手机号");
                    return;
                }
                getSmsCode(phone);
                timeCount.start();
                break;
            case R.id.tv_pwd:
                finish();
                break;
            default:
                break;
        }
    }

    private void loginSmsCode(String user, String smsCode) {
        FutureTask<String> futureTask = new FutureTask<>(new KeepfunSmsCodeTask(user, smsCode, glCountryEntity == null ? "" : glCountryEntity.getId()));
        YLPoolExecutor.getInstance().execute(futureTask);
        try {
            String token = futureTask.get();
            if (token != null) {
                getUserInfo(token);
            } else {
                dismissLoading();
            }
        } catch (Exception e) {
            dismissLoading();
            LogUtils.e("AppKey认证失败,请联系相关技术人员 \nERROR=" + e.getMessage());
        }
    }

    private void getUserInfo(String result) {
        FutureTask<UserBasicInfoBean> futureTask = new FutureTask<>(new KeepfunUserInfoTask(result));
        YLPoolExecutor.getInstance().execute(futureTask);
        try {
            UserBasicInfoBean userInfo = futureTask.get();
            LogUtils.e("currentTime : " + System.currentTimeMillis());
            if (userInfo != null) {
                dismissLoading();
                LogUtils.e("getUserInfo userInfo : " + userInfo);
                ServiceUser user = new ServiceUser();
                user.setUserUid(userInfo.getUserNo());
                user.setUsername(userInfo.getPhone());
                user.setNickname(userInfo.getNickname());
                user.setAvatar(userInfo.getAvatar());
                user.setMobile(userInfo.getPhone());
                ServiceSystem.setUserInfo(user);
                ServiceSystem.startServiceList("code login");
                finish();
            }
        } catch (Exception e) {
            dismissLoading();
            LogUtils.e("用户信息获取失败,请联系相关技术人员 \nERROR=" + e.getMessage());
        }

    }

    private void getSmsCode(String phone) {
        FutureTask<Boolean> futureTask = new FutureTask<>(new KeepfunGetSmsTask(phone, glCountryEntity == null ? "" : glCountryEntity.getId()));
        YLPoolExecutor.getInstance().execute(futureTask);
        try {
            Boolean result = futureTask.get();
            if (result != null && result) {
                timeCount.start();
            }
        } catch (Exception e) {
            dismissLoading();
            LogUtils.e("AppKey认证失败,请联系相关技术人员 \nERROR=" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_COUNTRY_CODE) {
            glCountryEntity = (GlCountryEntity) data.getSerializableExtra("country");
            tv_country_code.setText("+" + glCountryEntity.getCode());
        }
    }
}
