package com.unitedratings.lhcrm.excelprocess;

import com.unitedratings.lhcrm.constants.SummaryType;
import com.unitedratings.lhcrm.domains.AssetPool;
import com.unitedratings.lhcrm.domains.AssetPoolInfo;
import com.unitedratings.lhcrm.domains.LoanRecord;
import com.unitedratings.lhcrm.entity.*;
import com.unitedratings.lhcrm.exception.BusinessException;
import com.unitedratings.lhcrm.utils.DateUtil;
import com.unitedratings.lhcrm.utils.ExcelUtil;
import com.unitedratings.lhcrm.utils.MassExcelDataRead;
import com.unitedratings.lhcrm.utils.MathUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.doublematrix.impl.DefaultDenseDoubleMatrix2D;
import org.ujmp.core.objectmatrix.impl.DefaultDenseObjectMatrix2D;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 资产池违约概率模型excel模板处理类
 * @author wangyongxin
 */
public class AssetsExcelProcess {

    /**
     * 处理资产池excel模板
     * @param file excel文件
     * @param assetPool 资产池模拟实体
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static void processAssetsExcel(File file, AssetPool assetPool) throws Exception {
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
                    //processPerfectDefaultRateSheet(workbook.getSheetAt(index),assetPool.getAssetPoolInfo());
                    break;
                case 4:
                    //处理条件违约概率矩阵信息sheet(临时使用，季度数据)
                    /*if(assetPool.getAssetPoolInfo().getSummaryType() == SummaryType.QUARTER.getValue()){
                        processConditionDefaultRateSheet(workbook.getSheetAt(index),assetPool.getAssetPoolInfo());
                    }*/
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
        //获取分期摊还信息sheet有效行列数,模板中第0、1索引行为表头信息，有效数据从第2索引行开始（需要1索引行表头信息，所以数据从第1所银行开始）
        final int dataBeginRow = 2;
        final int dataBeginCol = 6;
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
    private static void processAssetPoolSheet(XSSFSheet sheet, AssetPool assetPool) throws BusinessException {
        try {
            //获取资产池sheet有效行列数,模板中第0-1索引行为表头信息，有效数据从第3索引行开始
            final int dataBeginRow = 2;
            final int dataBeginCol = 0;
            //确定有效行列
            int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,false);
            int validRowSize = validRowAndColSize[0];
            List<LoanRecord> loanRecords = new ArrayList<>();
            for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
                Row row = sheet.getRow(r);
                if (row != null) {
                    //封装贷款信息实体
                    loanRecords.add(assembleLoanRecord(row));
                }
            }
            assetPool.setLoanRecords(loanRecords);
        } catch (Exception e) {
            throw new BusinessException("000007","处理资产池信息sheet过程异常",e);
        }
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
        /*
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
        */
        //计算加权平均借款期限
        double[] yearMaturity = new double[size];
        for(int i = 0;i<maturityMatrix.getRowCount();i++){
            yearMaturity[i] = DateUtil.calculatePeriods(SummaryType.YEAR.getValue(),assetPoolInfo.getBeginCalculateDate(),DateUtil.parseDate(maturityMatrix.getAsString(i,0),"yyyy-MM-dd"));
        }
        assetPoolInfo.setYearMaturity(yearMaturity);
        assetPoolInfo.setWeightedAverageMaturity(MathUtil.calculateWeightedAverageMaturity(assetPoolInfo.getPrincipal(),yearMaturity));
    }

    /**
     * 封装贷款记录
     * @param row
     * @return
     */
    private static LoanRecord assembleLoanRecord(Row row) {
        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setDebtorInfo(assembleDebtorInfo(row));
        loanRecord.setGuarantorInfo(assembleGuarantorInfo(row));
        return loanRecord;
    }

    /**
     * 封装保证人信息
     * @param row
     * @return
     */
    private static GuarantorInfo assembleGuarantorInfo(Row row) {
        DecimalFormat df = new DecimalFormat("0");
        GuarantorInfo guarantorInfo = new GuarantorInfo();
        guarantorInfo.setGuaranteeMode(row.getCell(15).getStringCellValue());
        guarantorInfo.setGuaranteeName(row.getCell(16).getStringCellValue());
        guarantorInfo.setLiabilityForm(row.getCell(17).getStringCellValue());
        guarantorInfo.setGuaranteeRatio(row.getCell(18).getNumericCellValue());
        guarantorInfo.setGuaranteeCreditLevel(row.getCell(19).getStringCellValue());
        guarantorInfo.setGuaranteeIndustryCode(Long.parseLong(df.format(row.getCell(20).getNumericCellValue())));
        guarantorInfo.setGuaranteeBelongArea(row.getCell(21).getStringCellValue());
        guarantorInfo.setGuaranteeNum(Integer.parseInt(df.format(row.getCell(22).getNumericCellValue())));
        return guarantorInfo;
    }

    /**
     * 封装借款人信息
     * @param row
     * @return
     */
    private static DebtorInfo assembleDebtorInfo(Row row) {
        DebtorInfo debtorInfo = new DebtorInfo();
        DecimalFormat df = new DecimalFormat("0");
        String formatLoanSerial = df.format(row.getCell(0).getNumericCellValue());
        debtorInfo.setLoanSerial(Long.parseLong(formatLoanSerial));
        debtorInfo.setBorrowerSerial(Long.parseLong(df.format(row.getCell(1).getNumericCellValue())));
        debtorInfo.setIndustryCode(Long.parseLong(df.format(row.getCell(2).getNumericCellValue())));
        debtorInfo.setCreditLevel(row.getCell(3).getStringCellValue());
        debtorInfo.setMaturityDate(row.getCell(4).getDateCellValue());
        debtorInfo.setLoanBalance(row.getCell(5).getNumericCellValue());
        debtorInfo.setLendingRate(row.getCell(6).getNumericCellValue());
        debtorInfo.setAssetSelfRecoveryRate(row.getCell(7).getNumericCellValue());
        debtorInfo.setBorrowerArea(row.getCell(8).getStringCellValue());
        debtorInfo.setLoanProvideDate(row.getCell(9).getDateCellValue());
        String isAmortize = row.getCell(10).getStringCellValue();
        if("是".equals(isAmortize)){
            debtorInfo.setAmortize(true);
        }else {
            debtorInfo.setAmortize(false);
        }
        String governmentFunded = row.getCell(13).getStringCellValue();
        if("是".equals(governmentFunded)){
            debtorInfo.setGovernmentFunded(true);
        }else {
            debtorInfo.setGovernmentFunded(false);
        }
        //违约放大倍数（中间过程输出）
        debtorInfo.setDefaultMagnification(row.getCell(14).getNumericCellValue());
        debtorInfo.setBorrowerIndustry(row.getCell(25).getStringCellValue());
        debtorInfo.setCurrentMarketValue(row.getCell(26).getNumericCellValue());
        debtorInfo.setBorrowerName(row.getCell(27).getStringCellValue());
        //考虑保证人的回收率（结果输出）
        //debtorInfo.setGuaranteeRecoveryRate(row.getCell(28).getNumericCellValue());
        debtorInfo.setRelevanceForGuaranteeAndLender(ExcelUtil.getCellValue(row.getCell(30), Double.class));
        debtorInfo.setMortgageRecoveryRate(row.getCell(31).getNumericCellValue());
        //债券等级（中间过程输出）
        //debtorInfo.setDebtLevel(row.getCell(32).getStringCellValue());
        //最终回收率（结果输出）
        //debtorInfo.setFinalRecoveryRate(row.getCell(33).getNumericCellValue());
        //累计回收率（结果输出）
        //debtorInfo.setTotalDefaultRate(row.getCell(34).getNumericCellValue());
        debtorInfo.setDepositAmount(row.getCell(42).getNumericCellValue());

        return debtorInfo;
    }

    /**
     * 处理上传的资产池excel
     * @param file
     * @param portfolio
     */
    public static void processAssetsExcel(MultipartFile file, Portfolio portfolio) throws Exception {
        final OPCPackage pkg = OPCPackage.open(file.getInputStream());
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        AssetPool assetPool = new AssetPool();
        AssetPoolInfo assetPoolInfo = new AssetPoolInfo();
        assetPool.setAssetPoolInfo(assetPoolInfo);
        for(int index=0;index<workbook.getNumberOfSheets();index++){
            switch (index){
                //规定第1、2、3个sheet分别为资产池信息sheet、分期摊还信息sheet
                case 0:
                    //处理资产池信息sheet
                    processAssetPoolSheet(workbook.getSheetAt(index),assetPool);
                    portfolio.setRecordList(assetPool.getLoanRecords());
                    break;
                case 1:
                    //处理分期摊还信息sheet
                    portfolio.setAmortization(assembleAmortization(workbook.getSheetAt(index),portfolio));
                    break;
                default:
                    break;
            }
        }
        pkg.close();
    }

    /**
     * 封装分期摊还信息
     * @param sheet
     * @param portfolio
     * @return
     */
    private static Amortization assembleAmortization(XSSFSheet sheet, Portfolio portfolio) throws BusinessException {
        Amortization amortization = null;
        try {
            amortization = new Amortization();
            XSSFRow row = sheet.getRow(1);
            final int dataBeginRow = 2;
            final int dataBeginCol = 6;
            //确定有效行列
            int[] validRowAndColSize = ExcelUtil.getRegularValidRowAndColSize(sheet, dataBeginRow, dataBeginCol,true);
            int validRowSize = validRowAndColSize[0];
            int validColSize = validRowAndColSize[1];
            StringBuilder dateStr = new StringBuilder();
            for(int i=dataBeginCol;i<dataBeginCol+validColSize;i++){
                XSSFCell cell = row.getCell(i);
                if(cell!=null){
                    dateStr.append(DateUtil.formatDate(cell.getDateCellValue(),"yyyy-MM-dd")).append(",");
                }
                if(i==dataBeginCol+validColSize-1){
                    amortization.setAmortizationDate(dateStr.substring(0, dateStr.length() - 1));
                }
            }
            //处理数据
            List<AmortizationInfo> infoList = new ArrayList<>();
            for (int r = dataBeginRow; r < validRowSize + dataBeginRow; r++) {
                Row tempRow = sheet.getRow(r);
                if (tempRow != null) {
                    //封装分期摊还信息
                    AmortizationInfo amortizationInfo = new AmortizationInfo();
                    amortizationInfo.setLoanSerial(new Double(tempRow.getCell(0).getNumericCellValue()).longValue());
                    StringBuilder sb = new StringBuilder();
                    for (int c = dataBeginCol; c < validColSize+dataBeginCol; c++) {
                        Cell cell = tempRow.getCell(c);
                        if (cell != null) {
                            if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
                                sb.append(cell.getNumericCellValue()).append(",");
                            }
                        }
                        if(c == validColSize+dataBeginCol-1){
                            amortizationInfo.setAmortization(sb.substring(0,sb.length()-1));
                        }
                    }
                    infoList.add(amortizationInfo);
                }
            }
            amortization.setAmortizationInfoList(infoList);
        } catch (Exception e) {
            throw new BusinessException("000006","处理分期摊还sheet过程异常",e);
        }
        return amortization;
    }

    /**
     * 处理随机数矩阵信息sheet
     */
    public static List<Matrix> processRandomSheet() throws IOException {
        File template = new File("/Users/wangyongxin/Desktop/random2.xlsx");
        FileInputStream fis = new FileInputStream(template);
        final XSSFWorkbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int validRowSize = sheet.getLastRowNum()+1;
        final int loanNum = 18;//贷款笔数
        final int quarter = 6;//最大季度数
        List<Matrix> list = new ArrayList<>();
        for(int r=0;r<validRowSize;r++){
            //处理数据
            Matrix matrix = new DefaultDenseDoubleMatrix2D(loanNum, quarter);
            matrix.setLabel(sheet.getSheetName());
            Row tempRow = sheet.getRow(r);
            if (tempRow != null) {
                //封装随机数矩阵信息
                for (int c = 0; c <= tempRow.getLastCellNum(); c++) {
                    Cell cell = tempRow.getCell(c);
                    if (cell != null) {
                        int x = c%loanNum;
                        int y = c/loanNum;
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                matrix.setAsBoolean(cell.getBooleanCellValue(), x, y);
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                matrix.setAsString(cell.getCellFormula(), x, y);
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                matrix.setAsDouble(cell.getNumericCellValue(), x, y);
                                break;
                            case Cell.CELL_TYPE_STRING:
                                matrix.setAsString(cell.getStringCellValue(), x, y);
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
            list.add(matrix);
        }
        fis.close();
        return list;
    }

    /**
     * 处理数据量庞大的excel
     * @return
     * @throws OpenXML4JException
     * @throws SAXException
     * @throws IOException
     */
    public static List<Matrix> processMassRandomSheet() throws OpenXML4JException, IOException, SAXException {
        File file = new File("/Users/wangyongxin/Desktop/random1.xlsx");
        OPCPackage p = OPCPackage.open(file, PackageAccess.READ);
        MassExcelDataRead massExcelDataRead = new MassExcelDataRead(p,60,8);
        massExcelDataRead.process();
        p.close();
        return massExcelDataRead.getMatrices();
    }

    /**
     * 输出矩阵至excel
     * @param matrixs
     */
    public static void outputMatrixToExcel(List<Matrix> matrixs) {
        if(!CollectionUtils.isEmpty(matrixs)){
            OutputStream os = null;
            try {
                String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xlsx";
                File file = new File("/Users/wangyongxin/Desktop/"+fileName);
                boolean created = false;
                if(!file.exists()){
                    created = file.createNewFile();
                }
                if (created){
                    Workbook workbook = new XSSFWorkbook();
                    Iterator<Matrix> iterator = matrixs.iterator();
                    while (iterator.hasNext()){
                        createSheet(workbook, iterator.next());
                    }
                    os = new BufferedOutputStream(new FileOutputStream(file));
                    workbook.write(os);
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(os!=null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 输出矩阵至excel
     * @param matrix
     */
    public static void outputMatrixToExcel(Matrix matrix) {
        OutputStream os = null;
        try {
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xlsx";
            File file = new File("/Users/wangyongxin/Desktop/"+fileName);
            boolean created = false;
            if(!file.exists()){
                created = file.createNewFile();
            }
            if (created){
                Workbook workbook = new XSSFWorkbook();
                createSheet(workbook, matrix);
                os = new BufferedOutputStream(new FileOutputStream(file));
                workbook.write(os);
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createSheet(Workbook workbook, Matrix matrix) {
        Sheet sheet;
        if(!StringUtils.isEmpty(matrix.getLabel())){
            sheet = workbook.createSheet(matrix.getLabel());
        }else {
            sheet = workbook.createSheet();
        }
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        for (int r=0;r<matrix.getRowCount();r++){
            Row row = sheet.createRow(r);
            for(int c=0;c<matrix.getColumnCount();c++){
                Cell cell = row.createCell(c);
                cell.setCellValue(matrix.getAsDouble(r,c));
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellStyle(cellStyle);
            }
        }
    }
}
