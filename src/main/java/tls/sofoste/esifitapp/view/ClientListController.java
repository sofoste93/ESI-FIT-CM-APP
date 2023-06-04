package tls.sofoste.esifitapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tls.sofoste.esifitapp.controller.ClientController;
import tls.sofoste.esifitapp.model.Client;

import java.util.List;

public class ClientListController {
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> idColumn;
    @FXML
    private TableColumn<Client, String> firstNameColumn;
    @FXML
    private TableColumn<Client, String> lastNameColumn;

    private ClientController clientController = new ClientController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        List<Client> clients = clientController.getAllClients();
        if (!clients.isEmpty()) {
            clientsTable.getItems().addAll(clients);
        }
    }
}
