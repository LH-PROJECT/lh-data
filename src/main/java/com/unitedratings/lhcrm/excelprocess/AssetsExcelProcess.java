package com.unitedratings.lhcrm.excelprocess;

import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.AssetPool;
import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.utils.DateUtil;
import com.unitedratings.lhcrm.utils.ExcelUtil;
import com.unitedratings.lhcrm.utils.MathUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.objectmatrix.impl.DefaultDenseObjectMatrix2D;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 资产池违约概率模型excel模板处理类
 */
public class AssetsExcelProcess {

    /**
     * 处理资产池excel模板
     * @param file excel文件
     * @param assetPool 资产池模拟实体
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static void processAssetsExcel(File file, AssetPool assetPool) throws InvalidFormatException, IOException {
        final OPCPackage pkg = OPCPackage.open(file);
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        for(int index=0;index<workbook.getNumberOfSheets();index++){
            switch (index){
                //规定第1、2、3个sheet分别为资产池信息sheet、分期摊还信息sheet、资产相关系数矩阵信息sheet
                case 0:
                    //处理资产池信息sheet
                    processAssetPoolSheet(workbook.getSheetAt(index),assetPool);
                    break;
                case 1:
                    //处理分期摊还信息sheet
                    processAmortisationSheet(workbook.getSheetAt(index),assetPool.getAssetPoolInfo());
                    break;
                case 2:
                    //处理资产相关系数矩阵信息sheet
                    processCorrelationSheet(workbook.getSheetAt(index),assetPool.getAssetPoolInfo());
                    break;
                case 3:
                    //处理理想违约概率矩阵信息sheet
                    processPerfectDefaultRateSheet(workbook.getSheetAt(index),assetPool.getAssetPoolInfo());
                    break;
                case 4:
                    //处理理想违约概率矩阵信息sheet(临时使用，季度数据)
                    if(assetPool.getAssetPoolInfo().getSummaryType() == SummaryType.QUARTER.getValue()){
                        processConditionDefaultRateSheet(workbook.getSheetAt(index),assetPool.getAssetPoolInfo());
                    }
                    break;
                default:
                    break;
            }
        }
        pkg.close();
    }

    /**
     * 处理分期条件违约率表（临时使用，后续需替换为计算结果）
     * @param sheet
     * @param assetPoolInfo
     */
    private static void processConditionDefaultRateSheet(XSSFSheet sheet, AssetPoolInfo assetPoolInfo) {
        //获取按分期条件违约率表信息sheet有效行列数,模板中第0索引行、0索引列为表头信息，有效数据从第1索引行、1索引列开始
        final int dataBeginRow = 1;
        final int dataBeginCol = 1;
        //确定有效行列
        int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,false);
        int validRowSize = validRowAndColSize[0];
        int validColSize = validRowAndColSize[1];
        //处理数据
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
            Row tempRow = sheet.getRow(r);
            if (tempRow != null) {
                //封装分期摊还信息矩阵
                cellDataProcess(dataBeginRow, dataBeginCol, validColSize, df, dateFormat, matrix, r, tempRow);
            }
        }
        assetPoolInfo.setConditionMatrix(matrix);
    }

    /**
     * 处理理想违约率信息sheet
     * @param sheet
     * @param assetPoolInfo
     */
    private static void processPerfectDefaultRateSheet(XSSFSheet sheet, AssetPoolInfo assetPoolInfo) {
        //获取理想违约率矩阵信息sheet有效行列数,模板中第0-6索引行，0、1索引列为表头信息，有效数据从第7索引行第2索引列开始
        final int dataBeginRow = 7;
        final int dataBeginCol = 2;
        //确定有效行列
        int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,true);
        int validRowSize = validRowAndColSize[0];
        int validColSize = validRowAndColSize[1];
        //处理数据
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
            Row tempRow = sheet.getRow(r);
            if (tempRow != null) {
                //封装资产相关系数矩阵信息矩阵
                cellDataProcess(dataBeginRow, dataBeginCol,validColSize, df, dateFormat, matrix, r, tempRow);
            }
        }
        assetPoolInfo.setPerfectDefaultRate(matrix);
    }

    /**
     * 处理资产池信息sheet
     * @param sheet
     * @param assetPoolInfo
     */
    private static void processCorrelationSheet(XSSFSheet sheet, AssetPoolInfo assetPoolInfo) {
        //获取资产相关系数矩阵信息sheet有效行列数,模板中第0-4索引行，0-2索引列为表头信息，有效数据从第5索引行第3索引列开始
        final int dataBeginRow = 5;
        final int dataBeginCol = 3;
        //确定有效行列
        int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,true);
        int validRowSize = validRowAndColSize[0];
        int validColSize = validRowAndColSize[1];
        //处理数据
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
            Row tempRow = sheet.getRow(r);
            if (tempRow != null) {
                //封装资产相关系数矩阵信息矩阵
                cellDataProcess(dataBeginRow, dataBeginCol,validColSize, df, dateFormat, matrix, r, tempRow);
            }
        }
        assetPoolInfo.setCorrelation(matrix);
    }

    /**
     * 处理分期摊还信息sheet
     * @param sheet
     * @param assetPoolInfo
     */
    private static void processAmortisationSheet(XSSFSheet sheet, AssetPoolInfo assetPoolInfo) {
        //获取分期摊还信息sheet有效行列数,模板中第0、1索引行为表头信息，有效数据从第2索引行开始
        final int dataBeginRow = 2;
        final int dataBeginCol = 0;
        //确定有效行列
        int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,true);
        int validRowSize = validRowAndColSize[0];
        int validColSize = validRowAndColSize[1];
        //处理数据
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
            Row tempRow = sheet.getRow(r);
            if (tempRow != null) {
                //封装分期摊还信息矩阵
                cellDataProcess(dataBeginRow, dataBeginCol, validColSize, df, dateFormat, matrix, r, tempRow);
            }
        }
        assetPoolInfo.setAmortisation(matrix);
    }

    /**
     * 处理资产相关系数矩阵信息sheet
     * @param sheet
     * @param assetPool
     */
    private static void processAssetPoolSheet(XSSFSheet sheet, AssetPool assetPool) {
        //获取资产池sheet有效行列数,模板中第0-2索引行为表头信息，有效数据从第3索引行开始
        final int dataBeginRow = 3;
        final int dataBeginCol = 0;
        //确定有效行列
        int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,false);
        int validRowSize = validRowAndColSize[0];
        int validColSize = validRowAndColSize[1];
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        List<LoanRecord> loanRecords = new ArrayList<>();
        for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                //封装贷款信息实体
                loanRecords.add(assembleLoanRecord(row));
                //封装资产池矩阵
                cellDataProcess(dataBeginRow,dataBeginCol, validColSize, df, dateFormat, matrix, r, row);
            }
        }
        assetPool.setLoanRecords(loanRecords);
        AssetPoolInfo assetPoolInfo = assetPool.getAssetPoolInfo();
        assetPoolInfo.setLoanNum(loanRecords.size());
        assembleAssetPoolInfo(matrix,assetPoolInfo);
    }

    /**
     * excel单元格数据处理
     * @param dataBeginRow 有效数据开始行
     * @param colSize 有效数据列数
     * @param df 数据精度格式化器
     * @param dateFormat 日期格式化器
     * @param matrix 封装单元格处理后的矩阵
     * @param r 当前处理行索引
     * @param row 当前处理行
     */
    private static void cellDataProcess(int dataBeginRow, int dataBeginCol, int colSize, DecimalFormat df, DateFormat dateFormat, Matrix matrix, int r, Row row) {
        for (int c = dataBeginCol; c < colSize+dataBeginCol; c++) {
            Cell cell = row.getCell(c);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        matrix.setAsBoolean(cell.getBooleanCellValue(), r-dataBeginRow, c-dataBeginCol);
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        matrix.setAsString(cell.getCellFormula(), r-dataBeginRow, c-dataBeginCol);
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if(HSSFDateUtil.isCellDateFormatted(cell)){
                            matrix.setAsString(dateFormat.format(cell.getDateCellValue()),r-dataBeginRow, c-dataBeginCol);
                        }else if(BuiltinFormats.getBuiltinFormat(0).equals(cell.getCellStyle().getDataFormatString())){
                            matrix.setAsString(df.format(cell.getNumericCellValue()),r-dataBeginRow, c-dataBeginCol);
                        }else {
                            matrix.setAsDouble(cell.getNumericCellValue(), r-dataBeginRow, c-dataBeginCol);
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        matrix.setAsString(cell.getStringCellValue(), r-dataBeginRow, c-dataBeginCol);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * 根据资产池矩阵信息封装资产池分类综合信息
     * @param matrix 资产池信息矩阵
     * @param assetPoolInfo
     */
    private static void assembleAssetPoolInfo(Matrix matrix, AssetPoolInfo assetPoolInfo) {
        //获取贷款记录编码,矩阵第0列
        Matrix loanSerialMatrix = matrix.selectColumns(Calculation.Ret.LINK,0);
        int size = new Long(matrix.getRowCount()).intValue();
        long[] loanSerial = new long[size];
        for(int i = 0;i<loanSerialMatrix.getRowCount();i++){
            loanSerial[i] = loanSerialMatrix.getAsLong(i,0);
        }
        assetPoolInfo.setLoanSerial(loanSerial);
        //获取借款人id编码,矩阵第1列
        Matrix idMatrix = matrix.selectColumns(Calculation.Ret.LINK,1);
        long[] ids = new long[size];
        for(int i = 0;i<idMatrix.getRowCount();i++){
            ids[i] = idMatrix.getAsLong(i,0);
        }
        assetPoolInfo.setIds(ids);
        //获取贷款余额,矩阵第5列
        Matrix principalMatrix = matrix.selectColumns(Calculation.Ret.LINK,5);
        double[] principal = new double[size];
        for(int i = 0;i<principalMatrix.getRowCount();i++){
            principal[i] = principalMatrix.getAsDouble(i,0);
        }
        assetPoolInfo.setPrincipal(principal);
        //获取贷款保证金,矩阵第42列
        Matrix secureAmountMatrix = matrix.selectColumns(Calculation.Ret.LINK,42);
        double[] secureAmount = new double[size];
        for(int i = 0;i<secureAmountMatrix.getRowCount();i++){
            secureAmount[i] = secureAmountMatrix.getAsDouble(i,0);
        }
        assetPoolInfo.setSecureAmount(secureAmount);
        //获取贷款行业编码,矩阵第2列
        Matrix industryCodeMatrix = matrix.selectColumns(Calculation.Ret.LINK,2);
        long[] industryCode = new long[size];
        for(int i = 0;i<industryCodeMatrix.getRowCount();i++){
            industryCode[i] = industryCodeMatrix.getAsLong(i,0);
        }
        assetPoolInfo.setAssetType(industryCode);
        //计算贷款到期年限,矩阵第4列
        Matrix maturityMatrix = matrix.selectColumns(Calculation.Ret.LINK,4);
        double[] maturity = new double[size];
        for(int i = 0;i<maturityMatrix.getRowCount();i++){
            maturity[i] = DateUtil.calculatePeriods(assetPoolInfo.getSummaryType(),assetPoolInfo.getBeginCalculateDate(),DateUtil.parseDate(maturityMatrix.getAsString(i,0),"yyyy-MM-dd"));
        }
        assetPoolInfo.setMaturity(maturity);
        //获取最终回收率,矩阵第33列(需要计算，临时使用模板提供的)
        Matrix finalRecoveryRateMatrix = matrix.selectColumns(Calculation.Ret.LINK,33);
        double[] finalRecoveryRate = new double[size];
        for(int i = 0;i<finalRecoveryRateMatrix.getRowCount();i++){
            finalRecoveryRate[i] = finalRecoveryRateMatrix.getAsDouble(i,0);
        }
        assetPoolInfo.setFinalRecoveryRate(finalRecoveryRate);
        //若是按年，获取条件逐年条件违约率,矩阵第35-38列(需要计算，临时使用模板提供的)
        if(assetPoolInfo.getSummaryType()==SummaryType.YEAR.getValue()){
            Matrix conditionMatrix = matrix.selectColumns(Calculation.Ret.LINK,35,36,37,38);
            assetPoolInfo.setConditionMatrix(conditionMatrix);
        }
        //计算加权平均借款期限
        double[] yearMaturity = new double[size];
        for(int i = 0;i<maturityMatrix.getRowCount();i++){
            yearMaturity[i] = DateUtil.calculatePeriods(SummaryType.YEAR.getValue(),assetPoolInfo.getBeginCalculateDate(),DateUtil.parseDate(maturityMatrix.getAsString(i,0),"yyyy-MM-dd"));
        }
        assetPoolInfo.setWeightedAverageMaturity(MathUtil.calculateWeightedAverageMaturity(assetPoolInfo.getPrincipal(),yearMaturity));
    }

    /**
     * 封装贷款记录（待完善）
     * @param row
     * @return
     */
    private static LoanRecord assembleLoanRecord(Row row) {
        LoanRecord loanRecord = new LoanRecord();
        DecimalFormat df = new DecimalFormat("0");
        String formatLoanSerial = df.format(row.getCell(0).getNumericCellValue());
        loanRecord.setLoanSerial(Long.parseLong(formatLoanSerial));
        return loanRecord;
    }
}
