package tls.sofoste.esifitapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Session;

public class LoginController {
    @FXML
    private TextField clientIdField;

    @FXML
    private Text actionStatus;

    private ClientController clientController = new ClientController();
    private SessionController sessionController = new SessionController();

    public void loginClient() {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Please enter client ID!");
        } else {
            if (clientController.getClient(clientId) != null) {
                Session newSession = sessionController.startSession(clientId);
                if (newSession != null) {
                    actionStatus.setText("Session started for client with ID: " + clientId);
                    clientIdField.clear();
                } else {
                    actionStatus.setText("Failed to start session!");
                }
            } else {
                actionStatus.setText("Client with ID: " + clientId + " does not exist. Please register first.");
            }
        }
    }
}