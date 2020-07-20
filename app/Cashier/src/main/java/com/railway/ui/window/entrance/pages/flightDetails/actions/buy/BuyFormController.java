package com.railway.ui.window.entrance.pages.flightDetails.actions.buy;

import com.railway.ui.window.entrance.pages.flightDetails.actions.common.NewTicketFormController;

import java.net.URL;
import java.util.ResourceBundle;

public class BuyFormController extends NewTicketFormController {
    private static final String sql = "{call BUY_TICKET(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setSqlCommand(sql);
    }
}
