package com.railway.ui.window.common.buttonTableCell;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Consumer;


public class ButtonCellGenerator<T> {
    private String buttonName;

    public ButtonCellGenerator(String buttonName) {
        this.buttonName = buttonName;
    }


    public Callback<TableColumn<T, Void>, TableCell<T, Void>> getButtonCallback(Consumer<T> consumer) {
        return new Callback<>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                return new TableCell<>() {

                    private final Button button = new Button(buttonName);
                    {
                        button.setStyle("-fx-background-color: transparent; " +
                                        "-fx-text-fill: black");
                        button.setOnAction((ActionEvent event) -> {
                            T value = getTableView().getItems().get(getIndex());
                            consumer.accept(value);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };
    }
}
