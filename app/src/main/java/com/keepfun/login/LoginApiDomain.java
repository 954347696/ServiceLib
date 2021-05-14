package com.keepfun.login;

/**
 * @author yang
 * @description
 * @date 2020/9/5 6:21 PM
 */
public interface LoginApiDomain {

    /**
     * 获取短信验证码
     */
    String GET_SMS_CODE = "/app/user/v1/smsCode";
    /**
     * 密码登录接口
     */
    String LOGIN_PWD = "app/user/v1/loginPwd";
    /**
     * 短信验证码登录接口
     */
    String USER_LOGIN_CODE = "/app/user/v1/loginCode";
    /**
     * 个人基本信息
     */
    String USER_BASE_INFO = "/app/user/center/v1/userBasicInfo";

    /**
     * 国家列表
     */
    String GET_COUNTRY_LIST = "/gl/country/v1/list";
}
