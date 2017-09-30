package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

import java.util.Random;

public class MatrixUtil {

    private MatrixUtil(){}

    /**
     * 获取相关随机数矩阵
     * @param cov 关联系数矩阵
     * @param loanNum 贷款笔数
     * @param quarterNum 季度数
     * @return 随机关联系数矩阵
     */
    public static Matrix getRandomCovMatrix(Matrix cov, Integer loanNum, Integer quarterNum) {
        DenseMatrix matrix = Matrix.Factory.zeros(loanNum, quarterNum);
        Random random = new Random();
        for(int i=0;i<loanNum;i++){
            for(int j = 0;j<quarterNum;j++){
                matrix.setAsDouble(random.nextGaussian(),i,j);
            }
        }
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
            }
        }
        return matrix;
    }
}
