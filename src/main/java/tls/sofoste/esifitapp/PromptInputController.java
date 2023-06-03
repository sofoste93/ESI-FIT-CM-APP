package tls.sofoste.esifitapp;

import javafx.fxml.FXML;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class PromptInputController {
    @FXML
    public VBox mainWindowApp;
    @FXML
    protected void confirmViewClientInfo() {
        System.out.println("Kunden info angezeigt!");
    }

    @FXML
    public void onExitBtnClick() {
        Stage stage = (Stage) mainWindowApp.getScene().getWindow();
        stage.close();
    }
}
