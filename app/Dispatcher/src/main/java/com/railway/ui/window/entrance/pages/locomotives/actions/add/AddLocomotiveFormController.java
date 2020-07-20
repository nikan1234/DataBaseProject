package com.railway.ui.window.entrance.pages.locomotives.actions.add;

import com.jfoenix.controls.JFXDatePicker;
import com.railway.database.tables.Errors;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.entity.Team;
import com.railway.ui.window.common.fieldContollers.IntegerFieldController;
import com.railway.ui.window.common.formatters.DateFormatter;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddLocomotiveFormController extends WindowController implements Initializable {
    private TeamListController teamListController;
    private AddLocomotivesFormModel model = new AddLocomotivesFormModel();

    @FXML
    private TextField locomotiveId;

    @FXML
    private TextField locomotiveName;

    @FXML
    private ComboBox<String> teamBox;

    @FXML
    private JFXDatePicker entryDate;

    @FXML
    private Button addLocomotive;

    @FXML
    private Label resultLabel;

    @FXML
    private ListView<Employee> employeesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new IntegerFieldController(locomotiveId);

        teamListController = new TeamListController(teamBox);

        teamBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            Team team = teamListController.getSelectedTeam();
            if (team == null) {
                return;
            }
            employeesList.getItems().setAll(model.getByTeam(team.getTeamId()));
        });
        entryDate.setValue(LocalDate.now());
        entryDate.setConverter(new DateFormatter(entryDate));

        addLocomotive.setOnMouseClicked(e -> {
            resultLabel.setText("");

            String id = locomotiveId.getText();
            String name = locomotiveName.getText();
            Team team = teamListController.getSelectedTeam();
            if (id.isEmpty() || name.isEmpty() || team == null) {
                resultLabel.setText("Specify all fields");
                return;
            }
            int result = model.addLocomotive(new Locomotive(
                    Integer.parseInt(id),
                    name,
                    entryDate.getValue(),
                    team
            ));
            resultLabel.setText("RESULT: " + Errors.toString(result));
        });
    }

    @FXML
    void onPrevButtonClicked() {
        getWindowManager().prevScene();
    }
}
