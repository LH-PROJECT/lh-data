package com.unitedratings.lhcrm.web.model;

/**
 * @author wangyongxin
 * @createAt 2017-11-07 下午1:41
 **/
public class PageModel<T> {

    private T query;
    private Integer pageNo;
    private Integer pageSize;

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
