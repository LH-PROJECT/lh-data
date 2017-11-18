package com.unitedratings.lhcrm.entity;

import com.unitedratings.lhcrm.domains.LoanRecord;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author wangyongxin
 */
@Entity
public class Portfolio {

    @Id
    @GeneratedValue
    private Long id;
    private Long projectId;
    @Column(length = 50)
    private String projectName;
    @Column(length = 50)
    private String portfolioName;
    private Long uploadRecordId;
    private Date beginCalculateDate;
    private Double reservesMoney;
    private Integer simulationNum;
    @Column(length = 20)
    private String currentState;
    private Double multiplier;
    private Integer sponsorId;
    @Column(length = 20)
    private String sponsorName;
    private Integer idealDefaultId;
    private Integer userId;
    private Date createTime;
    @Transient
    private List<LoanRecord> recordList;
    @Transient
    private Amortization amortization;

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

    public Long getUploadRecordId() {
        return uploadRecordId;
    }

    public void setUploadRecordId(Long uploadRecordId) {
        this.uploadRecordId = uploadRecordId;
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

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
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

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getIdealDefaultId() {
        return idealDefaultId;
    }

    public void setIdealDefaultId(Integer idealDefaultId) {
        this.idealDefaultId = idealDefaultId;
    }

    public List<LoanRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<LoanRecord> recordList) {
        this.recordList = recordList;
    }

    public Amortization getAmortization() {
        return amortization;
    }

    public void setAmortization(Amortization amortization) {
        this.amortization = amortization;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
