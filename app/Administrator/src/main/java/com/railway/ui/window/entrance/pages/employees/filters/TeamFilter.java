package com.railway.ui.window.entrance.pages.employees.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.teams.TeamMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.common.listControllers.teams.TeamListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class TeamFilter extends FilterInterface implements Initializable {
    private TeamListController controller;

    @FXML
    private ComboBox<String> teamId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new TeamListController(teamId);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        int teamId = controller.getSelectedTeamId();
        if (teamId != TeamListController.NO_TEAM) {
            matchers.add(new TeamMatchers.MatchByTeamId().bind(teamId));
        }
        return matchers;
    }
}
