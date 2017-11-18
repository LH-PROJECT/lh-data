package com.unitedratings.lhcrm.web.model;

/**
 * @author wangyongxin
 * @createAt 2017-11-07 下午2:11
 **/
public class PortfolioQuery {

    private Long id;
    private String portfolioName;
    private Integer userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
