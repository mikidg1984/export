package it.mdg.export.formatter;


/**
 * Class FieldFormatter
 *
 * @author Michele Del Giudice <michele.delgiudice@doriansrl.it>
 * @created 28 February 2024 - 23:35
 * @project export
 */
public abstract class FieldFormatter<T> {

    public abstract String format(T var);

    public abstract static class None extends FieldFormatter<Object> {

        public static String CANONICAL_NAME = "it.mdg.export.formatter.FieldFormatter.None";
        public None() {
        }

    }
}
