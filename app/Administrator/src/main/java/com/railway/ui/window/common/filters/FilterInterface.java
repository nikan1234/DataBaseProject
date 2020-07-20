package com.railway.ui.window.common.filters;

import com.railway.database.Matcher;
import com.railway.ui.base.windowManager.WindowController;

import java.util.Collection;

public abstract class FilterInterface extends WindowController {
    abstract public Collection<Matcher> getMatchers();
}
