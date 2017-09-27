package com.unitedratings.lhcrm;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import org.ujmp.core.objectmatrix.impl.DefaultDenseObjectMatrix2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.util.Iterator;
import java.util.Random;

public class Generic {

    @Test
    public void testUniform(){
        Random random = new Random();
        for (int i=0;i<20000;i++){
            System.out.print(random.nextGaussian()+"\t");
            if(i%100==0){
                System.out.println();
            }
        }
    }


    public static void main(String[] args) throws IOException, InvalidFormatException {

        final OPCPackage pkg = OPCPackage.open(new File("/Users/wangyongxin/Desktop/模板.xlsx"));
        final XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        //规定sheet0为资产池信息
        XSSFSheet sheet0 = workbook.getSheetAt(0);
        System.out.println(sheet0.getLastRowNum());
        Iterator<Row> rowIterator = sheet0.rowIterator();
        int columnNum = 0;
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            if (row.getLastCellNum() > columnNum) {
                columnNum = row.getLastCellNum();
            }
        }
        System.out.println(columnNum);
        //封装资产池信息实体
        //1.确定贷款笔数
        int validateRow = 0;
        t:for (int r = 3; r < sheet0.getLastRowNum(); r++) {
            Row row = sheet0.getRow(r);
            if (row != null) {
                for (int c = 0; c < columnNum; c++) {
                    Cell cell = row.getCell(c);
                    if(c==0){
                        validateRow = r-3;
                        if(Cell.CELL_TYPE_BLANK==cell.getCellType()){
                            break t;
                        }
                    }
                }
            }
        }
        DecimalFormat df = new DecimalFormat("0");
        Matrix matrix = new DefaultDenseObjectMatrix2D(validateRow,columnNum);
        matrix.setLabel(sheet0.getSheetName());
        for (int r = 3; r < sheet0.getLastRowNum(); r++) {
            Row row = sheet0.getRow(r);
            if (row != null) {
                for (int c = 0; c < columnNum; c++) {
                    Cell cell = row.getCell(c);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                matrix.setAsBoolean(cell.getBooleanCellValue(), r-3, c);
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                matrix.setAsString(cell.getCellFormula(), r-3, c);
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                matrix.setAsDouble(cell.getNumericCellValue(), r-3, c);
                                break;
                            case Cell.CELL_TYPE_STRING:
                                matrix.setAsString(cell.getStringCellValue(), r-3, c);
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
        }
        pkg.close();
        matrix.showGUI();
        System.out.println(matrix);

        /*MatrixXLSXImporter matrixXLSXImporter = new MatrixXLSXImporter();
        DenseObjectMatrix2D matrix2D = matrixXLSXImporter.importFromXLSX("/Users/wangyongxin/Downloads/CLO国兴租赁终版.xlsm", 0);
        System.out.println(matrix2D.subMatrix(Calculation.Ret.NEW,15,1,28,27));
        System.out.println("======================");
        System.out.println(matrix2D.subMatrix(Calculation.Ret.NEW,15,5,27,5).convert(ValueType.STRING));
        System.out.println("======================");
        System.out.println(matrix2D.select(Calculation.Ret.NEW,"16;17"));
        System.out.println("======================");
        System.out.println(matrix2D.selectColumns(Calculation.Ret.NEW,16,17));*/
    }

    @Test
    public void testPoi() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("/Users/wangyongxin/Downloads/CLO国兴租赁终版.xlsm"));
        for(XSSFSheet sheet:workbook){
            System.out.println("==========="+sheet.getSheetName()+"============");
            for(Row row:sheet){
                for(Cell cell:row){
                    if(cell.getCellType()==Cell.CELL_TYPE_BOOLEAN){
                        System.out.print(cell.getBooleanCellValue());
                    }else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
                        System.out.print(cell.getNumericCellValue());
                    }else if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                        System.out.print(cell.getStringCellValue());
                    }else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA){
                        System.out.print(cell.getCellFormula());
                    }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
                        System.out.print("\t");
                    }
                    System.out.print("\t");
                }
                System.out.println();
            }
            System.out.println("========end========");
        }
    }

    @Test
    public void testDate(){
        LocalDate date = LocalDate.of(2019,6,15);
        System.out.println(date.minusDays(43631));
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void tesMatrix(){
        DenseMatrix zeros = Matrix.Factory.zeros(12, 10);
        System.out.println(zeros.getValueCount());
        System.out.println(ZoneId.systemDefault());
    }

    @Test
    public void tesGaussian(){
        NormalDistribution normalDistribution = new NormalDistribution();
        System.out.println(normalDistribution.cumulativeProbability(1));
        System.out.println(normalDistribution.density(0));
        System.out.println(normalDistribution.getNumericalVariance());
        System.out.println(normalDistribution.getSupportUpperBound());
        System.out.println(normalDistribution.getSupportLowerBound());
        System.out.println(normalDistribution.sample());
        System.out.println(normalDistribution.probability(0,1));
        System.out.println(normalDistribution.inverseCumulativeProbability(0.84));
    }

    @Test
    public void testTime(){
        System.out.println(LocalDate.now().getYear());
        System.out.println(LocalDate.now().getMonth().getValue());
    }

}
