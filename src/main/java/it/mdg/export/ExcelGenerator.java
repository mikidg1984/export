package it.mdg.export;

import it.mdg.export.annotation.ExcelColumn;
import it.mdg.export.annotation.ExcelSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Class ExcelGenerator
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 27 February 2024 - 22:40
 * @project export
 */
public class ExcelGenerator<T> {

    private List<T> dataList;
    private Class<T> tClass;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator(List<T> dataList, Class<T> tClass) {
        this.dataList = dataList;
        this.tClass = tClass;
        workbook = new XSSFWorkbook();
    }

    private String sheetName(){
        return Optional
                .ofNullable(tClass.getAnnotation(ExcelSheet.class))
                .map(ExcelSheet::name)
                .filter(n->!n.isBlank())
                .orElse(tClass.getSimpleName());
    }

    private String fieldName(Field field){
        return Optional
                .ofNullable(field.getAnnotation(ExcelColumn.class))
                .map(ExcelColumn::name)
                .filter(n->!n.isBlank())
                .orElse(field.getName());
    }

    private void autosize(){
        IntStream
            .range(0, tClass.getFields().length)
            .forEach(sheet::autoSizeColumn);
    }

    private void writeHeader() {
        sheet = workbook.createSheet(sheetName());
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        int cellCounter = 0;
        for (Field field : tClass.getDeclaredFields()) {
            createCell(row, cellCounter++, fieldName(field), style);
        }
    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        if(valueOfCell == null || (valueOfCell instanceof String && ((String)valueOfCell).isEmpty())) {
            return;
        }
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
    private void write() throws IllegalAccessException {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (T record: dataList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Field field : tClass.getDeclaredFields()) {
                field.setAccessible(true);
                createCell(row, columnCount++, field.get(record), style);
            }
        }
    }
    public void write(OutputStream stream) throws IOException, IllegalAccessException {
        writeHeader();
        write();
        autosize();
        workbook.write(stream);
        workbook.close();
        stream.close();
    }
}