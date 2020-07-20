package com.railway.ui.window.entrance.pages.locomotives.actions.remove;

import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RemoveLocomotiveFormController extends WindowController implements Initializable {
    private RemoveLocomotiveFormModel model = new RemoveLocomotiveFormModel();

    @FXML
    private TextField locomotiveId;

    @FXML
    private Button removeLocomotive;

    @FXML
    private ListView<Locomotive> locomotivesList;

    @FXML
    private Label resultLabel;

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(locomotiveId);

        locomotiveId.setOnKeyTyped(u -> showLocomotivesList());

        removeLocomotive.setOnMouseClicked(e -> {
            String id = locomotiveId.getText();
            if (id.isEmpty()) {
                resultLabel.setText("Specify locomotive id");
                return;
            }
            int result = model.removeLocomotive(Integer.parseInt(id));
            resultLabel.setText("RESULT: " + Errors.toString(result));
            showLocomotivesList();
        });

        showLocomotivesList();
    }

    private void showLocomotivesList() {
        locomotivesList.getItems().setAll(model.getLocomotivesList(locomotiveId.getText()));
    }
}
