package com.railway.ui.window.entrance;

import com.railway.ui.base.windowManager.ScrollableWindowManager;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.entrance.pages.cancellations.CancelFormController;
import com.railway.ui.window.entrance.pages.delays.DelaysFormController;
import com.railway.ui.window.entrance.pages.drivers.DriversFormController;
import com.railway.ui.window.entrance.pages.locomotives.LocomotiveFormController;
import com.railway.ui.window.entrance.pages.routes.RouteFormController;
import com.railway.ui.window.entrance.pages.timetable.TimetableController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;


import java.net.URL;
import java.util.ResourceBundle;

public class EntranceFormController extends WindowController implements Initializable {

    @FXML
    private ScrollPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWindowManager(new ScrollableWindowManager(rootPane));
        onHomeButtonClicked();
    }


    @FXML
    void onHomeButtonClicked() {
        final String fxml = "/entrance/pages/timetable/timetable.fxml";
        TimetableController controller = show(fxml);
        controller.updateTimetable();
    }

    @FXML
    void onDelaysButtonClicked() {
        final String fxml = "/entrance/pages/delays/delays.fxml";
        DelaysFormController controller = show(fxml);
        controller.setup();
    }

    @FXML
    void onCancellationsButtonClicked() {
        final String fxml = "/entrance/pages/cancellations/cancellations.fxml";
        CancelFormController controller = show(fxml);
        controller.setup();
    }

    @FXML
    void onRoutesButtonClicked() {
        final String fxml = "/entrance/pages/routes/routes.fxml";
        RouteFormController controller = show(fxml);
        controller.setup();
    }

    @FXML
    void onLocomotivesButtonClicked() {
        final String fxml = "/entrance/pages/locomotives/locomotives.fxml";
        LocomotiveFormController controller = show(fxml);
        controller.setup();
    }

    @FXML
    void onDriversButtonClicked() {
        final String fxml = "/entrance/pages/drivers/drivers.fxml";
        DriversFormController controller = show(fxml);
        controller.setup();
    }


    private <C extends WindowController> C show(String fxml) {
        WindowManagerInterface manager = getWindowManager();
        C controller = manager.loadScene(fxml, this);
        manager.resetScenes(controller);
        return controller;
    }
}
