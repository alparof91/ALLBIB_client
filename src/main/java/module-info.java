module com.allbib {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.allbib to javafx.fxml;
    opens com.allbib.entity to com.google.gson;
    exports com.allbib;
}