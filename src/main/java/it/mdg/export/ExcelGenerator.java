package it.mdg.export;

import it.mdg.export.annotation.ExcelColumn;
import it.mdg.export.annotation.ExcelFormat;
import it.mdg.export.annotation.ExcelIgnore;
import it.mdg.export.annotation.ExcelSheet;
import it.mdg.export.formatter.FieldFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
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
            .range(0, tClass.getDeclaredFields().length)
            .forEach(sheet::autoSizeColumn);
    }

    private Predicate<Field> notIgnored = field -> field.getAnnotation(ExcelIgnore.class) == null;

    private Object format(Field field,T record) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        ExcelFormat excelFormat = field.getAnnotation(ExcelFormat.class);

        if(excelFormat == null){
            return field.get(record);
        }

        Class<? extends FieldFormatter> formatterClass = excelFormat.formatter();

        if(!formatterClass.getCanonicalName().equals(FieldFormatter.None.CANONICAL_NAME)){
            return formatterClass.getConstructor().newInstance().format(field.get(record));
        }

        return field.get(record);
    }
    private void writeHeader() {
        sheet = workbook.createSheet(sheetName());
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        AtomicInteger ai = new AtomicInteger(0);

        Arrays.stream(tClass.getDeclaredFields())
                .filter(notIgnored)
                .forEach(field -> createCell(row, ai.getAndIncrement(), fieldName(field), style));

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
        } else if (valueOfCell instanceof Boolean) {
            cell.setCellValue((Boolean) valueOfCell);
        }else if (valueOfCell instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) valueOfCell);
        }else if (valueOfCell instanceof LocalDate) {
            cell.setCellValue((LocalDate) valueOfCell);
        } else {
            cell.setCellValue(String.valueOf(valueOfCell));
        }
        cell.setCellStyle(style);
    }
    private void write(){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (T record: dataList) {
            Row row = sheet.createRow(rowCount++);
            AtomicInteger ai = new AtomicInteger(0);

            Arrays.stream(tClass.getDeclaredFields())
                    .filter(notIgnored)
                    .peek(f->f.setAccessible(true))
                    .forEach(field -> {
                        try {

                            Object data = format(field,record);

                            createCell(row, ai.getAndIncrement(),data, style);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();
                        }
                    });

        }
    }
    public void write(OutputStream stream) throws IOException {
        writeHeader();
        write();
        autosize();
        workbook.write(stream);
        workbook.close();
        stream.close();
    }
}
