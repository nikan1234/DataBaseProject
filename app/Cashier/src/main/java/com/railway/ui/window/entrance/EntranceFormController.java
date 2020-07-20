package com.railway.ui.window.entrance;

import com.railway.ui.base.windowManager.ScrollableWindowManager;
import com.railway.ui.base.windowManager.WindowController;
import com.railway.ui.base.windowManager.WindowManager;
import com.railway.ui.base.windowManager.WindowManagerInterface;
import com.railway.ui.window.entrance.pages.passengers.PassengersFormController;
import com.railway.ui.window.entrance.pages.tickets.TicketsFormController;
import com.railway.ui.window.entrance.pages.timetable.TimetableController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class EntranceFormController extends WindowController implements Initializable {

    @FXML
    private ScrollPane rootPane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWindowManager(new ScrollableWindowManager(rootPane));
        showTimetable();
    }

    @FXML
    void onHomeButtonClicked() {
        showTimetable();
    }

    @FXML
    void onPassengersButtonClicked(MouseEvent event) {
        final String fxmlFile = "/entrance/pages/passengers/passengers.fxml";
        PassengersFormController controller = show(fxmlFile);
        controller.setup();
    }

    @FXML
    void onTicketsButtonClicked(MouseEvent event) {
        final String fxmlFile = "/entrance/pages/tickets/tickets.fxml";
        TicketsFormController controller = show(fxmlFile);
        controller.setup();
    }


    private void showTimetable() {
        final String fxmlFile = "/entrance/pages/timetable/timetable.fxml";
        TimetableController controller = show(fxmlFile);
        controller.updateTimetable();
    }

    private <C extends WindowController> C show(String fxml) {
        WindowManagerInterface manager = getWindowManager();
        C controller = manager.loadScene(fxml, this);
        manager.resetScenes(controller);
        return controller;
    }
}
