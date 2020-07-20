package com.railway.ui.base.windowManager;

import com.railway.ui.base.BaseController;

public class WindowController extends BaseController {
    WindowManagerInterface manager = null;
    private WindowController parent;

    public void setWindowManager(WindowManagerInterface manager) {
        this.manager = manager;
    }

    public WindowManagerInterface getWindowManager() {
        return manager;
    }

    public WindowController getParent() {
        return parent;
    }

    public void setParent(WindowController parent) {
        this.parent = parent;
    }
}
