package com.railway.ui.window.common.listControllers.teams;

import com.railway.ui.window.common.entity.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.stream.Collectors;


public class TeamListController {
    private ComboBox<String> box;
    private List<Team> teams;
    private TeamListModel model = new TeamListModel();


    public static final int NO_TEAM = -1;

    public TeamListController(ComboBox<String> box) {
        this.box = box;
        this.teams = model.getTeamList();

        ObservableList<String> items = FXCollections.observableList(teams
                .stream().map(t -> {
                    final char sep = ' ';
                    return Integer.toString(t.getTeamId()) +
                            sep + t.getTeamType();
                }).collect(Collectors.toList()));
        items.add(0, "Any team");

        box.setItems(items);
        box.setValue(items.get(0));
    }

    public int getSelectedTeamId() {
        int idx = box.getSelectionModel().getSelectedIndex();
        if (idx <= 0 ) {
            return NO_TEAM;
        }
        return teams.get(idx - 1).getTeamId();
    }

    public void setTeam(int id) {
        final char sep = ' ';

        for (Team team : teams) {
            if (id == team.getTeamId()) {
                box.setValue(Integer.toString(team.getTeamId()) +
                        sep + team.getTeamType());
            }
        }
    }
}
