package com.inventory.backend.services.impl;

import com.inventory.backend.entities.Exchange;
import com.inventory.backend.entities.Product;
import com.inventory.backend.services.ExchangeService;
import com.inventory.backend.services.ProductService;
import com.inventory.backend.services.TransactionReportService;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionReportServiceImpl implements TransactionReportService {

    private final ExchangeService exchangeService;
    private final ProductService productService;

    @Override
    public void generateTransactionReport(String fileName) {
        List<Exchange> exchanges = exchangeService.findAll();

        try (PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            String logoPath = "src/main/resources/static/img/app-logo.png";
            Image logo = new Image(ImageDataFactory.create(logoPath)).scaleToFit(100, 100);
            document.add(logo);

            Paragraph title = new Paragraph("Transaction Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(title);

            document.add(new Paragraph("\n"));

            if (exchanges.isEmpty()) {
                document.add(
                        new Paragraph("No transactions found")
                                .setTextAlignment(TextAlignment.CENTER)
                                .setFontSize(16)
                                .setFontColor(ColorConstants.RED)
                                .setBold()
                );
            } else {
                Table table = new Table(7);
                table.addHeaderCell(new Cell().add(new Paragraph("Exchange ID").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));
                table.addHeaderCell(new Cell().add(new Paragraph("Product").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));
                table.addHeaderCell(new Cell().add(new Paragraph("Sender").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));
                table.addHeaderCell(new Cell().add(new Paragraph("Receiver").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));
                table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));
                table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));
                table.addHeaderCell(new Cell().add(new Paragraph("Transaction").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.BLACK));

                for (Exchange exchange : exchanges) {
                    Product product = productService.findById(exchange.product_id());
                    table.addCell(exchange.exchange_id());
                    table.addCell(product.getName());
                    table.addCell(exchange.organization());
                    table.addCell("Company");
                    table.addCell(String.valueOf(exchange.quantity()));
                    table.addCell(exchange.date());
                    table.addCell(exchange.transaction().name());
                }

                document.add(table);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate transaction report", e);
        }
    }
}
