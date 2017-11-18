package com.unitedratings.lhcrm.entity;

import javax.persistence.*;

/**
 * @author wangyongxin
 */
@Entity
@Table(indexes = {@Index(columnList = "portfolioId")})
public class GuarantorInfo {

    @Id
    @GeneratedValue
    private Long id;
    private Long portfolioId;
    @Column(length = 20)
    private String guaranteeMode;
    @Column(length = 20)
    private String guaranteeModeCode;
    private Integer guaranteeModeId;
    @Column(length = 100)
    private String guaranteeName;
    @Column(length = 30)
    private String liabilityForm;
    @Column(length = 20)
    private Integer liabilityFormCode;
    private Double guaranteeRatio;
    @Column(length = 10)
    private String guaranteeCreditLevel;
    private Integer guaranteeCreditLevelCode;
    private Long guaranteeIndustryCode;
    @Column(length = 30)
    private String guaranteeAgentIndustry;
    @Column(length = 20)
    private String guaranteeBelongArea;
    private Integer guaranteeNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getGuaranteeMode() {
        return guaranteeMode;
    }

    public void setGuaranteeMode(String guaranteeMode) {
        this.guaranteeMode = guaranteeMode;
    }

    public String getGuaranteeModeCode() {
        return guaranteeModeCode;
    }

    public void setGuaranteeModeCode(String guaranteeModeCode) {
        this.guaranteeModeCode = guaranteeModeCode;
    }

    public Integer getGuaranteeModeId() {
        return guaranteeModeId;
    }

    public void setGuaranteeModeId(Integer guaranteeModeId) {
        this.guaranteeModeId = guaranteeModeId;
    }

    public String getGuaranteeAgentIndustry() {
        return guaranteeAgentIndustry;
    }

    public void setGuaranteeAgentIndustry(String guaranteeAgentIndustry) {
        this.guaranteeAgentIndustry = guaranteeAgentIndustry;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public String getLiabilityForm() {
        return liabilityForm;
    }

    public void setLiabilityForm(String liabilityForm) {
        this.liabilityForm = liabilityForm;
    }

    public Integer getLiabilityFormCode() {
        return liabilityFormCode;
    }

    public void setLiabilityFormCode(Integer liabilityFormCode) {
        this.liabilityFormCode = liabilityFormCode;
    }

    public Double getGuaranteeRatio() {
        return guaranteeRatio;
    }

    public void setGuaranteeRatio(Double guaranteeRatio) {
        this.guaranteeRatio = guaranteeRatio;
    }

    public String getGuaranteeCreditLevel() {
        return guaranteeCreditLevel;
    }

    public void setGuaranteeCreditLevel(String guaranteeCreditLevel) {
        this.guaranteeCreditLevel = guaranteeCreditLevel;
    }

    public Integer getGuaranteeCreditLevelCode() {
        return guaranteeCreditLevelCode;
    }

    public void setGuaranteeCreditLevelCode(Integer guaranteeCreditLevelCode) {
        this.guaranteeCreditLevelCode = guaranteeCreditLevelCode;
    }

    public Long getGuaranteeIndustryCode() {
        return guaranteeIndustryCode;
    }

    public void setGuaranteeIndustryCode(Long guaranteeIndustryCode) {
        this.guaranteeIndustryCode = guaranteeIndustryCode;
    }

    public String getGuaranteeBelongArea() {
        return guaranteeBelongArea;
    }

    public void setGuaranteeBelongArea(String guaranteeBelongArea) {
        this.guaranteeBelongArea = guaranteeBelongArea;
    }

    public Integer getGuaranteeNum() {
        return guaranteeNum;
    }

    public void setGuaranteeNum(Integer guaranteeNum) {
        this.guaranteeNum = guaranteeNum;
    }
}
