package com.railway.ui.base.windowManager;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.Stack;

public class ScrollableWindowManager extends WindowManagerInterface {
    private ScrollPane scrollPane;
    private Stack<WindowController> controllers = new Stack<>();

    public ScrollableWindowManager(ScrollPane pane) {
        this.scrollPane = pane;
    }


    @Override
    public void resetScenes(WindowController controller) {
        controllers.clear();
        showScene(controller);
    }

    @Override
    public void showScene(WindowController controller) {
        show(controller);
        controllers.add(controller);
    }

    @Override
    public void prevScene() {
        if (controllers.empty()) {
            System.err.println("Scenes stack are empty!");
            return;
        }
        controllers.pop();
        show(controllers.peek());
    }

    private void show(WindowController controller) {
        Pane page = (Pane) controller.getView();
        page.minWidthProperty().bind(scrollPane.widthProperty());
        page.minHeightProperty().bind(scrollPane.heightProperty());

        scrollPane.setContent(page);
    }
}
