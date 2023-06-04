package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Session;

public class LogoutController {

    @FXML
    private TextField clientIdField;
    @FXML
    private Label actionStatus;

    private ClientController clientController = new ClientController();
    private SessionController sessionController = new SessionController();

    public void logoutClient(ActionEvent event) {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Please enter client ID!");
        } else {
            if (clientController.getClient(clientId) != null) {
                Session endedSession = sessionController.endSession(clientId);
                if (endedSession != null) {
                    actionStatus.setText("Session ended for client with ID: " + clientId);
                    clientIdField.clear();
                } else {
                    actionStatus.setText("Failed to end session or no session found for client!");
                }
            } else {
                actionStatus.setText("Client with ID: " + clientId + " does not exist. Please register first.");
            }
        }
    }
}