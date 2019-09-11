package com.tony.billing.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
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

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = StringUtils.equalsIgnoreCase("desc", sort) ? "desc" : "asc";
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        pagination();
    }

    public void pagination() {
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

    public Map<String, Object> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, Object> extension) {
        this.extension = extension;
    }


}
