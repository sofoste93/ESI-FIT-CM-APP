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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Client;
import tls.sofoste.esifitapp.model.Session;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientInfoController {

    @FXML
    public VBox mainWindowApp;
    @FXML
    public MenuItem aboutMenuItem;
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

    private final ClientController clientController = new ClientController();

    public void displayClientInformation() {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Bitte Kunden-ID eingeben!");
            return;
        }

        Client client = clientController.getClient(clientId);
        if (client != null) {
            clientIdText.setText(client.getId());
            clientFirstNameText.setText(client.getFirstName());
            clientLastNameText.setText(client.getLastName());

            List<Session> sessions = sessionController.getClientSessions(clientId);

            if (sessions != null && !sessions.isEmpty()) {
                Session lastSession = sessions.get(sessions.size() - 1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                String loginTime = (lastSession.getLoginTime() != null) ? lastSession.getLoginTime().format(formatter) : "N/A";
                String logoutTime = (lastSession.getLogoutTime() != null) ? lastSession.getLogoutTime().format(formatter) : "N/A";

                clientSessionStart.setText(loginTime);
                clientSessionEnd.setText(logoutTime);
                actionStatus.setText("Kunden-Info angezeigt.");
            } else {
                actionStatus.setText("Keine Sitzungen für den Kunden mit der ID: " + clientId + " gefunden.");
            }

            clientIdField.clear();
        } else {
            actionStatus.setText("Keinen Kunden mit ID: " + clientId + " gefunden.");
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

    @FXML
    public void handleAboutMenuItem() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI("https://github.com/sofoste93/ESI-FIT-CM-APP");
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void updateClientSession(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("update-session-view.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(ESIFITController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
