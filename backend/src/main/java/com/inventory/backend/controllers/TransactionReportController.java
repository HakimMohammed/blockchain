package com.inventory.backend.controllers;

import com.inventory.backend.services.TransactionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

@RequestMapping("/api/v1/reports")
@RestController
@RequiredArgsConstructor
public class TransactionReportController {
    private final TransactionReportService transactionReportService;

    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> generateTransactionReport(@PathVariable String fileName) {
        File directory = new File("reports");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = "reports/" + fileName;
        transactionReportService.generateTransactionReport(filePath);

        try {
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(file.length())
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send the file", e);
        }
    }
}
