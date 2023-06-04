package tls.sofoste.esifitapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Session;

import java.time.LocalDateTime;
import java.util.List;

public class ClientSessionsController {

    @FXML
    private TextField clientIdField;
    @FXML
    private Text actionStatus;
    @FXML
    private TableView<Session> tableView;
    @FXML
    private TableColumn<Session, LocalDateTime> loginTimeColumn;
    @FXML
    private TableColumn<Session, LocalDateTime> logoutTimeColumn;

    private SessionController sessionController = new SessionController();

    public void displayClientSessions(ActionEvent event) {
        String clientId = clientIdField.getText().trim();

        if (clientId.isEmpty()) {
            actionStatus.setText("Please enter client ID!");
        } else {
            List<Session> sessions = sessionController.getClientSessions(clientId);
            if (sessions != null && !sessions.isEmpty()) {
                ObservableList<Session> sessionObservableList = FXCollections.observableArrayList(sessions);
                tableView.setItems(sessionObservableList);
                loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
                logoutTimeColumn.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));
                actionStatus.setText("Client sessions displayed successfully.");
                clientIdField.clear();
            } else {
                actionStatus.setText("No sessions found for client with ID: " + clientId);
            }
        }
    }
}