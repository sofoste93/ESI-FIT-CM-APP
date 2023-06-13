package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogoutController {

    @FXML
    private TextField clientIdField;
    @FXML
    private Label actionStatus;

    private final ClientController clientController = new ClientController();
    private final SessionController sessionController = new SessionController();

    public void logoutClient(ActionEvent event) {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Bitte Kunden-ID eingeben!");
        } else {
            if (clientController.getClient(clientId) != null) {
                Session endedSession = sessionController.endSession(clientId);
                if (endedSession != null) {
                    actionStatus.setText("Session beendet für Kunden mit ID: " + clientId);
                    clientIdField.clear();
                } else {
                    actionStatus.setText("Sitzung konnte nicht beendet werden oder Kunden nicht gefunden!");
                }
            } else {
                actionStatus.setText("Kunden mit ID: " + clientId + " existiert nicht.");
            }
        }
    }
    public void showClientList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("clientList-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ESIFITController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}