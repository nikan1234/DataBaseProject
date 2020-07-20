package com.railway.ui.window.entrance.pages.employee.speciality.driver;

import com.railway.ui.window.entrance.pages.employees.personalPage.speciality.TeamInfoController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DriverSpecController extends TeamInfoController implements Initializable {
    private DriverSpecModel model = new DriverSpecModel();

    @FXML
    private ListView<String> medicalCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        final String fontFamily = "Courier New";
        medicalCard.setCellFactory(cell -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setFont(Font.font(fontFamily, 16));
                }
            }
        });
    }

    @Override
    public void setEmployeeId(int id) {
        super.setEmployeeId(id);

        model.setEmployeeId(id);
        updateView();
    }

    protected void updateView() {
        medicalCard.setItems(FXCollections.observableList(
                model.getMedicalExaminations()
                        .stream()
                        .map(Objects::toString)
                        .collect(Collectors.toList())));
        super.updateView();
    }
}
