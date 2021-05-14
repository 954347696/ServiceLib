package com.keepfun.aiservice.entity;

import java.util.List;

/**
 * 查看红包领取信息<p>
 *
 * @author zixuefei
 * @since 2020/7/17 17:36
 */
public class RedDrawData {

    /**
     * curDrawAmount (integer, optional): 当前用户已领取金额 ,
     * curDrawStatus (string, optional): 当前用户领取状态(0-待领取 1-已领取 2-限制领取) ,
     * curIsRedSender (string, optional): 当前用户是否为红包发放人：0-否 1-是 ,
     * drawRecordPageVo (PageVo«RedDrawRecordVo», optional): 红包领取记录列表 ,
     * hasDrawTotalAmount (integer, optional): 红包已领取总金额 ,
     * hasDrawTotalNumber (integer, optional): 红包已领取数量 ,
     * isAllDraw (string, optional): 是否已被全部领取(0:否 1:是) ,
     * redId (integer, optional): 红包id ,
     * redSendUserVo (RedSendUserVo, optional): 红包发放人用户信息 ,
     * totalAmount (integer, optional): 红包总金额 ,
     * totalNumber (integer, optional): 红包总数量
     * <p>
     * drawRecordPageVo : {"datas":[{"avatar":"string","drawAmount":0,"drawTime":"2020-07-17T08:02:10.208Z","isBestLuck":"string","nickname":"string","userId":0}],"page":0,"pageNum":0,"size":0,"total":0}
     * redSendUserVo : {"avatar":"string","nickname":"string","userId":0}
     */

    private float curDrawAmount;
    private Integer curDrawStatus;
    private Integer curIsRedSender;
    private DrawRecordPageVoBean drawRecordPageVo;
    private float hasDrawTotalAmount;
    private int hasDrawTotalNumber;
    private String isAllDraw;
    private int redId;
    private RedSendUserVoBean redSendUserVo;
    private float totalAmount;
    private int totalNumber;

    public float getCurDrawAmount() {
        return curDrawAmount;
    }

    public void setCurDrawAmount(float curDrawAmount) {
        this.curDrawAmount = curDrawAmount;
    }

    public Integer getCurDrawStatus() {
        return curDrawStatus;
    }

    public void setCurDrawStatus(Integer curDrawStatus) {
        this.curDrawStatus = curDrawStatus;
    }

    public Integer getCurIsRedSender() {
        return curIsRedSender;
    }

    public void setCurIsRedSender(Integer curIsRedSender) {
        this.curIsRedSender = curIsRedSender;
    }

    public DrawRecordPageVoBean getDrawRecordPageVo() {
        return drawRecordPageVo;
    }

    public void setDrawRecordPageVo(DrawRecordPageVoBean drawRecordPageVo) {
        this.drawRecordPageVo = drawRecordPageVo;
    }

    public float getHasDrawTotalAmount() {
        return hasDrawTotalAmount;
    }

    public void setHasDrawTotalAmount(float hasDrawTotalAmount) {
        this.hasDrawTotalAmount = hasDrawTotalAmount;
    }

    public int getHasDrawTotalNumber() {
        return hasDrawTotalNumber;
    }

    public void setHasDrawTotalNumber(int hasDrawTotalNumber) {
        this.hasDrawTotalNumber = hasDrawTotalNumber;
    }

    public String getIsAllDraw() {
        return isAllDraw;
    }

    public void setIsAllDraw(String isAllDraw) {
        this.isAllDraw = isAllDraw;
    }

    public int getRedId() {
        return redId;
    }

    public void setRedId(int redId) {
        this.redId = redId;
    }

    public RedSendUserVoBean getRedSendUserVo() {
        return redSendUserVo;
    }

    public void setRedSendUserVo(RedSendUserVoBean redSendUserVo) {
        this.redSendUserVo = redSendUserVo;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public static class DrawRecordPageVoBean {
        /**
         * datas : [{"avatar":"string","drawAmount":0,"drawTime":"2020-07-17T08:02:10.208Z","isBestLuck":"string","nickname":"string","userId":0}]
         * page : 0
         * pageNum : 0
         * size : 0
         * total : 0
         */

        private int page;
        private int pageNum;
        private int size;
        private int total;
        private List<RedDrawBean> datas;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RedDrawBean> getDatas() {
            return datas;
        }

        public void setDatas(List<RedDrawBean> datas) {
            this.datas = datas;
        }

        public static class RedDrawBean {
            /**
             * avatar (string, optional): 领取人用户头像 ,
             * drawAmount (integer, optional): 红包领取金额 ,
             * drawTime (string, optional): 红包领取时间 ,
             * isBestLuck (string, optional): 是否手气最佳(0:否 1:是) ,
             * nickname (string, optional): 领取人用户昵称 ,
             * userId (integer, optional): 领取人用户id
             */

            private String avatar;
            private float drawAmount;
            private String drawTime;
            private String isBestLuck;
            private String nickname;
            private int userId;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public float getDrawAmount() {
                return drawAmount;
            }

            public void setDrawAmount(float drawAmount) {
                this.drawAmount = drawAmount;
            }

            public String getDrawTime() {
                return drawTime;
            }

            public void setDrawTime(String drawTime) {
                this.drawTime = drawTime;
            }

            public String getIsBestLuck() {
                return isBestLuck;
            }

            public void setIsBestLuck(String isBestLuck) {
                this.isBestLuck = isBestLuck;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }

    public static class RedSendUserVoBean {
        /**
         * avatar : string
         * nickname : string
         * userId : 0
         */

        private String avatar;
        private String nickname;
        private int userId;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
