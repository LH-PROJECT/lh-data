package com.unitedratings.lhcrm.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Amortization {

    @Id
    private Long portfolioId;
    @Column(length = 1000)
    private String amortizationDate;
    @Transient
    private List<AmortizationInfo> amortizationInfoList;

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public List<AmortizationInfo> getAmortizationInfoList() {
        return amortizationInfoList;
    }

    public void setAmortizationInfoList(List<AmortizationInfo> amortizationInfoList) {
        this.amortizationInfoList = amortizationInfoList;
    }

    public String getAmortizationDate() {
        return amortizationDate;
    }

    public void setAmortizationDate(String amortizationDate) {
        this.amortizationDate = amortizationDate;
    }
}
