package it.mdg.export.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class ExcelSheet
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 27 February 2024 - 23:03
 * @project export
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelSheet {
    String name() default "";
}
