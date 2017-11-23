package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.excelprocess.AssetsExcelProcess;
import org.apache.commons.math3.distribution.AbstractMultivariateRealDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.ujmp.commonsmath.CommonsMathDenseDoubleMatrix2DFactory;
import org.ujmp.core.doublematrix.DenseDoubleMatrix2D;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;

/**
 * 自定义多元正态分布（用于修正非正定矩阵的分解，修正方法：特征值分解时，如果出现负数特征值，将其置0）
 * 参考自
 * @see org.apache.commons.math3.distribution.MultivariateNormalDistribution
 * @author wangyongxin
 * @createAt 2017-11-22 上午9:56
 **/
public class CustomMultivariateNormalDistribution extends AbstractMultivariateRealDistribution {

    /** Vector of means. */
    private final double[] means;
    /** Covariance matrix. */
    private final RealMatrix covarianceMatrix;
    /** The matrix inverse of the covariance matrix. */
    private final RealMatrix covarianceMatrixInverse;
    /** The determinant of the covariance matrix. */
    private final double covarianceMatrixDeterminant;
    /** Matrix used in computation of samples. */
    private final RealMatrix samplingMatrix;

    /**
     * Creates a multivariate normal distribution with the given mean vector and
     * covariance matrix.
     * <br/>
     * The number of dimensions is equal to the length of the mean vector
     * and to the number of rows and columns of the covariance matrix.
     * It is frequently written as "p" in formulae.
     *
     * @param means Vector of means.
     * @param covariances Covariance matrix.
     * @throws DimensionMismatchException if the arrays length are
     * inconsistent.
     * @throws SingularMatrixException if the eigenvalue decomposition cannot
     * be performed on the provided covariance matrix.
     * @throws NonPositiveDefiniteMatrixException if any of the eigenvalues is
     * negative.
     */
    public CustomMultivariateNormalDistribution(final double[] means,
                                          final double[][] covariances)
            throws SingularMatrixException,
            DimensionMismatchException,
            NonPositiveDefiniteMatrixException {
        this(new Well19937c(), means, covariances);
    }

    /**
     * Creates a multivariate normal distribution with the given mean vector and
     * covariance matrix.
     * <br/>
     * The number of dimensions is equal to the length of the mean vector
     * and to the number of rows and columns of the covariance matrix.
     * It is frequently written as "p" in formulae.
     *
     * @param rng Random Number Generator.
     * @param means Vector of means.
     * @param covariances Covariance matrix.
     * @throws DimensionMismatchException if the arrays length are
     * inconsistent.
     * @throws SingularMatrixException if the eigenvalue decomposition cannot
     * be performed on the provided covariance matrix.
     * @throws NonPositiveDefiniteMatrixException if any of the eigenvalues is
     * negative.
     */
    public CustomMultivariateNormalDistribution(RandomGenerator rng,
                                          final double[] means,
                                          final double[][] covariances)
            throws SingularMatrixException,
            DimensionMismatchException {
        super(rng, means.length);

        final int dim = means.length;

        if (covariances.length != dim) {
            throw new DimensionMismatchException(covariances.length, dim);
        }

        for (int i = 0; i < dim; i++) {
            if (dim != covariances[i].length) {
                throw new DimensionMismatchException(covariances[i].length, dim);
            }
        }

        this.means = MathArrays.copyOf(means);

        covarianceMatrix = new Array2DRowRealMatrix(covariances);

        // Covariance matrix eigen decomposition.
        final EigenDecomposition covMatDec = new EigenDecomposition(covarianceMatrix);

        // Compute and store the inverse.
        covarianceMatrixInverse = covMatDec.getSolver().getInverse();
        // Compute and store the determinant.
        covarianceMatrixDeterminant = covMatDec.getDeterminant();

        // Eigenvalues of the covariance matrix.
        final double[] covMatEigenvalues = covMatDec.getRealEigenvalues();

        double[] clonedCovMatEigenvalues = new double[covMatEigenvalues.length];
        for (int i = 0; i < covMatEigenvalues.length; i++) {
            if (covMatEigenvalues[i] < 0) {
                clonedCovMatEigenvalues[i] = 0;
                covMatDec.getD().setEntry(i,i,0);
            } else {
                clonedCovMatEigenvalues[i] = covMatEigenvalues[i];
            }
        }

        // Matrix where each column is an eigenvector of the covariance matrix.
        final Array2DRowRealMatrix covMatEigenvectors = new Array2DRowRealMatrix(dim, dim);
        for (int v = 0; v < dim; v++) {
            final double[] evec = covMatDec.getEigenvector(v).toArray();
            covMatEigenvectors.setColumn(v, evec);
        }

        final RealMatrix tmpMatrix = covMatEigenvectors.transpose();

        // Scale each eigenvector by the square root of its eigenvalue.
        for (int row = 0; row < dim; row++) {
            final double factor = FastMath.sqrt(clonedCovMatEigenvalues[row]);
            for (int col = 0; col < dim; col++) {
                tmpMatrix.multiplyEntry(row, col, factor);
            }
        }

        samplingMatrix = covMatEigenvectors.multiply(tmpMatrix);
        //outputErrorAnalysis(dim, covMatDec);
    }

