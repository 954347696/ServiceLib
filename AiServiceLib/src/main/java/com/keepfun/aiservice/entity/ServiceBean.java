package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/8 10:36 AM
 */
public class ServiceBean {
    /**
     * 客服信息 ,
     */
    private UserInfo csUser;
    /**
     * : 主键 ,
     */
    private long id;

    private String avatarUrl;
    /**
     * : 商户id ,
     */
    private long shopId;
    /**
     * 客户等待人数, -1 不需要等待
     */
    private int waitNo;

    public ServiceEntity getService() {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setId(getId());
        serviceEntity.setShopId(getShopId());
        serviceEntity.setWaitNo(getWaitNo());
        serviceEntity.setAccountType(getCsUser().getAccountType());
        serviceEntity.setAvatarUrl(getAvatarUrl());
        serviceEntity.setConversationType(getCsUser().getConversationType());
        serviceEntity.setName(getCsUser().getName());
        serviceEntity.setNameEn(getCsUser().getNameEn());
        return serviceEntity;
    }

    public UserInfo getCsUser() {
        if (csUser == null) {
            csUser = new UserInfo();
        }
        return csUser;
    }

    public void setCsUser(UserInfo csUser) {
        this.csUser = csUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getWaitNo() {
        return waitNo;
    }

    public void setWaitNo(int waitNo) {
        this.waitNo = waitNo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public class UserInfo {
        /**
         * 账号类型: 1-官网、2-电服、3-社群，4-主播客服 ,
         */
        private int accountType;
        /**
         * 头像 ,
         */
        private String avatarUrl;
        /**
         * 公司名称 ,
         */
        private String companyName;
        /**
         * 对话类型 0人工服务， 1VIP专线，2视频通话，3语音通话，4不接待 ,
         */
        private int conversationType;
        /**
         * 当前会话 ,
         */
        private String curSessionNum;
        /**
         * 本日会话 ,
         */
        private String daySessionNum;
        /**
         * 分组id ,
         */
        private String groupId;
        /**
         * 主键 ,
         */
        private String id;
        /**
         * 客服名 ,
         */
        private String name;
        /**
         * 客服英文名 ,
         */
        private String nameEn;
        /**
         * 备注 ,
         */
        private String remark;
        /**
         * 商户id ,
         */
        private String shopId;
        /**
         * 商户名称 ,
         */
        private String shopName;
        /**
         * 用户id ,
         */
        private String sysUserId;
        /**
         * 等待接入 ,
         */
        private String waitJoinNum;
        /**
         * 工作状态: 0-签出 1-在线 2-事忙
         */
        private int workStatus;

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public int getConversationType() {
            return conversationType;
        }

        public void setConversationType(int conversationType) {
            this.conversationType = conversationType;
        }

        public String getCurSessionNum() {
            return curSessionNum;
        }

        public void setCurSessionNum(String curSessionNum) {
            this.curSessionNum = curSessionNum;
        }

        public String getDaySessionNum() {
            return daySessionNum;
        }

        public void setDaySessionNum(String daySessionNum) {
            this.daySessionNum = daySessionNum;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getSysUserId() {
            return sysUserId;
        }

        public void setSysUserId(String sysUserId) {
            this.sysUserId = sysUserId;
        }

        public String getWaitJoinNum() {
            return waitJoinNum;
        }

        public void setWaitJoinNum(String waitJoinNum) {
            this.waitJoinNum = waitJoinNum;
        }

        public int getWorkStatus() {
            return workStatus;
        }

        public void setWorkStatus(int workStatus) {
            this.workStatus = workStatus;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "accountType=" + accountType +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", conversationType=" + conversationType +
                    ", curSessionNum='" + curSessionNum + '\'' +
                    ", daySessionNum='" + daySessionNum + '\'' +
                    ", groupId='" + groupId + '\'' +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", nameEn='" + nameEn + '\'' +
                    ", remark='" + remark + '\'' +
                    ", shopId='" + shopId + '\'' +
                    ", shopName='" + shopName + '\'' +
                    ", sysUserId='" + sysUserId + '\'' +
                    ", waitJoinNum='" + waitJoinNum + '\'' +
                    ", workStatus=" + workStatus +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                " id=" + id +
                ", csUser=" + csUser +
                ", shopId=" + shopId +
                '}';
    }
}
