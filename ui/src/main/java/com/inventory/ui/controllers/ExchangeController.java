package com.inventory.ui.controllers;

import com.inventory.ui.models.Exchange;
import com.inventory.ui.models.Product;
import com.inventory.ui.services.ExchangeService;
import com.inventory.ui.services.ProductService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ExchangeController {
    @FXML
    private TableView<Exchange> exchangeTable;
    @FXML private Pagination pagination;

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ProductService productService = ProductService.getInstance();
    private static final int ITEMS_PER_PAGE = 10;

    @FXML
    private void initialize() {
        setupTable();
        setupPagination();
    }

    private void setupTable() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TableColumn<Exchange, String> exchangeIdCol = new TableColumn<>("Exchange ID");
        TableColumn<Exchange, String> productNameCol = new TableColumn<>("Product Name");
        TableColumn<Exchange, String> organizationCol = new TableColumn<>("Organization");
        TableColumn<Exchange, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Exchange, String> dateCol = new TableColumn<>("Date");
        TableColumn<Exchange, String> transactionCol = new TableColumn<>("Transaction");
        TableColumn<Exchange, Void> actionsCol = new TableColumn<>("Actions");

        exchangeIdCol.setCellValueFactory(new PropertyValueFactory<>("exchange_id"));
        organizationCol.setCellValueFactory(new PropertyValueFactory<>("organization"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        transactionCol.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        dateCol.setCellValueFactory(cellData -> {
            Exchange exchange = cellData.getValue();
            LocalDateTime date = LocalDateTime.parse(exchange.getDate(), inputFormatter);
            return new SimpleStringProperty(date.format(outputFormatter));
        });
        productNameCol.setCellValueFactory(cellData -> {
            Exchange exchange = cellData.getValue();
            Product product = productService.getProducts().stream().filter(p -> p.getId().equals(exchange.getProduct_id())).findFirst().orElse(null);
            return new SimpleStringProperty(product != null ? product.getName() : "Unknown");
        });

        setupActionsColumn(actionsCol);

        exchangeTable.getColumns().addAll(
                exchangeIdCol, productNameCol, organizationCol, quantityCol, dateCol, transactionCol, actionsCol
        );

        refreshTable();
    }

    private void setupActionsColumn(TableColumn<Exchange, Void> actionsCol) {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    var box = new javafx.scene.layout.HBox(5);
                    box.getChildren().addAll(editButton);
                    setGraphic(box);
                }
            }
        });
    }

    private void setupPagination() {
        pagination.setPageCount((exchangeService.getTotalExchanges() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Exchange> createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        exchangeTable.setItems(exchangeService.getExchanges(fromIndex, ITEMS_PER_PAGE));
        return exchangeTable;
    }


    private void refreshTable() {
        pagination.setPageCount((exchangeService.getTotalExchanges() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
        createPage(pagination.getCurrentPageIndex());
    }
}

