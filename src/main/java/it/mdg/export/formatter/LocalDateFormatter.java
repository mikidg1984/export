package it.mdg.export.formatter;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class LocalDateFormatter
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 28 February 2024 - 23:41
 * @project export
 */
public class LocalDateFormatter extends FieldFormatter<LocalDate>{
    @Override
    public String format(LocalDate var) {
        if(var == null){
            return null;
        }
        return var.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
