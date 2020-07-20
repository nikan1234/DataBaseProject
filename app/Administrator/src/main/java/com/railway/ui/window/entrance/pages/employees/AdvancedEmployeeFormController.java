package com.railway.ui.window.entrance.pages.employees;


import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AdvancedEmployeeFormController extends EmployeeFormCommon implements Initializable {
    private AdvancedEmployeeFormModel model = new AdvancedEmployeeFormModel();

    private List<Node> nodes;

    private static final int SEARCH_BY_AVERAGE_SALARY = 0;
    private static final int SEARCH_BY_SUMMARY_SALARY = 1;
    private static final int SEARCH_BY_MIN_SALARY = 2;
    private static final int SEARCH_BY_MAX_SALARY = 3;

    @FXML
    private StackPane filters;

    @FXML
    private Button caseAverageSalary;

    @FXML
    private Button caseSummarySalary;

    @FXML
    private Button caseMaxSalary;

    @FXML
    private Button caseMinSalary;

    @FXML
    private TextField maxAverageSalary;

    @FXML
    private TextField minAverageSalary;

    @FXML
    private TextField maxSummarySalary;

    @FXML
    private TextField minSummarySalary;

    @FXML
    private Button searchButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        nodes = new LinkedList<>(filters.getChildren());

        new NumberFieldController(minAverageSalary);
        new NumberFieldController(maxAverageSalary);
    }

    @FXML
    void onDefaultFiltersSelected() {
        getWindowManager().prevScene();
    }

    public void setup() {
        setupSearch();
        hideAll();
    }


    private void setupSearch() {
        caseAverageSalary.setOnMouseClicked(e -> {
            searchButton.setOnMouseClicked(u -> {
                String min = minAverageSalary.getText();
                String max = maxAverageSalary.getText();
                if (min.isEmpty() || max.isEmpty()) {
                    return;
                }
                int minSalary = Integer.parseInt(min);
                int maxSalary = Integer.parseInt(max);

                List<Employee> employees = model.getAverageSalary(minSalary, maxSalary);
                updateTable(employees, employees.size());
            });
            show(SEARCH_BY_AVERAGE_SALARY);
        });

        caseSummarySalary.setOnMouseClicked(e -> {
            searchButton.setOnMouseClicked(u -> {
                String min = minSummarySalary.getText();
                String max = maxSummarySalary.getText();
                if (min.isEmpty() || max.isEmpty()) {
                    return;
                }
                int minSalary = Integer.parseInt(min);
                int maxSalary = Integer.parseInt(max);

                List<Employee> employees = model.getSummarySalary(minSalary, maxSalary);
                updateTable(employees, employees.size());
            });
            show(SEARCH_BY_SUMMARY_SALARY);
        });

        caseMinSalary.setOnMouseClicked(e -> {
            searchButton.setOnMouseClicked(u -> {
                List<Employee> employees = model.getMinSalary();
                updateTable(employees, employees.size());
            });
            show(SEARCH_BY_MIN_SALARY);
        });

        caseMaxSalary.setOnMouseClicked(e -> {
            searchButton.setOnMouseClicked(u -> {
                List<Employee> employees = model.getMaxSalary();
                updateTable(employees, employees.size());
            });
            show(SEARCH_BY_MAX_SALARY);
        });
    }

    private void hideAll() {
        searchButton.setVisible(false);
        for (Node child : nodes) {
            child.setVisible(false);
        }
    }

    private void show(int idx) {

        hideAll();
        Node node = nodes.get(idx);
        node.toFront();
        node.setVisible(true);

        searchButton.setVisible(true);
    }
}
