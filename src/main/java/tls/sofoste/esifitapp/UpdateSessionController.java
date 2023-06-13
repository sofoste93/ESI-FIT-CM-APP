package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.SessionController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateSessionController {
    @FXML
    public VBox mainWindowApp;
    @FXML
    public MenuItem aboutMenuItem;

    @FXML
    private TextField clientIdField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField startTimeField;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField endTimeField;

    @FXML
    private Label actionStatus;

    private final SessionController sessionController = new SessionController();

    public void handleUpdateSession(ActionEvent event) {
        String clientId = clientIdField.getText().trim();
        String startTimeString = startTimeField.getText().trim();
        String endTimeString = endTimeField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Data validation: to prevent wrong time format input
        if (clientId.isEmpty()) {
            actionStatus.setText("Bitte Kunden-ID eingeben!");
            return;
        } else if (startTimeString.isEmpty() || endTimeString.isEmpty()) {
            actionStatus.setText("Bitte Start- und Endzeit ausfüllen!");
            return;
        } else if (startDate == null || endDate == null) {
            actionStatus.setText("Bitte alle Felder füllen!");
            return;
        } else if (isTimeValid(startTimeString) || isTimeValid(endTimeString)) {
            actionStatus.setText("Bitte geben Sie eine gültige Start- und Endzeit ein!");
            return;
        }

        // Parsing the dates and times
        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalTime endTime = LocalTime.parse(endTimeString);

        // Constructing LocalDateTime objects
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        // Formatting LocalDateTime objects
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedStartDateTime = startDateTime.format(formatter);
        String formattedEndDateTime = endDateTime.format(formatter);

        // Updating the session
        boolean updateSuccessful = sessionController.updateSession(clientId, formattedStartDateTime, formattedEndDateTime);
        if (updateSuccessful) {
            actionStatus.setText("Neue Sitzung für Kunden mit ID: " + clientId + " gespeichert.");
            clientIdField.clear();
        } else {
            actionStatus.setText("Die Operation konnte nicht abgeschlossen werden!");
        }
    }

    private boolean isTimeValid(String time) { // using regex method
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        return !time.matches(regex);
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

