package com.railway.ui.window.entrance.pages.employees.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeMatchers;
import com.railway.ui.window.common.fieldContollers.NumberFieldController;
import com.railway.ui.window.common.filters.FilterInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ChildrenFilter extends FilterInterface implements Initializable {
    @FXML
    private CheckBox childrenCheckBox;

    @FXML
    private TextField childrenCountField;

    @FXML
    private Label childLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new NumberFieldController(childrenCountField);

        childrenCheckBox.setOnAction(e -> {
            childrenCountField.setText("");

            boolean visible = childrenCheckBox.isSelected();
            childrenCountField.setVisible(visible);
            childLabel.setVisible(visible);
        });
        childrenCountField.setVisible(false);
        childLabel.setVisible(false);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        final int childFree = 0;

        List<Matcher> matchers = new LinkedList<>();

        boolean hasChildren = childrenCheckBox.isSelected();

        /* Has children */
        if (hasChildren) {
            String text = childrenCountField.getText();

            /* Any count of children */
            if (text.isEmpty()) {
                matchers.add(new EmployeeMatchers
                        .MatchByChildrenCount()
                        .bind(childFree)
                        .comparator(Matcher.GREATER));
            }
            /* Fixed count */
            else {
                int childCount = Integer.parseInt(text);
                matchers.add(new EmployeeMatchers
                        .MatchByChildrenCount()
                        .bind(childCount)
                        .comparator(Matcher.EQUAL));
            }
        }
        /* Has't children */
        else {
            matchers.add(new EmployeeMatchers
                    .MatchByChildrenCount()
                    .bind(childFree)
                    .comparator(Matcher.EQUAL));
        }
        return matchers;
    }
}
