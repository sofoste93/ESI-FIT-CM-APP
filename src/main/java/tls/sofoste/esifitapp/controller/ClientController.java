package tls.sofoste.esifitapp.controller;

import tls.sofoste.esifitapp.model.Client;
import tls.sofoste.esifitapp.service.ClientService;

import java.util.List;

public class ClientController {
    private final ClientService clientService = new ClientService();

    public Client registerClient(String firstName, String lastName) {
        return clientService.registerClient(firstName, lastName);
    }

    public Client getClient(String id) {
        return clientService.getClient(id);
    }

    public void deleteClient(String id) {
        clientService.deleteClient(id);
    }

    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }
}

