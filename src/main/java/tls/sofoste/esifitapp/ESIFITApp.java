package tls.sofoste.esifitapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class ESIFITApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ESIFITApp.class.getResource("landing-view.fxml"));
        stage.setResizable(false);
        stage.setTitle("ESI-FITNESS CLIENT MANAGER     \t\t\tPowered by Enrico, Stephane & Islam TLS");
        stage.setFullScreen(true);
        Label label = new Label("Welcome to ESI-Fit Client Manager!");
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}