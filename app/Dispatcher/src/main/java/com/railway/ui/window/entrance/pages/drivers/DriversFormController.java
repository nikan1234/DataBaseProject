package com.railway.ui.window.entrance.pages.drivers;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.common.entity.Driver;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.drivers.personalPage.PersonalPageController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DriversFormController extends WindowController implements Initializable {
    private DriversFormModel model = new DriversFormModel();
    private List<FilterInterface>filterInterfaces = new LinkedList<>();
    private int currentIndex = 0;

    @FXML
    private TableView<Driver> employeesTable;

    @FXML
    private Label logoLabel;

    @FXML
    private StackPane filters;

    @FXML
    private Button caseAllDrivers;

    @FXML
    private Button caseGender;

    @FXML
    private Button caseAge;

    @FXML
    private Button caseSalary;

    @FXML
    private Button caseExamination;

    @FXML
    private Button searchButton;

    @FXML
    private Label employeeCount;

    private static final int SEARCH_ALL_DRIVERS = 0;
    private static final int SEARCH_BY_GENDER = 1;
    private static final int SEARCH_BY_SALARY = 2;
    private static final int SEARCH_BY_AGE = 3;
    private static final int SEARCH_BY_EXAMINATION = 4;

    private static final String[] filterFXML = {
            "/entrance/pages/drivers/filters/allDrivers.fxml",
            "/entrance/pages/drivers/filters/genderFilter.fxml",
            "/entrance/pages/drivers/filters/salaryFilter.fxml",
            "/entrance/pages/drivers/filters/ageFilter.fxml",
            "/entrance/pages/drivers/filters/examinationYearFilter.fxml",
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setup() {
        setupFilters();
        setupSearch();
        setupTableView();

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
        caseAllDrivers.setOnMouseClicked(e ->  show(SEARCH_ALL_DRIVERS));
        caseAge.setOnMouseClicked(e ->         show(SEARCH_BY_AGE));
        caseGender.setOnMouseClicked(e ->      show(SEARCH_BY_GENDER));
        caseSalary.setOnMouseClicked(e ->      show(SEARCH_BY_SALARY));
        caseExamination.setOnMouseClicked(e -> show(SEARCH_BY_EXAMINATION));

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

    private void setupTableView() {
        final double ID_COLUMN_RATIO = 0.1;
        final double LAST_NAME_COLUMN_RATIO = 0.2;
        final double FIRST_NAME_COLUMN_RATIO = 0.2;
        final double SECOND_NAME_COLUMN_RATIO = 0.2;
        final double BIRTH_DATE_COLUMN_RATIO = 0.1;
        final double GENDER_COLUMN_RATIO = 0.1;
        final double HIRE_DATE_COLUMN_RATIO = 0.1;

        employeesTable.getColumns().add(getColumn("ID", "id", ID_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("LAST NAME", "lastName", LAST_NAME_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("FIRST NAME", "firstName", FIRST_NAME_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("SECOND NAME", "secondName", SECOND_NAME_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("BIRTH", "birthDate", BIRTH_DATE_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("GENDER", "gender", GENDER_COLUMN_RATIO));
        employeesTable.getColumns().add(getColumn("HIRE DATE", "hireDate", HIRE_DATE_COLUMN_RATIO));

        employeesTable.setRowFactory(tv -> {
            TableRow<Driver> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Driver rowData = row.getItem();

                    /* Employee personal page */
                    showDriverPersonalPage(rowData);
                }
            });
            return row;
        });
    }

    private void showDriverPersonalPage(Driver driver) {
        final String fxml = "/entrance/pages/drivers/personalPage/personalPage.fxml";

        WindowManagerInterface manager = getWindowManager();
        PersonalPageController controller = manager.loadScene(fxml, this);
        controller.setEmployeeId(driver.getId());

        manager.showScene(controller);
    }

    private TableColumn<Driver, String> getColumn(String name, String arg, double ratio) {
        TableColumn<Driver, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(arg));
        column.prefWidthProperty().bind(employeesTable.widthProperty().multiply(ratio));
        return column;
    }

    protected void updateTable(Collection<Matcher> matchers) {
        List<Driver> employees = model.getDriverList(matchers);

        employeesTable.setItems(FXCollections.observableList(employees));
        employeeCount.setText("Count: " + model.getDriversCount(matchers));
    }
}