    /**
     * 输出特征值修正过程误差分析
     * @param dim
     * @param covMatDec
     */
    private void outputErrorAnalysis(int dim, EigenDecomposition covMatDec) {
        RealMatrix needCheckCov = covMatDec.getV().multiply(covMatDec.getD().multiply(covMatDec.getVT()));
        DenseDoubleMatrix2D checkedResult = new DefaultDenseDoubleMatrix2D(dim,dim);
        for(int i=0;i<dim;i++){
            for(int j=0;j<dim;j++){
                double newVal = (double) FastMath.round(needCheckCov.getEntry(i, j)*1000)/1000;
                double originVal = covarianceMatrix.getEntry(i, j);
                if(FastMath.abs(newVal-originVal)>0){
                    checkedResult.setAsDouble(FastMath.abs(newVal-originVal)/originVal,i,j);
                }
            }
        }
        AssetsExcelProcess.outputMatrixToExcel(CommonsMathDenseDoubleMatrix2DFactory.INSTANCE.dense(covMatDec.getD()),CommonsMathDenseDoubleMatrix2DFactory.INSTANCE.dense(needCheckCov),CommonsMathDenseDoubleMatrix2DFactory.INSTANCE.dense(covarianceMatrix),checkedResult);
    }

    /**
     * Gets the mean vector.
     *
     * @return the mean vector.
     */
    public double[] getMeans() {
        return MathArrays.copyOf(means);
    }

    /**
     * Gets the covariance matrix.
     *
     * @return the covariance matrix.
     */
    public RealMatrix getCovariances() {
        return covarianceMatrix.copy();
    }

    /** {@inheritDoc} */
    @Override
    public double density(final double[] vals) throws DimensionMismatchException {
        final int dim = getDimension();
        if (vals.length != dim) {
            throw new DimensionMismatchException(vals.length, dim);
        }

        return FastMath.pow(2 * FastMath.PI, -0.5 * dim) *
                FastMath.pow(covarianceMatrixDeterminant, -0.5) *
                getExponentTerm(vals);
    }

    /**
     * Gets the square root of each element on the diagonal of the covariance
     * matrix.
     *
     * @return the standard deviations.
     */
    public double[] getStandardDeviations() {
        final int dim = getDimension();
        final double[] std = new double[dim];
        final double[][] s = covarianceMatrix.getData();
        for (int i = 0; i < dim; i++) {
            std[i] = FastMath.sqrt(s[i][i]);
        }
        return std;
    }

    /** {@inheritDoc} */
    @Override
    public double[] sample() {
        final int dim = getDimension();
        final double[] normalVals = new double[dim];

        for (int i = 0; i < dim; i++) {
            normalVals[i] = random.nextGaussian();
        }

        final double[] vals = samplingMatrix.operate(normalVals);

        for (int i = 0; i < dim; i++) {
            vals[i] += means[i];
        }

        return vals;
    }

    /**
     * Computes the term used in the exponent (see definition of the distribution).
     *
     * @param values Values at which to compute density.
     * @return the multiplication factor of density calculations.
     */
    private double getExponentTerm(final double[] values) {
        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - getMeans()[i];
        }
        final double[] preMultiplied = covarianceMatrixInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }
}
