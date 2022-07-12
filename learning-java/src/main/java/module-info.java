module com.example.learningjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.learningjava to javafx.fxml;
    exports com.example.learningjava;
}