package com.keepfun.login.entity;

import com.keepfun.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

import kotlin.jvm.Transient;

/**
 * @author yang
 * @description
 * @date 2020/12/25 10:13 AM
 */
public class GlCountryEntity implements MultiItemEntity, Serializable {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;

    private String cnName;
    private String code;
    private String continent;
    private String enName;
    private String id;
    /**
     * 拼音 正常是拼音 热门是"热门城市",定位是"定位城市"
     */
    private String pinyin;
    /**
     * 类型 标题还是城市
     */
    private int itemType = 1;
    /**
     * 列表中的位置
     */
    @Transient
    private int scrollPosition;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
