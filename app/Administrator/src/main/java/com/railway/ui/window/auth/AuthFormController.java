package com.railway.ui.window.auth;

import com.railway.ClientApplication;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import com.railway.ui.window.entrance.EntranceFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthFormController extends WindowController implements Initializable {
    private static final int ACCESS_LEVEL = 4;
    AuthService service = new AuthService(ACCESS_LEVEL);

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new NumberFieldController(loginField);

        loginButton.setOnMouseClicked(u -> {
            final String fxml = "/entrance/entrance.fxml";

            String text = loginField.getText();
            if (text.isEmpty())
                return;

            int login = Integer.parseInt(text);
            String password = passwordField.getText();

            try {
                if (!service.login(login, password)) {
                    errorLabel.setText("Wrong ID or password");
                    return;
                }
            }
            catch (final Exception e) {
                errorLabel.setText("Couldn't connect to server");
                return;
            }

            WindowManager manager = ClientApplication.getWindowManager();
            EntranceFormController controller = manager.loadScene(fxml);
            manager.showScene(controller);
            errorLabel.setText("");
        });
    }

    @FXML
    void onSettingsButtonClicked() {
        String fxml = "/auth/settings.fxml";
        WindowManager manager = ClientApplication.getWindowManager();
        SettingsFormController controller = manager.loadScene(fxml);
        manager.showScene(controller);
        errorLabel.setText("");
    }
}
