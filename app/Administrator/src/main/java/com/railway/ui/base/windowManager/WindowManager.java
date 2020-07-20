package com.railway.ui.base.windowManager;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Stack;

public class WindowManager extends WindowManagerInterface {
    private Stack<WindowController> controllers;
    private Pane pageKeeper;


    public WindowManager(Stage primaryStage, int width, int height) {
        pageKeeper = new Pane();
        controllers = new Stack<>();

        Scene scene = new Scene(pageKeeper, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public WindowManager(AnchorPane root) {
        pageKeeper = root;
        controllers = new Stack<>();
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
        page.prefWidthProperty().bind(pageKeeper.widthProperty());
        page.prefHeightProperty().bind(pageKeeper.heightProperty());

        pageKeeper.getChildren().clear();
        pageKeeper.getChildren().add(page);
    }
}
