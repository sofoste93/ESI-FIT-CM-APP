package tls.sofoste.esifitapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;

public class DeleteClientController {

    @FXML
    private TextField clientIdField;
    @FXML
    private Label actionStatus;

    private ClientController clientController = new ClientController();
    private SessionController sessionController = new SessionController();

    @FXML
    private void handleDeleteClient() {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Bitte geben Sie eine Kunden-ID ein!");
        } else {
            if (clientController.getClient(clientId) != null) {
                clientController.deleteClient(clientId);
                sessionController.deleteClientSessions(clientId);
                actionStatus.setText("Kunde mit ID: " + clientId + " wurde gelöscht");
                clientIdField.clear();
            } else {
                actionStatus.setText("Kunde mit ID: " + clientId + " existiert nicht.");
            }
        }
    }
}

