package it.mdg.export.formatter;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class LocalDateTimeFormatter
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 28 February 2024 - 23:41
 * @project export
 */
public class LocalDateTimeFormatter extends FieldFormatter<LocalDateTime> {
    @Override
    public String format(LocalDateTime var) {
        if(var == null){
            return null;
        }
        return var.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
