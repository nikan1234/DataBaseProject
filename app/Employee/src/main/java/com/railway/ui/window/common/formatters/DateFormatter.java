package com.railway.ui.window.common.formatters;

import com.jfoenix.controls.JFXDatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatter extends StringConverter<LocalDate> {
    private final String pattern = "dd-MM-yyyy";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    public DateFormatter(JFXDatePicker calendar) {
        calendar.setPromptText(pattern.toLowerCase());
    }

    @Override
    public String toString(LocalDate date) {
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }

    @Override
    public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
    }
}
