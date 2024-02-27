import it.mdg.export.ExcelGenerator;
import it.mdg.export.annotation.ExcelColumn;
import it.mdg.export.annotation.ExcelSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
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

        List<SimpleType> dataList = List.of(
                new SimpleType().setColumn1(1L).setNotAnnotatedCol("Testo").setBoolCol(true),
                new SimpleType().setColumn1(2L).setNotAnnotatedCol("Testo2").setBoolCol(true),
                new SimpleType().setColumn1(3L).setNotAnnotatedCol("Testo3").setBoolCol(false),
                new SimpleType().setColumn1(4L).setNotAnnotatedCol("Testo4").setBoolCol(true),
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

        String notAnnotatedCol;

        @ExcelColumn(name = "Vero/Falso")
        Boolean boolCol;

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
    }

}
