package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Client;
import tls.sofoste.esifitapp.model.Session;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientInfoController {

    @FXML
    public VBox mainWindowApp;
    @FXML
    private TextField clientIdField;
    @FXML
    private Text actionStatus;
    @FXML
    private Label clientIdText;
    @FXML
    private Label clientFirstNameText;
    @FXML
    private Label clientLastNameText;
    @FXML
    private Label clientSessionStart;
    @FXML
    private Label clientSessionEnd;
    private static final SessionController sessionController = new SessionController();

    private ClientController clientController = new ClientController();

    public void displayClientInformation(ActionEvent event) {
        String clientId = clientIdField.getText().trim();
        List<Session> sessions = sessionController.getClientSessions(clientId);

        if (clientId.isEmpty()) {
            actionStatus.setText("Please enter client ID!");
        } else {
            Client client = clientController.getClient(clientId);
            if (client != null) {
                clientIdText.setText(client.getId());
                clientFirstNameText.setText(client.getFirstName());
                clientLastNameText.setText(client.getLastName());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                for (Session session : sessions) {
                    String loginTime = (session.getLoginTime() != null) ? session.getLoginTime().format(formatter) : "N/A";
                    String logoutTime = (session.getLogoutTime() != null) ? session.getLogoutTime().format(formatter) : "N/A";
                    clientSessionStart.setText(loginTime);
                    clientSessionEnd.setText(logoutTime);
                }
                actionStatus.setText("Client information displayed successfully.");
                clientIdField.clear();
            } else {
                actionStatus.setText("Client with ID: " + clientId + " does not exist. Please register first.");
            }
        }
    }

    @FXML
    public void switchToMainWindow(ActionEvent event) {
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
