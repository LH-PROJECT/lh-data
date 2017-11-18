package com.unitedratings.lhcrm.web.model;

import com.unitedratings.lhcrm.entity.User;

import java.util.Date;

/**
 * @author wangyongxin
 * @createAt 2017-11-08 下午2:22
 **/
public class PortfolioVo {

    private Long id;
    private String portfolioName;
    private Date beginCalculateDate;
    private Double reservesMoney;
    private Integer simulationNum;
    private Double multiplier;
    private Integer sponsorId;
    private String sponsorName;
    private Integer idealDefaultId;
    private Long uploadRecordId;
    private Date createTime;
    private UserModel user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUploadRecordId() {
        return uploadRecordId;
    }

    public void setUploadRecordId(Long uploadRecordId) {
        this.uploadRecordId = uploadRecordId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public Date getBeginCalculateDate() {
        return beginCalculateDate;
    }

    public void setBeginCalculateDate(Date beginCalculateDate) {
        this.beginCalculateDate = beginCalculateDate;
    }

    public Double getReservesMoney() {
        return reservesMoney;
    }

    public void setReservesMoney(Double reservesMoney) {
        this.reservesMoney = reservesMoney;
    }

    public Integer getSimulationNum() {
        return simulationNum;
    }

    public void setSimulationNum(Integer simulationNum) {
        this.simulationNum = simulationNum;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public Integer getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Integer sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public Integer getIdealDefaultId() {
        return idealDefaultId;
    }

    public void setIdealDefaultId(Integer idealDefaultId) {
        this.idealDefaultId = idealDefaultId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
