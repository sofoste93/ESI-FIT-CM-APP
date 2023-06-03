package tls.sofoste.esifitapp.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.ESIFITApp;

import java.io.IOException;

public class UpdateClientInfoView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ESIFITApp.class.getResource("updateClientInfo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 620);
        stage.setResizable(false);
        stage.setTitle("ESI-FITNESS CLIENT MANAGER [- - -] UPDATE DATA");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
