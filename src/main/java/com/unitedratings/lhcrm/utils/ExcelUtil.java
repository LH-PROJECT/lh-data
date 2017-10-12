package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.config.FileConfig;
import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelUtil {

    /**
     * 获取有效数据行列数目(此方法适用于紧凑型excel，即不允许excel数据区中间出现空行或空单元格，否则获取到的有效行列是错误的)
     * @param sheet 待处理sheet
     * @param dataBeginRow 有效数据在模板中的开始行
     * @param dataBeginCol 有效数据在模板中的开始列
     * @param isRegular 是否是规整sheet（规整sheet行和列都是整整齐齐的，非规整sheet每行单元格数目可以不同）
     * @return 行列数组，其中第一个元素代表有效行数，第二个元素代表有效列数
     */
    public static int[] getRegularValidRowAndColSize(Sheet sheet,int dataBeginRow,int dataBeginCol,boolean isRegular){
        int[] rowAndCol = new int[2];
        rowAndCol[0] = sheet.getLastRowNum() - dataBeginRow + 1;
        //1.确定有效行
        for (int r = dataBeginRow; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                Cell cell = row.getCell(dataBeginCol);
                if(cell!=null){
                    if(Cell.CELL_TYPE_BLANK==cell.getCellType()){
                        rowAndCol[0] = r - dataBeginRow;
                        break;
                    }
                }
            }
        }
        //1.确定有效列
        if(isRegular){
            Row row = sheet.getRow(dataBeginRow);
            rowAndCol[1] = row.getLastCellNum() - dataBeginCol;
            if (row != null) {
                for(int c = dataBeginCol;c < row.getLastCellNum();c++){
                    Cell cell = row.getCell(c);
                    if(cell!=null){
                        if(Cell.CELL_TYPE_BLANK==cell.getCellType()){
                            rowAndCol[1] = c - dataBeginCol;
                            break;
                        }
                    }
                }
            }
        }else {
            rowAndCol[1] = 0;
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                if (row.getLastCellNum() > rowAndCol[1]) {
                    rowAndCol[1] = row.getLastCellNum();
                }
            }
        }
        return rowAndCol;
    }

    /**
     * 输出资产池蒙特卡洛模拟结果
     * @param portfolioStatisticalResult
     * @param info
     * @param num
     * @param config
     */
    public static String outputPortfolioAnalysisResult(PortfolioStatisticalResult portfolioStatisticalResult, AssetPoolInfo info, Integer num, FileConfig config) throws IOException, InvalidFormatException {
        //File template = ResourceUtils.getFile("classpath:输出模板.xlsx");
        File template = new File(config.getTemplatePath()+File.separator+"输出模板.xlsx");
        FileInputStream fis = new FileInputStream(template);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        MonteSummaryResult monteSummaryResult = portfolioStatisticalResult.getMonteSummaryResult();
        FinalMonteResult monteResult  = portfolioStatisticalResult.getMonteResult();
        //处理填充sheet0---模拟结果输出
        processSheet0(portfolioStatisticalResult, info, num, workbook, monteSummaryResult, monteResult);
        //处理填充sheet1---组合风险缝隙结果输出
        PortfolioDefaultDistribution distribution = portfolioStatisticalResult.getPortfolioDefaultDistribution();
        processSheet1(workbook, distribution);
        //输出
        String childPath = FileUtil.createChildPath(config.getResultPath());
        String fileName = portfolioStatisticalResult.getPortfolioId()+"_"+System.currentTimeMillis()+".xlsx";
        String filePath = childPath+File.separator+fileName;
        portfolioStatisticalResult.setFileName(fileName);
        FileOutputStream os = new FileOutputStream(new File(config.getResultPath()+File.separator+filePath));
        workbook.write(os);
        os.flush();
        os.close();
        fis.close();
        return filePath;
    }

    /**
     * 处理sheet1
     * @param workbook
     * @param distribution
     */
    private static void processSheet1(XSSFWorkbook workbook, PortfolioDefaultDistribution distribution) {
        XSSFSheet sheet1 = workbook.getSheetAt(1);
        double cumulativeDefaultProbability = 0;
        for(int i=0;i<distribution.getDefaultProbability().length;i++){
            XSSFCell cell_1 = sheet1.getRow(4 + i).getCell(2);
            cell_1.setCellValue(distribution.getDefaultProbability()[i]);
            cumulativeDefaultProbability += distribution.getDefaultProbability()[i];
            XSSFCell cell_2 = sheet1.getRow(4 + i).getCell(3);
            cell_2.setCellValue(cumulativeDefaultProbability);
            XSSFCell cell_3 = sheet1.getRow(4 + i).getCell(4);
            cell_3.setCellValue(1-cumulativeDefaultProbability);
        }
        double cumulativeLossProbability = 0;
        for(int i=0;i<distribution.getLossProbability().length;i++){
            XSSFCell cell_1 = sheet1.getRow(4 + i).getCell(7);
            cell_1.setCellValue(distribution.getLossProbability()[i]);
            cumulativeLossProbability += distribution.getLossProbability()[i];
            XSSFCell cell_2 = sheet1.getRow(4 + i).getCell(8);
            cell_2.setCellValue(cumulativeLossProbability);
            XSSFCell cell_3 = sheet1.getRow(4 + i).getCell(9);
            cell_3.setCellValue(1-cumulativeLossProbability);
        }
    }

    /**
     * 处理sheet0
     * @param portfolioStatisticalResult
     * @param info
     * @param num
     * @param workbook
     * @param monteSummaryResult
     * @param monteResult
     * @return
     */
    private static void processSheet0(PortfolioStatisticalResult portfolioStatisticalResult, AssetPoolInfo info, Integer num, XSSFWorkbook workbook, MonteSummaryResult monteSummaryResult, FinalMonteResult monteResult) {
        XSSFSheet sheet0 = workbook.getSheetAt(0);
        XSSFCell cell1 = sheet0.getRow(4).getCell(9);
        cell1.setCellValue("测试");
        XSSFCell cell2 = sheet0.getRow(5).getCell(9);
        cell2.setCellValue(info.getBeginCalculateDate());
        XSSFCell cell3 = sheet0.getRow(6).getCell(9);
        cell3.setCellValue(num);
        XSSFCell cell4 = sheet0.getRow(7).getCell(9);
        cell4.setCellValue(info.getWeightedAverageMaturity());
        XSSFCell cell5 = sheet0.getRow(8).getCell(9);
        cell5.setCellValue(portfolioStatisticalResult.getAverageDefaultRate());
        XSSFCell cell6 = sheet0.getRow(9).getCell(9);
        cell6.setCellValue(portfolioStatisticalResult.getStandardDeviation());
        for(int i=0;i<20;i++){
            XSSFCell cell_1 = sheet0.getRow(5 + i).getCell(3);
            cell_1.setCellValue(monteSummaryResult.getTargetDefaultProbability()[i]);
            XSSFCell cell_2 = sheet0.getRow(5 + i).getCell(4);
            cell_2.setCellValue(monteSummaryResult.getTargetDefaultRate()[i]);
            XSSFCell cell_3 = sheet0.getRow(5 + i).getCell(5);
            cell_3.setCellValue(monteSummaryResult.getTargetRecoveryRate()[i]);
            XSSFCell cell_4 = sheet0.getRow(5 + i).getCell(6);
            cell_4.setCellValue(monteSummaryResult.getTargetLossRate()[i]);
        }
        //输出按季度违约概率
        if(SummaryType.QUARTER.getValue().equals(info.getSummaryType())){
            double[] defaultRateByPeriod = monteResult.getDefaultRateByPeriod();
            for(int i=0;i<defaultRateByPeriod.length;i++){
                sheet0.getRow(13+i).getCell(9).setCellValue(defaultRateByPeriod[i]);
            }
        }

    }

    /**
     * 获取单元格对应值
     * @param cell
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getCellValue(Cell cell, Class<T> clazz) {
        Object result = null;
        if (cell!=null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                default:
                    break;
            }

            if(result !=null){
                if(!StringUtils.isEmpty(result)){
                    return clazz.cast(result);
                }
            }
        }
        return null;
    }
}
