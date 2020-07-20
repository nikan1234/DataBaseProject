package com.railway.ui.base.windowManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public abstract class WindowManagerInterface {


    public <C extends WindowController> C loadScene(String url) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
            Node root = fxmlLoader.load();

            C controller = fxmlLoader.getController();
            controller.setView(root);

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <C extends WindowController> C loadScene(String url, WindowController parent) {
        C controller = loadScene(url);
        if (controller == null)
            return null;

        if (parent == null)
            return controller;

        controller.setParent(parent);
        controller.setWindowManager(parent.getWindowManager());
        return controller;
    }

    abstract public void resetScenes(WindowController controller);

    abstract public void showScene(WindowController controller);

    abstract public void prevScene();
}
