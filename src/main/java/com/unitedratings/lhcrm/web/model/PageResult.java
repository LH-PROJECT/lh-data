package com.unitedratings.lhcrm.web.model;

import com.unitedratings.lhcrm.constants.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangyongxin
 */
public class PageResult<T> implements Serializable {

    // 记录总数
    private int totalRecords;
    // 每页记录数
    private int pageSize = Constant.DEFAULT_PAGE_SIZE;
    // 第几页
    private int pageNo = Constant.DEFAULT_PAGE_NO;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
