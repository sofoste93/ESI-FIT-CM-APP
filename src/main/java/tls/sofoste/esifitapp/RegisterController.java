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
import tls.sofoste.esifitapp.model.Client;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    @FXML
    public MenuItem aboutMenuItem;
    @FXML
    public VBox mainWindowApp;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private Label actionStatus;
    @FXML
    private Label generatedId;

    private ClientController clientController = new ClientController();

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
    public void registerClient() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            actionStatus.setText("Bitte alle Felder ausfüllen!");
        } else {
            Client newClient = clientController.registerClient(firstName, lastName);
            if (newClient != null) {
                actionStatus.setText("Kunden registriert mit ID: " + newClient.getId());
                firstNameField.clear();
                lastNameField.clear();
                generatedId.setText(newClient.getId());
            } else {
                actionStatus.setText("Registrierung konnte nicht ausgeführt werden!");
            }
        }
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

    @FXML
    public void onExitBtnClick() {
        Stage stage = (Stage) mainWindowApp.getScene().getWindow();
        stage.close();
    }
}