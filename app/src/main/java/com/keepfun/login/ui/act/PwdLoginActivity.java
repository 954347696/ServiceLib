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

import com.keepfun.aiservice.ServiceManager;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.ActivityUtils;
import com.keepfun.blankj.util.KeyboardUtils;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.aiservice.entity.ServiceUser;
import com.keepfun.aiservice.threads.YLPoolExecutor;
import com.keepfun.login.BuildConfig;
import com.keepfun.login.Constant;
import com.keepfun.login.LoginApiDomain;
import com.keepfun.login.R;
import com.keepfun.login.entity.GlCountryEntity;
import com.keepfun.login.entity.UserBasicInfoBean;
import com.keepfun.login.net.KeepfunLoginTask;
import com.keepfun.login.net.KeepfunUserInfoTask;
import com.keepfun.aiservice.ServiceSystem;

import java.util.HashMap;
import java.util.concurrent.FutureTask;

import okhttp3.Call;
import okhttp3.Response;


/**
 * @author yang
 * @description
 * @date 2020/9/5 4:16 PM
 */
public class PwdLoginActivity extends PanActivity implements View.OnClickListener {

    EditText et_login_user;
    EditText et_login_pwd;
    ImageView iv_clear;
    TextView tv_country_code;

    private static final int REQUEST_COUNTRY_CODE = 0x11;
    private GlCountryEntity glCountryEntity;

    @Override
    public int getLayoutId() {
        return R.layout.service_activity_pwd_login;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }


    @Override
    public void bindUI(View rootView) {
        et_login_user = findViewById(R.id.et_login_user);
        et_login_pwd = findViewById(R.id.et_login_pwd);
        iv_clear = findViewById(R.id.iv_clear);
        tv_country_code = findViewById(R.id.tv_country_code);
    }

    @Override
    public void initData() {
        if (BuildConfig.DEBUG) {
//            et_login_user.setText("13552711920");
//            et_login_user.setText("15021046479");
//            et_login_pwd.setText("a12345678");
            et_login_user.setText("15800000005");
            et_login_pwd.setText("12345678");
//            et_login_user.setText("18721673408");
//            et_login_pwd.setText("abc111111");
//            et_login_user.setText("15821767347");
//            et_login_pwd.setText("1234567a");
        }
    }

    @Override
    public void bindEvent() {
        findViewById(R.id.tv_login).setOnClickListener(new CheckClickListener(this));
        findViewById(R.id.tv_code).setOnClickListener(new CheckClickListener(this));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                KeyboardUtils.hideSoftInput(this);
                String user = et_login_user.getText().toString().trim();
                if (StringUtils.isEmpty(user)) {
                    showToast("请输入用户名/手机号");
                    return;
                }
                String pwd = et_login_pwd.getText().toString().trim();
                if (StringUtils.isEmpty(pwd)) {
                    showToast("请输入密码");
                    return;
                }
                showLoading();
                new Thread(() -> login(user, pwd)).start();
                break;
            case R.id.tv_code:
                ActivityUtils.startActivity(CodeLoginActivity.class);
                break;
            case R.id.iv_clear:
                et_login_user.setText("");
                break;
            case R.id.tv_country_code:
                ActivityUtils.startActivityForResult(this, SelectCountryActivity.class, REQUEST_COUNTRY_CODE);
                break;
            default:
                break;
        }
    }

    private void login(String user, String pwd) {
//        YLPoolExecutor.getInstance().shutdownNow();
        new ServiceManager().firstStep();

        //鉴权
        FutureTask<String> futureTask = new FutureTask<>(new KeepfunLoginTask(user, pwd, glCountryEntity == null ? "" : glCountryEntity.getId()));
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
            dismissLoading();
            if (userInfo != null) {
                LogUtils.e("getUserInfo userInfo : " + userInfo);
                ServiceUser user = new ServiceUser();
                user.setUserUid(userInfo.getUserNo());
                user.setUsername(userInfo.getPhone());
                user.setNickname(userInfo.getNickname());
                user.setAvatar(userInfo.getAvatar());
                user.setMobile(userInfo.getPhone());
                ServiceSystem.setUserInfo(user);
                ServiceSystem.startServiceList("password login");
//                ServiceSystem.startServiceListOnly("password login");
            }
        } catch (Exception e) {
            dismissLoading();
            LogUtils.e("用户信息获取失败,请联系相关技术人员 \nERROR=" + e.getMessage());
        }

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
