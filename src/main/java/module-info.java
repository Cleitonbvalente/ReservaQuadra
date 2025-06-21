module com.reservaquadraoficial.reservaquadrateste {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.reservaquadraoficial.reservaquadrateste to javafx.fxml;
    exports com.reservaquadraoficial.reservaquadrateste;
}