package com.unitedratings.lhcrm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class UploadRecord {

    @Id
    @GeneratedValue
    private Long id;
    private String fileName;
    private Integer num;
    private Date beginCalculateDate;
    private Double reservesMoney;
    private Date createTime;
    private Integer summaryType;
    private boolean finish;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public Integer getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(Integer summaryType) {
        this.summaryType = summaryType;
    }
}
