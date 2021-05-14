package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/11/30 2:53 PM
 */
public class CodeImgVo {
    /**
     * 验证码图片(base64) ,
     */
    private String codeImg;
    /**
     * 图形验证码key，需要传到后端
     */
    private String imgKey;

    public String getCodeImg() {
        return codeImg;
    }

    public void setCodeImg(String codeImg) {
        this.codeImg = codeImg;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    @Override
    public String toString() {
        return "CodeImgVo{" +
                "codeImg='" + codeImg + '\'' +
                ", imgKey='" + imgKey + '\'' +
                '}';
    }
}
