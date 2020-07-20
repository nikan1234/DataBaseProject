package com.railway.ui.window.auth;

import com.railway.ClientApplication;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsFormController extends WindowController implements Initializable {

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    void onAcceptButtonClicked() {
        SettingsStorage.getInstance().setDatabaseAddress(ipField.getText());
        SettingsStorage.getInstance().setDatabasePort(Integer.parseInt(portField.getText()));
        SettingsStorage.getInstance().saveSettings();

        ClientApplication.getWindowManager().prevScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(portField);

        SettingsStorage storage = SettingsStorage.getInstance();
        ipField.setText(storage.getDatabaseAddress());
        portField.setText(Integer.toString(storage.getDatabasePort()));
    }
}
