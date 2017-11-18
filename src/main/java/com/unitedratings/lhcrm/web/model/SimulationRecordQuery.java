package com.unitedratings.lhcrm.web.model;

/**
 * @author wangyongxin
 * @createAt 2017-11-09 下午4:15
 **/
public class SimulationRecordQuery {
    private Long id;
    private Long attachableId;
    private String attachableType;
    private String portfolioName;
    private Integer userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttachableId() {
        return attachableId;
    }

    public void setAttachableId(Long attachableId) {
        this.attachableId = attachableId;
    }

    public String getAttachableType() {
        return attachableType;
    }

    public void setAttachableType(String attachableType) {
        this.attachableType = attachableType;
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
