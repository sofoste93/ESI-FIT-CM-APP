package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;

public class ESIFITController {
    @FXML
    public VBox mainWindowApp;

    @FXML
    protected void onStartButtonClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ESIFITController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void onExitBtnClick() {
        Stage stage = (Stage) mainWindowApp.getScene().getWindow();
        stage.close();
    }
}