package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/28 2:29 PM
 */
public class StatusBean {
    /**
     * 禁言状态(0-正常，1-禁言中)
     */
    private int status;
    /**
     * 1 所有人 2 仅管理员
     */
    private int root;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }
}
