package com.unitedratings.lhcrm.constants;

/**
 * 违约率计算汇总维度：
 * 1-按年汇总
 * 2-按季度汇总
 * 3-按月汇总
 * 4-按周汇总
 * 5-按天汇总
 * @author wangyongxin
 */
public enum SummaryType {

    YEAR("年",1),QUARTER("季度",2),MONTH("月",3),WEEK("周",4),DAY("天",5);

    private String name;
    private Integer value;

    private SummaryType(String name,Integer value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
