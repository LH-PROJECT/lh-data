package com.unitedratings.lhcrm.entity;

import javax.persistence.*;

/**
 * @author wangyongxin
 */
@Entity
@Table(indexes = {@Index(columnList = "portfolioId")})
public class AmortizationInfo {

    @Id
    @GeneratedValue
    private Long id;
    private Long portfolioId;
    private Long LoanSerial;
    @Column(length = 1000)
    private String amortization;

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

    public Long getLoanSerial() {
        return LoanSerial;
    }

    public void setLoanSerial(Long loanSerial) {
        LoanSerial = loanSerial;
    }

    public String getAmortization() {
        return amortization;
    }

    public void setAmortization(String amortization) {
        this.amortization = amortization;
    }
}
