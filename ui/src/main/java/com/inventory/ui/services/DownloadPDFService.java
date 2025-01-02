package com.inventory.ui.services;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadPDFService {

    public static void downloadPdfFromUrl(String fileUrl, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File destinationFile = fileChooser.showSaveDialog(stage);

        if (destinationFile != null) {
            try {
                URL url = new URL(fileUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                InputStream inputStream = connection.getInputStream();

                OutputStream outputStream = new FileOutputStream(destinationFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                showAlert(AlertType.INFORMATION, "Download Successful", "The PDF has been downloaded successfully!");

            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Download Failed", "An error occurred while downloading the PDF.");
            }
        } else {
            showAlert(AlertType.WARNING, "No File Chosen", "Please choose a destination file to save the PDF.");
        }
    }

    private static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
