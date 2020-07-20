package com.railway.ui.window.entrance.pages.routes.actions.remove;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RemoveRouteFormController extends WindowController implements Initializable {
    private RemoveRouteFormModel model = new RemoveRouteFormModel();

    @FXML
    private TextField routeId;

    @FXML
    private Button removeRoute;

    @FXML
    private Label finalLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(routeId);

        removeRoute.setOnMouseClicked(e -> {
            String text = routeId.getText();
            if (text.isEmpty()) {
                finalLabel.setText("Specify route id");
                return;
            }
            int result = model.removeRoute(Integer.parseInt(text));
            finalLabel.setText("RESULT: " + Errors.toString(result));
        });
    }


    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }
}
