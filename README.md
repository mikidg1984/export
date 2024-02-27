# Excel export utility
Java Apache POI Export based generator 

### Create custom data class 
````
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
````

### Customize sheet name
````
@ExcelSheet(name = "Foglio 1")
````

### Customize field column name
````
@ExcelColumn(name = "Colonna 1")
````

## Obtain a list of data, instantiate generate & write stream
````
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
````

## Compile & install
You need Java 21 SDK
````
mvn clean package
mvn install
````
