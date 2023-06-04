package tls.sofoste.esifitapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tls.sofoste.esifitapp.controller.SessionController;
import tls.sofoste.esifitapp.model.Client;
import tls.sofoste.esifitapp.model.Session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSessionsController {

    @FXML
    public AnchorPane mainWindowApp;
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
    @FXML
    private TableColumn <Client, String> clientIdTableColumn;

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
                clientIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
                loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
                logoutTimeColumn.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));
                actionStatus.setText("Client sessions displayed successfully.");
                clientIdField.clear();
            } else {
                actionStatus.setText("No sessions found for client with ID: " + clientId);
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
    @FXML
    public void onExitBtnClick() {
        Stage stage = (Stage) mainWindowApp.getScene().getWindow();
        stage.close();
    }
}