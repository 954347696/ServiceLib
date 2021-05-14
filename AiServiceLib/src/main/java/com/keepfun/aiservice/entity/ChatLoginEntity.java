package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/5 3:29 PM
 */
public class ChatLoginEntity {
    /**
     * token,登录后需要携带,退出时放到header ,
     */
    private String token;
    /**
     * userInfoVo，用户信息 ,
     */
    private ImUserInfo userInfoVo;
    /**
     * 登录人数、客服人数、咨询人数
     */
    private Object waitCountVo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ImUserInfo getUserInfoVo() {
        return userInfoVo;
    }

    public void setUserInfoVo(ImUserInfo userInfoVo) {
        this.userInfoVo = userInfoVo;
    }

    public Object getWaitCountVo() {
        return waitCountVo;
    }

    public void setWaitCountVo(Object waitCountVo) {
        this.waitCountVo = waitCountVo;
    }

    @Override
    public String toString() {
        return "ChatLoginEntity{" +
                "token='" + token + '\'' +
                ", userInfoVo=" + userInfoVo +
                ", waitCountVo=" + waitCountVo +
                '}';
    }
}
