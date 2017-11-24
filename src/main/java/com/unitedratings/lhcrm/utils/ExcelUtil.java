package com.unitedratings.lhcrm.utils;

import com.unitedratings.lhcrm.config.FileConfig;
import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangyongxin
 */
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
     * @param assetPool
     * @param num
     * @param config
     */
    public static String outputPortfolioAnalysisResult(PortfolioStatisticalResult portfolioStatisticalResult, AssetPool assetPool, Integer num, FileConfig config) throws IOException, InvalidFormatException {
        AssetPoolInfo info = assetPool.getAssetPoolInfo();
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
        //处理填充sheet2---资产池统计量
        processSheet2(workbook,assetPool.getAssetPoolSummaryResult());
        //输出
        String childPath = FileUtil.createChildPath(config.getResultPath());
        String fileName = info.getPortfolioName()+"_"+DateUtil.getTimestamp()+".xlsx";
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
     * 将资产池统计量写入sheet2
     * @param workbook
     * @param assetPoolSummaryResult
     */
    private static void processSheet2(XSSFWorkbook workbook, AssetPoolSummaryResult assetPoolSummaryResult) {
        XSSFSheet sheet = workbook.getSheetAt(2);
        //输出统计量
        outputStatisticResult(assetPoolSummaryResult.getStatisticalResult(), sheet);
        //输出统计分布
        outputDistribution(assetPoolSummaryResult, sheet,workbook);
    }

    private static void outputDistribution(AssetPoolSummaryResult assetPoolSummaryResult, XSSFSheet sheet, XSSFWorkbook workbook) {
        IndustryDistribution industryDistribution = assetPoolSummaryResult.getIndustryDistribution();
        CellStyle defaultCellStyle = workbook.createCellStyle();
        defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        defaultCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        List<? extends Distribution.Statistical> details = null;
        if(industryDistribution!=null){
            details = industryDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                IndustryDistribution.IndustryStatistical statistical = (IndustryDistribution.IndustryStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(1).setCellValue(statistical.getIndustryName());
                row.getCell(3).setCellValue(statistical.getDebtNum());
                row.getCell(4).setCellValue(statistical.getLoanNum());
                row.getCell(5).setCellValue(statistical.getAmount());
                row.getCell(6).setCellValue(statistical.getProportion());
            }
        }
        AreaDistribution areaDistribution = assetPoolSummaryResult.getAreaDistribution();
        if(areaDistribution!=null){
            details = areaDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                AreaDistribution.AreaStatistical statistical = (AreaDistribution.AreaStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(8).setCellValue(statistical.getAreaName());
                row.getCell(9).setCellValue(statistical.getDebtNum());
                row.getCell(10).setCellValue(statistical.getLoanNum());
                row.getCell(11).setCellValue(statistical.getAmount());
                row.getCell(12).setCellValue(statistical.getProportion());
            }
        }
        DebtorCreditRankDistribution debtorCreditRankDistribution = assetPoolSummaryResult.getDebtorCreditRankDistribution();
        if(debtorCreditRankDistribution!=null){
            details = debtorCreditRankDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                DebtorCreditRankDistribution.DebtorCreditRankStatistical statistical = (DebtorCreditRankDistribution.DebtorCreditRankStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(14).setCellValue(statistical.getCreditLevel());
                row.getCell(15).setCellValue(statistical.getDebtNum());
                row.getCell(16).setCellValue(statistical.getLoanNum());
                row.getCell(17).setCellValue(statistical.getAmount());
                row.getCell(18).setCellValue(statistical.getProportion());
            }
        }
        ResidualMaturityDistribution residualMaturityDistribution = assetPoolSummaryResult.getResidualMaturityDistribution();
        if(residualMaturityDistribution!=null){
            details = residualMaturityDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                ResidualMaturityDistribution.ResidualMaturityStatistical statistical = (ResidualMaturityDistribution.ResidualMaturityStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(20).setCellValue(statistical.getResidualMaturity());
                row.getCell(21).setCellValue(statistical.getDebtNum());
                row.getCell(22).setCellValue(statistical.getLoanNum());
                row.getCell(23).setCellValue(statistical.getAmount());
                row.getCell(24).setCellValue(statistical.getProportion());
            }
        }
        GuaranteeModeDistribution guaranteeModeDistribution = assetPoolSummaryResult.getGuaranteeModeDistribution();
        if(guaranteeModeDistribution!=null){
            details = guaranteeModeDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                GuaranteeModeDistribution.GuaranteeModeStatistical statistical = (GuaranteeModeDistribution.GuaranteeModeStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(26).setCellValue(statistical.getGuaranteeMode());
                row.getCell(27).setCellValue(statistical.getDebtNum());
                row.getCell(28).setCellValue(statistical.getLoanNum());
                row.getCell(29).setCellValue(statistical.getAmount());
                row.getCell(30).setCellValue(statistical.getProportion());
            }
        }
        DebtorDistribution debtorDistribution = assetPoolSummaryResult.getDebtorDistribution();
        if(debtorDistribution!=null){
            details = debtorDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                DebtorDistribution.DebtorStatistical statistical = (DebtorDistribution.DebtorStatistical) details.get(i);
                int r = 24 + i;
                silentSetCellValue(sheet, r,32,statistical.getLoanSerial(),defaultCellStyle);
                silentSetCellValue(sheet, r,33,statistical.getBorrower(),defaultCellStyle);
                silentSetCellValue(sheet, r,34,statistical.getLoanNum(),defaultCellStyle);
                silentSetCellValue(sheet, r,35,statistical.getCreditLevel(),defaultCellStyle);
                silentSetCellValue(sheet, r,36,statistical.getIndustryName(),defaultCellStyle);
                silentSetCellValue(sheet, r,37,statistical.getAreaName(),defaultCellStyle);
                silentSetCellValue(sheet, r,38,statistical.getAmount(),defaultCellStyle);
                silentSetCellValue(sheet, r,39,statistical.getProportion(),defaultCellStyle);
            }
        }
        GuaranteeCreditRankDistribution guaranteeCreditRankDistribution = assetPoolSummaryResult.getGuaranteeCreditRankDistribution();
        if(guaranteeCreditRankDistribution!=null){
            details = guaranteeCreditRankDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical statistical = (GuaranteeCreditRankDistribution.GuaranteeCreditRankStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(41).setCellValue(statistical.getCreditLevel());
                row.getCell(42).setCellValue(statistical.getGuaranteeNum());
                row.getCell(43).setCellValue(statistical.getGuaranteeLoanNum());
                row.getCell(44).setCellValue(statistical.getAmount());
                row.getCell(45).setCellValue(statistical.getProportion());
            }
        }
        LoanCreditRankDistribution loanCreditRankDistribution = assetPoolSummaryResult.getLoanCreditRankDistribution();
        if(loanCreditRankDistribution!=null){
            details = loanCreditRankDistribution.getDetails();
            for(int i = 0; i< details.size(); i++){
                LoanCreditRankDistribution.LoanCreditRankStatistical statistical = (LoanCreditRankDistribution.LoanCreditRankStatistical) details.get(i);
                XSSFRow row = sheet.getRow(24 + i);
                row.getCell(47).setCellValue(statistical.getCreditLevel());
                row.getCell(48).setCellValue(statistical.getDebtNum());
                row.getCell(49).setCellValue(statistical.getLoanNum());
                row.getCell(50).setCellValue(statistical.getAmount());
                row.getCell(51).setCellValue(statistical.getProportion());
            }
        }
    }

    /**
     * 给单元格设置值
     * @param sheet
     * @param r
     * @param c
     * @param value
     * @param defaultCellStyle
     */
    public static void silentSetCellValue(XSSFSheet sheet, int r, int c, Object value, CellStyle defaultCellStyle) {
        XSSFRow row = sheet.getRow(r);
        if(value!=null){
            if(row!=null){
                XSSFCell cell = row.getCell(c);
                if(cell==null){
                    cell = row.createCell(c);
                    cell.setCellStyle(defaultCellStyle);
                }
                setCellValueInternal(cell, value);
            } else {
                XSSFCell cell = sheet.createRow(r).createCell(c);
                cell.setCellStyle(defaultCellStyle);
                setCellValueInternal(cell, value);
            }
        }else {
            if(row==null){
                sheet.createRow(r).createCell(c,Cell.CELL_TYPE_BLANK);
            }
        }
    }

    /**
     * 内部调用，处理设置cell值
     * @param cell
     * @param value
     */
    private static void setCellValueInternal(XSSFCell cell, Object value) {
        if(Number.class.isAssignableFrom(value.getClass())){
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            if(value instanceof Integer){
                cell.setCellValue((Integer)value);
            }else if(value instanceof Long){
                cell.setCellValue((Long)value);
            }else if(value instanceof Double){
                cell.setCellValue((Double) value);
            }else if(value instanceof Date){
                cell.setCellValue((Date) value);
            }
        }else if(value instanceof String){
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue((String)value);
        }
    }

    private static void outputStatisticResult(StatisticalResult statisticalResult, XSSFSheet sheet) {
        sheet.getRow(4).getCell(3).setCellValue(statisticalResult.getTransactionName());
        sheet.getRow(5).getCell(3).setCellValue(statisticalResult.getBeginCalculateDate());
        sheet.getRow(6).getCell(3).setCellValue(statisticalResult.getSimulationTimes());
        sheet.getRow(7).getCell(3).setCellValue(statisticalResult.getAssetServiceInstitution());
        sheet.getRow(8).getCell(3).setCellValue(statisticalResult.getSponsorOrganization());
        sheet.getRow(9).getCell(3).setCellValue(statisticalResult.getTrustInstitution());
        sheet.getRow(10).getCell(3).setCellValue(statisticalResult.getLoanNum());
        sheet.getRow(11).getCell(3).setCellValue(statisticalResult.getDebtorNum());
        sheet.getRow(12).getCell(3).setCellValue(statisticalResult.getOutstandingPrincipal());
        sheet.getRow(13).getCell(3).setCellValue(statisticalResult.getWeightedYearMaturity());
        sheet.getRow(14).getCell(3).setCellValue(statisticalResult.getLongestMaturity());
        sheet.getRow(15).getCell(3).setCellValue(statisticalResult.getShortestMaturity());
        sheet.getRow(16).getCell(3).setCellValue(statisticalResult.getWeightedAverageRecoverRate());
        sheet.getRow(17).getCell(3).setCellValue(statisticalResult.getWeightedDebtorCreditLevel());
        sheet.getRow(18).getCell(3).setCellValue(statisticalResult.getWeightedDebtorDefaultRate());
        sheet.getRow(19).getCell(3).setCellValue(statisticalResult.getWeightedLoanCreditLevel());
        sheet.getRow(20).getCell(3).setCellValue(statisticalResult.getWeightedLoanDefaultRate());
        sheet.getRow(14).getCell(6).setCellValue(statisticalResult.getAging());
        sheet.getRow(15).getCell(6).setCellValue(statisticalResult.getWeightedAverageInterestRate());
        sheet.getRow(16).getCell(6).setCellValue(statisticalResult.getWeightedDebtorSelfRecoverRate());
        sheet.getRow(17).getCell(6).setCellValue(statisticalResult.getWeightedGuaranteePromotedRecoverRate());
        sheet.getRow(18).getCell(6).setCellValue(statisticalResult.getWeightedCollateralAverageRecoverRate());
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
        cell1.setCellValue(info.getPortfolioName());
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
        XSSFCell cell7 = sheet0.getRow(10).getCell(9);
        cell7.setCellValue(portfolioStatisticalResult.getAverageRecoveryRate());
        for(int i=0;i<20;i++){
            XSSFCell cell_1 = sheet0.getRow(5 + i).getCell(3);
            cell_1.setCellValue(monteSummaryResult.getTargetDefaultProbability()[i]);
            XSSFCell cell_2 = sheet0.getRow(5 + i).getCell(4);
            cell_2.setCellValue(monteSummaryResult.getTargetDefaultRate()[i]);
            /*XSSFCell cell_3 = sheet0.getRow(5 + i).getCell(5);
            cell_3.setCellValue(monteSummaryResult.getTargetRecoveryRate()[i]);*/
            XSSFCell cell_4 = sheet0.getRow(5 + i).getCell(6);
            cell_4.setCellValue(monteSummaryResult.getTargetLossRate()[i]);
        }
        //输出按季度违约概率
        if(SummaryType.QUARTER.getValue().equals(info.getSummaryType())){
            CellStyle defaultCellStyle = workbook.createCellStyle();
            defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            defaultCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            double[] defaultRateByPeriod = monteResult.getDefaultRateByPeriod();
            for(int i=0;i<defaultRateByPeriod.length;i++){
                silentSetCellValue(sheet0,13+i,9,defaultRateByPeriod[i],defaultCellStyle);
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
