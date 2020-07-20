package com.railway.ui.window.common.listControllers.managers;

import com.railway.ui.window.common.entity.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class ManagerListController {
    private ManagerListModel model;
    private LinkedList<Manager> managers;
    private ComboBox<String> list;

    private ObservableList<String> items;

    public ManagerListController(ComboBox<String> list) {
        this.list = list;
        this.model = new ManagerListModel();
        this.managers = model.getManagerList();
        this.managers.addFirst(null);

        items = FXCollections.observableList(
                managers.stream().map(m -> {
                    final char sep = ' ';
                    if (m == null)
                        return "None";
                    return m.getLastName() + sep +
                            m.getFirstName() + sep +
                            m.getSecondName();
                }).collect(Collectors.toList()));

        list.setItems(items);
        list.setValue(items.get(0));
    }

    public Manager getSelectedManager() {
        int idx = list.getSelectionModel().getSelectedIndex();
        return managers.get(idx);
    }

    public void setItem(int idx) {
        list.setValue(items.get(idx));
    }

    public void setItem(Manager manager) {
        int idx = managers.indexOf(manager);
        if (idx < 0)
            return;
        list.setValue(items.get(idx));
    }
}
