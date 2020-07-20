package com.railway.ui.window.entrance.pages.flightDetails.actions.refund;

import com.railway.ui.window.entrance.pages.flightDetails.actions.common.ExistingTicketFormController;

import java.net.URL;
import java.util.ResourceBundle;

public class RefundFormController extends ExistingTicketFormController {
    private final String sql = "{call RETURN_TICKET(?, ?)}";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setSqlCommand(sql);
    }
}
