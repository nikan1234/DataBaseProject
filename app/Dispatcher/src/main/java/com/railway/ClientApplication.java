package com.railway;

import com.railway.ui.base.windowManager.WindowManager;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientApplication extends Application {
    private static WindowManager manager;

    public static WindowManager getWindowManager() {
        return manager;
    }


    @Override
    public void start(Stage primaryStage) {
        final String authForm = "/auth/auth.fxml";

        final double SCREEN_RATIO = 0.75;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        int DEFAULT_WIDTH = (int)(screenBounds.getWidth() * SCREEN_RATIO);
        int DEFAULT_HEIGHT = (int)(screenBounds.getHeight() * SCREEN_RATIO);

        primaryStage.setMaximized(true);
        manager = new WindowManager(primaryStage, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        manager.showScene(manager.loadScene(authForm));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
