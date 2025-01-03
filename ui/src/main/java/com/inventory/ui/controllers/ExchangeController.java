package com.inventory.ui.controllers;

import com.inventory.ui.models.Exchange;
import com.inventory.ui.models.Product;
import com.inventory.ui.services.ExchangeService;
import com.inventory.ui.services.ProductService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExchangeController {
    @FXML
    private TableView<Exchange> exchangeTable;
    @FXML
    private Pagination pagination;

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ProductService productService = ProductService.getInstance();
    private static final int ITEMS_PER_PAGE = 10;

    @FXML
    private void initialize() {
        setupTable();
        setupPagination();
    }

    private void setupTable() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TableColumn<Exchange, String> productNameCol = new TableColumn<>("Product Name");
        TableColumn<Exchange, String> organizationCol = new TableColumn<>("Organization");
        TableColumn<Exchange, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Exchange, String> dateCol = new TableColumn<>("Date");
        TableColumn<Exchange, String> transactionCol = new TableColumn<>("Transaction");

        organizationCol.setCellValueFactory(new PropertyValueFactory<>("organization"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        transactionCol.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        productNameCol.setCellValueFactory(cellData -> {
            Exchange exchange = cellData.getValue();
            Product product = productService.getProducts().stream().filter(p -> p.getId().equals(exchange.getProduct_id())).findFirst().orElse(null);
            return new SimpleStringProperty(product != null ? product.getName() : "Unknown");
        });

        exchangeTable.getColumns().addAll(
                productNameCol, organizationCol, quantityCol, dateCol, transactionCol
        );

        refreshTable();
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