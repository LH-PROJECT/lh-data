package com.unitedratings.lhcrm.business;

import com.alibaba.fastjson.JSON;
import com.unitedratings.lhcrm.algorithm.MonteCarlo;
import com.unitedratings.lhcrm.domains.AssetPool;
import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.domains.MonteResult;
import com.unitedratings.lhcrm.excelprocess.AssetsExcelProcess;
import com.unitedratings.lhcrm.utils.MathUtil;
import com.unitedratings.lhcrm.utils.MatrixUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.objectmatrix.impl.DefaultDenseObjectMatrix2D;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class CreditPortfolioRiskAnalysisTest {

    @Test
    public void monteCarloSimulation() throws Exception {
        long t1 = System.currentTimeMillis();
        File file = new File("/Users/wangyongxin/Desktop/模板.xlsx");
        AssetPool assetPool = new AssetPool();
        AssetPoolInfo assetPoolInfo = new AssetPoolInfo();
        assetPoolInfo.setReservesMoney(0);
        assetPoolInfo.setSummaryType(1);
        assetPoolInfo.setBeginCalculateDate(Date.from(LocalDate.of(2017,5,1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        assetPool.setAssetPoolInfo(assetPoolInfo);
        AssetsExcelProcess.processAssetsExcel(file,assetPool);
        MonteResult result = MonteCarlo.AmortizedM(assetPoolInfo, assetPoolInfo.getCorrelation().chol(), assetPoolInfo.getConditionMatrix(), assetPoolInfo.getFinalRecoveryRate(), 900000, new AtomicInteger(0), 10000);
        long t2= System.currentTimeMillis();
        System.out.println("消耗时间为"+(t2-t1)+"ms");
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void assetMatrixPrint() throws Exception {
        File file = new File("/Users/wangyongxin/Desktop/模板.xlsx");
        final OPCPackage pkg = OPCPackage.open(file);
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        XSSFSheet sheet = workbook.getSheetAt(0);
        final int dataBeginRow = 3;
        final int dataBeginCol = 0;
        int validRowSize = 0;
        for (int r = dataBeginRow; r < sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                Cell cell = row.getCell(0);
                if(cell!=null){
                    if(Cell.CELL_TYPE_BLANK==cell.getCellType()){
                        validRowSize = r - dataBeginRow;
                        break;
                    }
                }
            }
        }
        //确定有效列
        int validColSize = 0;
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            if (row.getLastCellNum() > validColSize) {
                validColSize = row.getLastCellNum();
            }
        }
        DecimalFormat df = new DecimalFormat("0");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Matrix matrix = new DefaultDenseObjectMatrix2D(validRowSize, validColSize);
        matrix.setLabel(sheet.getSheetName());
        for (int r = dataBeginRow; r < sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                //封装贷款信息实体
                //封装资产池矩阵
                for (int c = dataBeginCol; c < validColSize; c++) {
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
                                    System.out.println(cell.getCellStyle().getDataFormatString());
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
        }
        for(int i = 0;i<matrix.getColumnCount();i++){
            System.out.println("==========第"+i+"列===========");
            System.out.println(matrix.selectColumns(Calculation.Ret.LINK,i));
        }
    }

    @Test
    public void tesRandomDefaultRate() throws Exception {
        File file = new File("/Users/wangyongxin/Desktop/模板.xlsx");
        AssetPool assetPool = new AssetPool();
        AssetPoolInfo assetPoolInfo = new AssetPoolInfo();
        assetPoolInfo.setReservesMoney(0);
        assetPoolInfo.setSummaryType(2);
        assetPoolInfo.setBeginCalculateDate(Date.from(LocalDate.of(2017,5,1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        assetPool.setAssetPoolInfo(assetPoolInfo);
        AssetsExcelProcess.processAssetsExcel(file,assetPool);
        Matrix randomCovMatrix = MatrixUtil.getRandomCovMatrix(assetPoolInfo.getCorrelation().chol(), assetPoolInfo.getLoanNum(), MathUtil.getMaxQuarter(assetPoolInfo.getMaturity()));
        System.out.println(assetPoolInfo.getCorrelation());
        System.out.println(assetPoolInfo.getCorrelation().chol());
        randomCovMatrix.setLabel("随机相关系数矩阵");
        Matrix matrix = MatrixUtil.calculateConditionProbability(assetPoolInfo);
        matrix.setLabel("按季度正态分布逆矩阵");
        Matrix conditionMatrix = assetPoolInfo.getConditionMatrix();
        conditionMatrix.setLabel("按季度条件违约率矩阵");
        System.out.println(randomCovMatrix);
        System.out.println(matrix);
        System.out.println(conditionMatrix);
        Matrix perfectDefaultRate = assetPoolInfo.getPerfectDefaultRate();
        perfectDefaultRate.setLabel("理想违约率表");
        System.out.println(perfectDefaultRate);
    }

}