package com.keepfun.aiservice.entity;

/**
 * @author yang
 * @description
 * @date 2020/9/3 10:10 AM
 */
public class QuestionType {
    /**
     * 分类code
     */
    private String code;
    /**
     * 分类id
     */
    private long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 排序
     */
    private int sort;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
