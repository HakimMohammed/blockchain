package com.inventory.ui.controllers;

import com.inventory.ui.models.Product;
import com.inventory.ui.services.AuthenticationService;
import com.inventory.ui.services.ProductService;
import com.inventory.ui.utils.DialogUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class ProductTableController {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private Pagination pagination;

    private final ProductService productService = ProductService.getInstance();
    private final AuthenticationService authenticationService = AuthenticationService.getInstance();
    private final String userCompany = authenticationService.me().getOrganization().getName();
    private static final int ITEMS_PER_PAGE = 10;


    @FXML
    private void initialize() {
        setupTable();
        setupPagination();
    }

    private void setupTable() {
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        TableColumn<Product, Void> actionsCol = new TableColumn<>("Actions");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        productTable.getColumns().addAll(nameCol, descCol, priceCol, quantityCol, categoryCol);

        if ("supplier1".equals(userCompany)) {
            setupActionsColumn(actionsCol);
            productTable.getColumns().add(actionsCol);
        }

        refreshTable();
    }

    private void setupActionsColumn(TableColumn<Product, Void> actionsCol) {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button sendButton = new Button("Send");

            {
                sendButton.setOnAction(event -> {
                    Product selectedProduct = getTableView().getItems().get(getIndex());
                    showPopup(selectedProduct);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if ("supplier1".equals(userCompany)) {
                        setGraphic(sendButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }

            private void showPopup(Product product) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Send Product");

                Label productLabel = new Label("Product:");
                TextField productField = new TextField(product.getName());
                productField.setEditable(false);

                Label quantityLabel = new Label("Quantity:");
                TextField quantityField = new TextField();

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(10)); // Add padding
                gridPane.add(productLabel, 0, 0);
                gridPane.add(productField, 1, 0);
                gridPane.add(quantityLabel, 0, 1);
                gridPane.add(quantityField, 1, 1);

                dialog.getDialogPane().setContent(gridPane);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        String quantity = quantityField.getText();
                        // Handle the send action with the selected product and quantity
                    }
                });
            }
        });
    }

    private void setupPagination() {
        pagination.setPageCount((productService.getTotalProducts() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Product> createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        productTable.setItems(productService.getProducts());
        return productTable;
    }


    private boolean confirmDelete(Product product) {
        return DialogUtils.showConfirmation(
                "Confirm Delete",
                "Are you sure you want to delete " + product.getName() + "?"
        );
    }

    private void refreshTable() {
        pagination.setPageCount((productService.getTotalProducts() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
        createPage(pagination.getCurrentPageIndex());
    }
}