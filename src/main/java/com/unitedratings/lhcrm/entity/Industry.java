package com.unitedratings.lhcrm.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes = {@Index(columnList = "industryCode"),@Index(columnList = "parentIndustryCode")})
public class Industry {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 20)
    private String industryCode;
    @Column(length = 50)
    private String industryName;
    @Column(length = 20)
    private String parentIndustryCode;
    private String industryDesc;
    private Date createTime;
    private Double version;
    private boolean isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getParentIndustryCode() {
        return parentIndustryCode;
    }

    public void setParentIndustryCode(String parentIndustryCode) {
        this.parentIndustryCode = parentIndustryCode;
    }

    public String getIndustryDesc() {
        return industryDesc;
    }

    public void setIndustryDesc(String industryDesc) {
        this.industryDesc = industryDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
