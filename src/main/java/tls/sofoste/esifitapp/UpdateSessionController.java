package tls.sofoste.esifitapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.SessionController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateSessionController {

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

    private SessionController sessionController = new SessionController();

    public void handleUpdateSession(ActionEvent event) {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Please enter client ID!");
        } else {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalTime startTime = LocalTime.parse(startTimeField.getText().trim());
            LocalTime endTime = LocalTime.parse(endTimeField.getText().trim());

            if (startDate == null || endDate == null || startTime == null || endTime == null) {
                actionStatus.setText("Please select both start and end dates and times!");
            } else {
                LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                String formattedStartDateTime = startDateTime.format(formatter);
                String formattedEndDateTime = endDateTime.format(formatter);
                boolean updateSuccessful = sessionController.updateSession(clientId, formattedStartDateTime, formattedEndDateTime);
                if (updateSuccessful) {
                    actionStatus.setText("Session updated successfully for client with ID: " + clientId);
                    clientIdField.clear();
                } else {
                    actionStatus.setText("Failed to update session!");
                }
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
}

