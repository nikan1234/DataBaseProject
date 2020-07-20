package com.railway.ui.window.common.formatters;

import com.jfoenix.controls.JFXTimePicker;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class TimeFormatter extends StringConverter<LocalTime> {
    private final String pattern = "HH:mm:ss";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    public TimeFormatter(JFXTimePicker clock) {
        clock.setPromptText(pattern.toLowerCase());
    }

    @Override
    public String toString(LocalTime date) {
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }

    @Override
    public LocalTime fromString(String string){
        {
            if (string != null && !string.isEmpty()) {
                return LocalTime.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    }
}
