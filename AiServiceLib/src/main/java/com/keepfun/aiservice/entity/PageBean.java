package com.keepfun.aiservice.entity;

import com.keepfun.aiservice.gson.EGson;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @description
 * @date 2020/8/28 3:23 PM
 */
public class PageBean<T> {
    /**
     * 数据
     */
    private List<T> datas;
    /**
     * 当前页数
     */
    private int page;
    /**
     * 总页数
     */
    private int pageNum;
    /**
     * 每页条数
     */
    private int size;
    /**
     * 总数
     */
    private int total;

    public static PageBean fromJson(String json, Class clazz) {
        EGson gson = new EGson();
        Type objectType = type(PageBean.class, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            @NotNull
            public Type getRawType() {
                return raw;
            }

            @Override
            @NotNull
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    public List<T> getDatas() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

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

    @Override
    public String toString() {
        return "PageBean{" +
                "datas=" + datas +
                ", page=" + page +
                ", pageNum=" + pageNum +
                ", size=" + size +
                ", total=" + total +
                '}';
    }
}
