package com.unitedratings.lhcrm.domains;

import com.unitedratings.lhcrm.entity.Portfolio;

import java.util.List;

/**
 * @author wangyongxin
 */
public class AssetPool {

    private AssetPoolInfo assetPoolInfo;

    private Portfolio portfolio;

    private List<LoanRecord> loanRecords;

    public AssetPoolInfo getAssetPoolInfo() {
        return assetPoolInfo;
    }

    public void setAssetPoolInfo(AssetPoolInfo assetPoolInfo) {
        this.assetPoolInfo = assetPoolInfo;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<LoanRecord> getLoanRecords() {
        return loanRecords;
    }

    public void setLoanRecords(List<LoanRecord> loanRecords) {
        this.loanRecords = loanRecords;
    }
}
