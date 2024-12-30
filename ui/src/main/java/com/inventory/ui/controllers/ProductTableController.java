package com.inventory.ui.controllers;

import com.inventory.ui.enums.OrganizationType;
import com.inventory.ui.models.Organization;
import com.inventory.ui.models.Product;
import com.inventory.ui.services.AuthenticationService;
import com.inventory.ui.services.ExchangeService;
import com.inventory.ui.services.ProductService;
import com.inventory.ui.utils.DialogUtils;
import javafx.beans.property.SimpleStringProperty;
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

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ProductService productService = ProductService.getInstance();
    private final AuthenticationService authenticationService = AuthenticationService.getInstance();
    private final Organization userOrganization = authenticationService.me().getOrganization();
    private static final int ITEMS_PER_PAGE = 10;

    @FXML
    private void initialize() {
        setupTable();
        setupPagination();
    }

    private void setupTable() {
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        TableColumn<Product, Void> actionsCol = new TableColumn<>("Actions");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice() + " dh"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        productTable.getColumns().addAll(nameCol, descCol, priceCol, quantityCol, categoryCol);

        if (OrganizationType.SUPPLIER.equals(userOrganization.getType())) {
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
                    if (OrganizationType.SUPPLIER.equals(userOrganization.getType())) {
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
                quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        quantityField.setText(oldValue);
                    }
                });

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(10));
                gridPane.add(productLabel, 0, 0);
                gridPane.add(productField, 1, 0);
                gridPane.add(quantityLabel, 0, 1);
                gridPane.add(quantityField, 1, 1);

                dialog.getDialogPane().setContent(gridPane);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        String inputValue = quantityField.getText();
                        if (!inputValue.isEmpty()) {
                            int quantity = Integer.parseInt(inputValue);
                            try {
                                exchangeService.tradeProducts(userOrganization.getName(), product.getId().toString(), quantity);
                                refreshTable();
                            } catch (Exception e) {
                                DialogUtils.showError("Error sending product", e.getMessage());
                            }
                        } else {
                            DialogUtils.showError("Error sending product", "Please enter a valid quantity");
                        }
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

    private void refreshTable() {
        pagination.setPageCount((productService.getTotalProducts() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
        createPage(pagination.getCurrentPageIndex());
    }
}