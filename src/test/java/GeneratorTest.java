import it.mdg.export.ExcelGenerator;
import it.mdg.export.annotation.ExcelColumn;
import it.mdg.export.annotation.ExcelFormat;
import it.mdg.export.annotation.ExcelIgnore;
import it.mdg.export.annotation.ExcelSheet;
import it.mdg.export.formatter.LocalDateFormatter;
import it.mdg.export.formatter.LocalDateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class GeneratorTest
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 27 February 2024 - 23:35
 * @project export
 */
public class GeneratorTest {

    @Test
    public void generate() throws IOException, IllegalAccessException {

        LocalDate date = LocalDate.now();
        LocalDateTime datetime = LocalDateTime.now();

        List<SimpleType> dataList = List.of(
                new SimpleType().setColumn1(1L).setNotAnnotatedCol("Testo").setBoolCol(true).setData(date).setDatetime(datetime),
                new SimpleType().setColumn1(2L).setNotAnnotatedCol("Testo2").setBoolCol(true),
                new SimpleType().setColumn1(3L).setNotAnnotatedCol("Testo3").setBoolCol(false),
                new SimpleType().setColumn1(4L).setNotAnnotatedCol("Testo4").setBoolCol(true).setData(date).setDatetime(datetime),
                new SimpleType().setColumn1(5L).setNotAnnotatedCol("Testo5").setBoolCol(false)
        );

        ExcelGenerator<SimpleType> generator = new ExcelGenerator<>(dataList,SimpleType.class);

        OutputStream outputStream = new FileOutputStream("test.xlsx");

        generator.write(outputStream);

        Assertions.assertNotNull(outputStream);

    }


    @ExcelSheet(name = "Foglio 1")
    public static class SimpleType{

        @ExcelColumn(name = "Colonna 1")
        Long column1;

        @ExcelIgnore
        String notAnnotatedCol;

        @ExcelColumn(name = "Vero/Falso")
        Boolean boolCol;

        @ExcelFormat(formatter = LocalDateFormatter.class)
        LocalDate data;

        @ExcelFormat(formatter = LocalDateTimeFormatter.class)
        LocalDateTime datetime;

        public SimpleType() {
        }

        public Long getColumn1() {
            return column1;
        }

        public SimpleType setColumn1(Long column1) {
            this.column1 = column1;
            return this;
        }

        public String getNotAnnotatedCol() {
            return notAnnotatedCol;
        }

        public SimpleType setNotAnnotatedCol(String notAnnotatedCol) {
            this.notAnnotatedCol = notAnnotatedCol;
            return this;
        }

        public Boolean getBoolCol() {
            return boolCol;
        }

        public SimpleType setBoolCol(Boolean boolCol) {
            this.boolCol = boolCol;
            return this;
        }

        public LocalDate getData() {
            return data;
        }

        public SimpleType setData(LocalDate data) {
            this.data = data;
            return this;
        }

        public LocalDateTime getDatetime() {
            return datetime;
        }

        public SimpleType setDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }
    }

}
