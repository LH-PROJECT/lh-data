package com.unitedratings.lhcrm.domains;

import java.util.List;

/**
 * 分布父类，定义子类所需公共属性及方法
 * @author wangyongxin
 * @createAt 2017-10-19 下午5:45
 **/
public abstract class Distribution {

    protected final static String INDUSTRY = "行业名称";
    protected final static String AREA = "地区";
    protected final static String CREDIT_LEVEL = "信用等级";
    protected final static String RESIDUAL_MATURITY = "剩余期限";
    protected final static String GUARANTEE_MODE = "担保方式";
    protected final static String DEBTOR_SERIAL = "债务人编号";
    protected final static String DEBT_NUM = "借款人户数";
    protected final static String LOAN_NUM = "贷款笔数";
    protected final static String LOAN_BALANCE = "贷款余额（万元）";
    protected final static String PROPORTION = "金额占比";
    protected final static String BORROWER = "借款人";
    protected final static String BELONG_INDUSTRY = "所属行业";
    protected final static String BELONG_AREA = "所属地区";
    protected final static String LOAN_AMOUNT = "贷款额（万元）";
    protected final static String GUARANTEE_NUM = "保证人户数";
    protected final static String GUARANTEE_LOAN_NUM = "担保债务笔数";
    protected final static String OUTSTANDING_LOAN_AMOUNT = "担保贷款未偿本金金额（万元）";

    private List<? extends Statistical> details;

    /**
     * 由子类根据分类提供的表头信息定制自己所需的表头
     * @return
     */
    protected abstract String[] getHeader();

    public List<? extends Statistical> getDetails() {
        return details;
    }

    public void setDetails(List<? extends Statistical> details) {
        this.details = details;
    }

    public Statistical createStatical(){
        return createStatisticalInternal();
    }

    /**
     * 创建分布统计实体，由子类继承Statistical父类，决定要新建的子类
     * @return
     */
    protected abstract Statistical createStatisticalInternal();

    /**
     * 子类分布的统一父类
     */
    public class Statistical{

    }
}
