package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/12/17 8:28 PM
 */
public class ReceiveAmount {
    private long csUserId; //客服id（红包发送人）
    private String csUserName; //客服名称（红包发送人）
    private String amount; //领取金额
    private String currency;//币种名称

    public long getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(long csUserId) {
        this.csUserId = csUserId;
    }

    public String getCsUserName() {
        return csUserName;
    }

    public void setCsUserName(String csUserName) {
        this.csUserName = csUserName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
