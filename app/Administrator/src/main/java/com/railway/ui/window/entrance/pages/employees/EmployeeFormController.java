package com.railway.ui.window.entrance.pages.employees;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Employee;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.employees.dismissPage.DismissFormController;
import com.railway.ui.window.entrance.pages.employees.hirePage.HireFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeFormController extends EmployeeFormCommon implements Initializable {
    private List<FilterInterface> filterInterfaces = new LinkedList<>();
    private EmployeeFormModel model = new EmployeeFormModel();
    private int currentIndex = 0;

    @FXML
    private StackPane filters;

    @FXML
    private Button caseDepartment;

    @FXML
    private Button caseTeam;

    @FXML
    private Button caseAge;

    @FXML
    private Button caseGender;

    @FXML
    private Button caseChildCount;

    @FXML
    private Button caseSalary;

    @FXML
    private Button caseExperience;

    @FXML
    private Button searchButton;

    private static final int SEARCH_BY_DEPARTMENT = 0;
    private static final int SEARCH_BY_TEAM       = 1;
    private static final int SEARCH_BY_AGE        = 2;
    private static final int SEARCH_BY_GENDER     = 3;
    private static final int SEARCH_BY_CHILDREN   = 4;
    private static final int SEARCH_BY_SALARY     = 5;
    private static final int SEARCH_BY_EXPERIENCE = 6;

    private static final String filterFXML[] = {
            "/entrance/pages/employees/filters/departmentFilter.fxml",
            "/entrance/pages/employees/filters/teamFilter.fxml",
            "/entrance/pages/employees/filters/ageFilter.fxml",
            "/entrance/pages/employees/filters/genderFilter.fxml",
            "/entrance/pages/employees/filters/childFilter.fxml",
            "/entrance/pages/employees/filters/salaryFilter.fxml",
            "/entrance/pages/employees/filters/experienceFilter.fxml"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    @FXML
    void onAdvancedFiltersSelected() {
        final String fxml = "/entrance/pages/employees/employeesAdvanced.fxml";

        AdvancedEmployeeFormController controller = getWindowManager().loadScene(fxml, this);
        controller.setup();
        getWindowManager().showScene(controller);
    }

    public void setup() {
        setupFilters();
        setupSearch();

        hideAll();
    }

    private void setupFilters() {
        WindowManagerInterface manager = getWindowManager();

        for (String fxml : filterFXML) {
            filterInterfaces.add(manager.loadScene(fxml));
        }
        filters.getChildren().addAll(filterInterfaces
                .stream()
                .map(e -> {
                    AnchorPane node = (AnchorPane) e.getView();
                    node.prefWidthProperty().bind(filters.widthProperty());
                    node.prefHeightProperty().bind(filters.heightProperty());
                    return e.getView();
                })
                .collect(Collectors.toList()));
    }

    private void setupSearch() {
        caseDepartment.setOnMouseClicked(e -> show(SEARCH_BY_DEPARTMENT));
        caseTeam.setOnMouseClicked(e ->       show(SEARCH_BY_TEAM));
        caseAge.setOnMouseClicked(e ->        show(SEARCH_BY_AGE));
        caseGender.setOnMouseClicked(e ->     show(SEARCH_BY_GENDER));
        caseChildCount.setOnMouseClicked(e -> show(SEARCH_BY_CHILDREN));
        caseSalary.setOnMouseClicked(e ->     show(SEARCH_BY_SALARY));
        caseExperience.setOnMouseClicked(e -> show(SEARCH_BY_EXPERIENCE));

        searchButton.setOnMouseClicked(e -> {
            int idx = currentIndex;
            updateTable(filterInterfaces.get(idx).getMatchers());
        });
    }

    private void hideAll() {
        searchButton.setVisible(false);
        for (FilterInterface child : filterInterfaces) {
            child.getView().setVisible(false);
        }
    }

    private void show(int idx) {
        currentIndex = idx;

        hideAll();
        Node node = filterInterfaces.get(idx).getView();
        node.toFront();
        node.setVisible(true);

        searchButton.setVisible(true);
    }

    private void updateTable(Collection<Matcher> matchers) {
        List<Employee> employees = model.getEmployeeList(matchers);
        int count = model.getEmployeeCount(matchers);
        updateTable(employees, count);
    }
}
