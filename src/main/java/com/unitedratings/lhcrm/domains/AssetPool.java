package com.unitedratings.lhcrm.domains;

import java.util.List;

public class AssetPool {

    private AssetPoolInfo assetPoolInfo;

    private List<LoanRecord> loanRecords;

    public AssetPoolInfo getAssetPoolInfo() {
        return assetPoolInfo;
    }

    public void setAssetPoolInfo(AssetPoolInfo assetPoolInfo) {
        this.assetPoolInfo = assetPoolInfo;
    }

    public List<LoanRecord> getLoanRecords() {
        return loanRecords;
    }

    public void setLoanRecords(List<LoanRecord> loanRecords) {
        this.loanRecords = loanRecords;
    }
}
