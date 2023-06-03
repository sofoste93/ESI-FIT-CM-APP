package tls.sofoste.esifitapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ErrorView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ESIFITApp.class.getResource("error-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 420);
        stage.setResizable(false);
        stage.setTitle("ESI-FITNESS CLIENT MANAGER [ - - - ] ERROR VIEW");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
