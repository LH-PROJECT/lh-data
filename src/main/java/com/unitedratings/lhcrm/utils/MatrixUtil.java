package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.excelprocess.AssetsExcelProcess;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

/**
 * @author wangyongxin
 */
public class MatrixUtil {

    private static List<Matrix> matrices;

    static {
        try {
            matrices = AssetsExcelProcess.processMassRandomSheet();
        } catch (IOException|SAXException|OpenXML4JException e) {
            e.printStackTrace();
        }
    }

    private MatrixUtil(){}

    /**
     * 获取相关随机数矩阵
     * @param cov 关联系数矩阵
     * @param loanNum 贷款笔数
     * @param quarterNum 季度数
     * @return 随机关联系数矩阵
     */
    public static Matrix getRandomCovMatrix(Matrix cov, Integer loanNum, Integer quarterNum,int num) {
        Matrix matrix = matrices.get(num);
        /*DenseMatrix matrix = Matrix.Factory.zeros(loanNum, quarterNum);
        Random random = new Random();
        //NormalDistributionRandomGenerator random = new NormalDistributionRandomGenerator();
        for(int i=0;i<loanNum;i++){
            for(int j = 0;j<quarterNum;j++){
                matrix.setAsDouble(random.nextGaussian(),i,j);
            }
        }*/
        return cov.mtimes(Calculation.Ret.NEW,true,matrix);
    }

    /**
     * 计算条件违约概率在正态分布中的对应逆矩阵
     * @param assetPoolInfo
     * @return
     */
    public static Matrix calculateConditionProbability(AssetPoolInfo assetPoolInfo) {
        //求逆
        NormalDistribution normal = new NormalDistribution();
        Matrix conditionMatrix = assetPoolInfo.getConditionMatrix();
        DefaultDenseDoubleMatrix2D matrix = new DefaultDenseDoubleMatrix2D((int)conditionMatrix.getRowCount(),(int)conditionMatrix.getColumnCount());
        for(long[] c:conditionMatrix.allCoordinates()){
            double value = conditionMatrix.getAsDouble(c);
            if(value>0){
                matrix.setAsDouble(normal.inverseCumulativeProbability(value),c);
                //matrix.setAsDouble(MathUtil.inverseCumulativeProbability(value),c);
            }
        }
        return matrix;
    }
}
