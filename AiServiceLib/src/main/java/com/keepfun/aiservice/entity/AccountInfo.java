package com.keepfun.aiservice.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author yang
 * @description
 * @date 2020/12/16 2:20 PM
 */
public class AccountInfo {
    /**
     * 账户金额 ,
     */
    private Double amount;
    /**
     * 币种 ,
     */
    private String currency;
    /**
     * 用户标识 ,
     */
    private String userUid;
    /**
     * 用户名
     */
    private String username;

    /**
     * 客服币种 ,
     */
    private String csCurrency;
    /**
     * 可兑换客服币 ,
     */
    private Double exchangeAmount;
//   private String exchangeRate (string, optional): 兑换比例 ,

    public Double getAmount() {
        return amount;
    }

    public String getAmountStr() {
        return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCsCurrency() {
        return csCurrency;
    }

    public void setCsCurrency(String csCurrency) {
        this.csCurrency = csCurrency;
    }

    public Double getExchangeAmount() {
        return exchangeAmount;
    }

    public String getExchangeAmountStr(){
        return new BigDecimal(exchangeAmount).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    public void setExchangeAmount(Double exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }
}
