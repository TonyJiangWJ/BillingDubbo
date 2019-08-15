package com.tony.billing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
@Data
@ToString
@EqualsAndHashCode
public class PagerGrid<T> implements Serializable {

    private List<T> result;
    /**
     * 查询条件
     */
    private T t;
    /**
     *  记录总数
     */
    private int count;

    private int index;

    private int offset;
    /**
     * 排序的字段
     */
    private String orderBy;
    /**
     * 排序顺序
     */
    private String sort;
    /**
     * easyui分页
     */
    private int page;

    private int totalPage;

    private Map<String, Object> extension;

    public PagerGrid() {
        this.count = 0;
        this.index = 0;
        this.offset = 50;
        this.orderBy = "ID";
        this.sort = "desc";
    }

    public PagerGrid(T t) {
        this.t = t;
        this.count = 0;
        this.index = 0;
        this.offset = 10;
        this.orderBy = "ID";
        this.sort = "desc";
    }

    public PagerGrid(T t, int index, int offset) {
        this.t = t;
        this.count = 0;
        this.index = index;
        this.offset = offset;
        this.orderBy = "ID";
        this.sort = "desc";
    }

    public PagerGrid(T t, int index, int offset, String sort) {
        this.t = t;
        this.count = 0;
        this.index = index;
        this.offset = offset;
        this.orderBy = "ID";
        this.sort = sort;
    }

    public PagerGrid(T t, int index, int offset, String sort, String orderBy) {
        this.t = t;
        this.count = 0;
        this.index = index;
        this.offset = offset;
        this.orderBy = orderBy;
        this.sort = sort;
    }


    public void setSort(String sort) {
        this.sort = StringUtils.equalsIgnoreCase("desc", sort) ? "desc" : "asc";
    }

    public void pagenation() {
        if (page != 0) {
            this.index = (page - 1) * offset;
        }
    }

    public Integer getTotalPage() {
        if (count == 0) {
            return 0;
        }
        return count % offset == 0 ? (count / offset) : (count / offset + 1);
    }



}
