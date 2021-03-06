package com.railway.ui.window.entrance.pages.flightDetails.actions.purchase;

import com.railway.ui.window.entrance.pages.flightDetails.actions.common.ExistingTicketFormController;

import java.net.URL;
import java.util.ResourceBundle;

public class PurchaseFormController extends ExistingTicketFormController {
    private static String sql = "{call PURCHASE_TICKET(?, ?)}";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        setSqlCommand(sql);
    }
}
