package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Session;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Desktop;

public class LoginController {
    @FXML
    public MenuItem aboutMenuItem;
    @FXML
    private TextField clientIdField;
    @FXML
    private Label actionStatus;
    @FXML
    public VBox mainWindowApp;

    private ClientController clientController = new ClientController();
    private SessionController sessionController = new SessionController();

    public void loginClient() {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Bitte Kunden-Id eingeben!".toUpperCase());
        } else {
            if (clientController.getClient(clientId) != null) {
                Session newSession = sessionController.startSession(clientId);
                if (newSession != null) {
                    actionStatus.setText(("Sitzung für den Kunden mit ID: " + clientId + " gestartet!").toUpperCase());
                    clientIdField.clear();
                } else {
                    actionStatus.setText(("Sitzung konnte nicht gestartet werden!".toUpperCase()).toUpperCase());
                }
            } else {
                actionStatus.setText(("Kunden mit ID: " + clientId + " nicht gefunden. " +
                        "Bitte registrieren oder ID prüfen.").toUpperCase());
            }
        }
    }
    public void showClientList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("clientList-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ESIFITController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    @FXML
    public void switchToMainWindow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
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
            stage.setFullScreen(true);
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

    @FXML
    public void handleAboutMenuItem(ActionEvent actionEvent) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    URI uri = new URI("https://github.com/sofoste93/ESI-FIT-CM-APP");
                    Desktop.getDesktop().browse(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}