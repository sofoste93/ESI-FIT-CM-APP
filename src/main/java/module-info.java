module tls.sofoste.esifitapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens tls.sofoste.esifitapp to javafx.fxml;
    exports tls.sofoste.esifitapp;
}