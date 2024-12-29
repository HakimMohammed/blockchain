package com.inventory.ui.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.function.Function;

public class TableUtils {
    public static <T> void setupColumn(TableColumn<T, String> column, String property) {
        column.setCellValueFactory(cellData -> {
            try {
                Object value = cellData.getValue().getClass()
                        .getMethod("get" + capitalize(property))
                        .invoke(cellData.getValue());
                return new SimpleStringProperty(value.toString());
            } catch (Exception e) {
                return new SimpleStringProperty("");
            }
        });
    }

    public static <T> void setupColumn(
            TableColumn<T, String> column,
            Function<T, String> valueExtractor) {
        column.setCellValueFactory(cellData ->
                new SimpleStringProperty(valueExtractor.apply(cellData.getValue()))
        );
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
