package tls.sofoste.esifitapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ESIFITController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onStartButtonClick() {
        welcomeText.setText("\tWelcome to ESI \nFITNESS CLIENT MANAGER");
    }
}