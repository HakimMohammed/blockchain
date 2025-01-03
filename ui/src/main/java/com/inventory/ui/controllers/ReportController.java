package com.inventory.ui.controllers;

import com.inventory.ui.enums.DemandStatus;
import com.inventory.ui.enums.OrganizationType;
import com.inventory.ui.models.CompanyDemand;
import com.inventory.ui.models.Exchange;
import com.inventory.ui.models.User;
import com.inventory.ui.services.*;
import com.inventory.ui.utils.Env;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.List;

public class ReportController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button downloadButton;

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final CompanyDemandService companyDemandService = CompanyDemandService.getInstance();
    private final AuthenticationService authenticationService = AuthenticationService.getInstance();

    public void initialize() {
        downloadButton.setGraphic(new FontIcon(Feather.DOWNLOAD));
        List<Exchange> exchanges = getMockExchanges();
        List<CompanyDemand> companyDemands = getMockCompanyDemands();

        populateBarChart(exchanges);
        if (!authenticationService.me().getOrganization().getType().equals(OrganizationType.COMPANY)) {
            populatePieChart(companyDemands);
        } else {
            pieChart.setVisible(false);
        }
    }

    private void populateBarChart(List<Exchange> exchanges) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Product Quantities");

        for (Exchange exchange : exchanges) {
            series.getData().add(new XYChart.Data<>(exchange.getOrganization(), exchange.getQuantity()));
        }

        barChart.getData().add(series);
    }

    private void populatePieChart(List<CompanyDemand> companyDemands) {
        long pendingCount = companyDemands.stream().filter(d -> d.getStatus() == DemandStatus.PENDING).count();
        long completedCount = companyDemands.stream().filter(d -> d.getStatus() == DemandStatus.ACCEPTED).count();

        pieChart.getData().addAll(
                new PieChart.Data("Pending", pendingCount),
                new PieChart.Data("Completed", completedCount)
        );
    }

    private List<Exchange> getMockExchanges() {
        return exchangeService.getExchanges(0, 10);
    }

    private List<CompanyDemand> getMockCompanyDemands() {
        return companyDemandService.findAll();
    }

    @FXML
    private void downloadPdf() {
        User user = authenticationService.me();
        String organizationName = user.getOrganization().getName();
        String timestamp = String.valueOf(System.currentTimeMillis());

        Stage stage = (Stage) downloadButton.getScene().getWindow();

        String pdfFileUrl = Env.get("API_URL") + "reports/transaction-report-" + organizationName + timestamp + ".pdf";
        PDFDownloaderService downloader = new PDFDownloaderService(pdfFileUrl, stage);

        downloader.setOnSucceeded(event -> {
            File downloadedFile = downloader.getValue();
            System.out.println("Downloaded to: " + downloadedFile.getAbsolutePath());
        });

        downloader.setOnFailed(event -> {
            Throwable exception = downloader.getException();
            System.err.println("Download failed: " + exception.getMessage());
        });

        downloader.start();
    }
}
