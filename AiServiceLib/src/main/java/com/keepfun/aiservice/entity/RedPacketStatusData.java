package com.keepfun.aiservice.entity;

/**
 * 红包状态数据<p>
 *
 * @author zixuefei
 * @since 2020/7/30 19:52
 */
public class RedPacketStatusData {

    /**
     * curDrawAmount (integer, optional): 当前用户已领取金额 ,
     * curDrawStatus (string, optional): 当前用户领取状态(0-待领取 1-已领取 2-限制领取) ,
     * curIsRedSender (string, optional): 当前用户是否为红包发放人：0-否 1-是 ,
     * redId (integer, optional): 红包id ,
     * redSendUserVo (RedSendUserVo, optional): 红包发放人用户信息 ,
     * redStatus (string, optional): 红包状态(0-正常未领完 1-已领完 2-已过期)
     * redSendUserVo : {"userId":17,"nickname":"缥缈叶","avatar":"http://47.103.98.44:8021/image/avatar/2020/07/28/1595927743828384.jpg"}
     */

    private long redId;
    private Integer redStatus;
    private Integer curDrawStatus;
    private int curIsRedSender;
    private float curDrawAmount;
    private RedSendUserVoBean redSendUserVo;

    public long getRedId() {
        return redId;
    }

    public void setRedId(long redId) {
        this.redId = redId;
    }

    public Integer getRedStatus() {
        return redStatus;
    }

    public void setRedStatus(Integer redStatus) {
        this.redStatus = redStatus;
    }

    public Integer getCurDrawStatus() {
        return curDrawStatus;
    }

    public void setCurDrawStatus(Integer curDrawStatus) {
        this.curDrawStatus = curDrawStatus;
    }

    public int getCurIsRedSender() {
        return curIsRedSender;
    }

    public void setCurIsRedSender(int curIsRedSender) {
        this.curIsRedSender = curIsRedSender;
    }

    public float getCurDrawAmount() {
        return curDrawAmount;
    }

    public void setCurDrawAmount(float curDrawAmount) {
        this.curDrawAmount = curDrawAmount;
    }

    public RedSendUserVoBean getRedSendUserVo() {
        if (redSendUserVo == null) {
            redSendUserVo = new RedSendUserVoBean();
        }
        return redSendUserVo;
    }

    public void setRedSendUserVo(RedSendUserVoBean redSendUserVo) {
        this.redSendUserVo = redSendUserVo;
    }

    public static class RedSendUserVoBean {
        /**
         * userId : 17
         * nickname : 缥缈叶
         * avatar : http://47.103.98.44:8021/image/avatar/2020/07/28/1595927743828384.jpg
         */

        private long userId;
        private String nickname;
        private String avatar;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
