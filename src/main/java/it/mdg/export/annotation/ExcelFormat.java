package it.mdg.export.annotation;

import it.mdg.export.formatter.FieldFormatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class ExcelFormat
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 27 February 2024 - 22:57
 * @project export
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelFormat {
    Class<? extends FieldFormatter> formatter() default FieldFormatter.None.class;
}
