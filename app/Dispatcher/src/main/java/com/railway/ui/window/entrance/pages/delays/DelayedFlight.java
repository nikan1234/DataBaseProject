package com.railway.ui.window.entrance.pages.delays;

import com.railway.ui.window.common.entity.Flight;

import java.time.LocalDateTime;

public class DelayedFlight extends Flight {
    private LocalDateTime delayedUntil;

    public DelayedFlight(int number,
                         String date,
                         String delayedUntil,
                         String type,
                         double cost) {
        super(number, date, type, cost);
        this.delayedUntil = LocalDateTime.parse(delayedUntil, super.formatter);
    }

    public String getDelayedUntil() {
        return delayedUntil.format(super.formatter);
    }
}
