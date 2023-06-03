package tls.sofoste.esifitapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.ESIFITController;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    @FXML
    public VBox mainWindowApp;
    @FXML
    public Label displayMenuText;

    public void onListBtn(ActionEvent actionEvent) {

    }
    @FXML
    public void switchToLoginWindow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ESIFITController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    @FXML
    public void switchToRegisterWindow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ESIFITController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
