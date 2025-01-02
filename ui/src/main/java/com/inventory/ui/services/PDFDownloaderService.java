package com.inventory.ui.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.concurrent.FutureTask;

public class PDFDownloaderService extends Service<File> {
    private final String pdfUrl;
    private final Stage stage;

    public PDFDownloaderService(String pdfUrl, Stage stage) {
        this.pdfUrl = pdfUrl;
        this.stage = stage;
    }

    @Override
    protected Task<File> createTask() {
        return new Task<>() {
            @Override
            protected File call() throws Exception {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save PDF File");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );
                String defaultFileName = pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
                fileChooser.setInitialFileName(defaultFileName);
                FutureTask<File> futureTask = new FutureTask<>(() -> fileChooser.showSaveDialog(stage));
                javafx.application.Platform.runLater(futureTask);
                File outputFile = futureTask.get();
                if (outputFile == null) {
                    throw new IOException("No file selected");
                }

                String token = TokenStorageService.loadToken();
                HttpURLConnection connection = (HttpURLConnection) new URL(pdfUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                long fileSize = connection.getContentLengthLong();

                try (InputStream in = connection.getInputStream();
                     BufferedInputStream bis = new BufferedInputStream(in)) {

                    byte[] buffer = new byte[8192];
                    long totalBytesRead = 0;
                    int bytesRead;

                    try (BufferedOutputStream bos = new BufferedOutputStream(
                            Files.newOutputStream(outputFile.toPath()))) {
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            updateProgress(totalBytesRead, fileSize);
                        }
                    }

                    return outputFile;
                }
            }
        };
    }
}