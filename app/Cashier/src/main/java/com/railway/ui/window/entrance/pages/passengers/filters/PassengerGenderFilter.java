package com.railway.ui.window.entrance.pages.passengers.filters;

import com.railway.database.Matcher;
import com.railway.database.tables.tickets.TicketMatchers;
import com.railway.ui.window.common.filters.FilterInterface;
import com.railway.ui.window.entrance.pages.passengers.Passenger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.*;

public class PassengerGenderFilter extends FilterInterface implements Initializable {

    @FXML
    private ComboBox<String> genderBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderBox.setItems(FXCollections.observableArrayList(Passenger.genders));
        genderBox.setValue(Passenger.genders[0]);
    }

    @Override
    public Collection<Matcher> getMatchers() {
        String gender = genderBox.getValue();
        return Arrays.asList(
                new TicketMatchers.MatchByPassengerGender().bind(gender));
    }
}
