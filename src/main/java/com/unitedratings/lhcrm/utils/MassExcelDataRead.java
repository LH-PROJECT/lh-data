package com.unitedratings.lhcrm.utils;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ujmp.core.Matrix;
import org.ujmp.core.objectmatrix.impl.DefaultDenseObjectMatrix2D;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyongxin
 * @createAt 2017-10-22 下午9:44
 **/
public class MassExcelDataRead {

    private static final Logger LOGGER = LoggerFactory.getLogger(MassExcelDataRead.class);

    private final OPCPackage xlsxPackage;

    private final List<Matrix> matrices = new ArrayList<>();

    /**
     * 矩阵行数
     */
    private final int rowSize;

    /**
     * 矩阵列数
     */
    private final int colSize;

    /**
     *
     * @param pkg
     */
    public MassExcelDataRead(OPCPackage pkg,int rowSize,int colSize){
        this.xlsxPackage = pkg;
        this.rowSize = rowSize;
        this.colSize = colSize;
    }

    /**
     * 处理excel sheet
     * @param styles
     * @param strings
     * @param sheetHandler
     * @param sheetInputStream
     * @throws IOException
     * @throws SAXException
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        XMLReader sheetParser = XMLReaderFactory.createXMLReader();
        ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheetHandler, formatter, false);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
    }

    public void process() throws IOException, OpenXML4JException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            LOGGER.debug("开始处理"+sheetName + " [index=" + index + "]:");
            processSheet(styles, strings, new SheetToMatrix(), stream);
            stream.close();
            LOGGER.debug("处理"+sheetName + " [index=" + index + "]结束");
            ++index;
        }
    }

    public List<Matrix> getMatrices() {
        return matrices;
    }

    /**
     * 将excel单元格数据存储至矩阵
     */
    private class SheetToMatrix implements SheetContentsHandler {

        private boolean firstCellOfRow = false;
        private int currentRow = -1;
        private int currentCol = -1;
        private Matrix matrix;

        @Override
        public void startRow(int rowNum) {
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
        }

        @Override
        public void endRow() {
            matrices.add(matrix);
            //System.out.println();
        }

        @Override
        public void cell(String cellReference, String formattedValue) {
            if (firstCellOfRow) {
                firstCellOfRow = false;
                matrix = new DefaultDenseObjectMatrix2D(rowSize,colSize);
            }

            if(cellReference == null) {
                cellReference = new CellReference(currentRow, currentCol).formatAsString();
            }

            currentCol = (new CellReference(cellReference)).getCol();

            // 数值类型 或 字符串类型
            int r = currentCol/colSize;
            int c = currentCol%colSize;
            try {
                matrix.setAsDouble(Double.parseDouble(formattedValue),r,c);
            } catch (NumberFormatException e) {
                matrix.setAsString(formattedValue,r,c);
            }
            //System.out.print(formattedValue+"\t");
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {

        }
    }

    public static void main(String[] args) throws Exception {
        File xlsxFile = new File("/Users/wangyongxin/Desktop/random1.xlsx");
        OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
        MassExcelDataRead massExcelDataRead = new MassExcelDataRead(p,60,8);
        massExcelDataRead.process();
        p.close();
    }

}
