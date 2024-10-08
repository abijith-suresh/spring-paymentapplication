package com.example.payments.service;

import com.example.payments.model.Item;
import com.example.payments.model.Payment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    private final SpringTemplateEngine templateEngine;

    public PdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateInvoicePdf(Payment payment) throws Exception {
        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariable("invoiceNumber", payment.getInvoiceNumber());
        context.setVariable("paymentDate", payment.getPaymentDate());
        context.setVariable("poNumber", payment.getPoNumber());
        context.setVariable("targetBankAccount", payment.getTargetBankAccount());
        context.setVariable("sourceBankAccount", payment.getSourceBankAccount());
        context.setVariable("clientName", payment.getClientName());
        context.setVariable("clientAddress", payment.getClientAddress());
        context.setVariable("clientNumber", payment.getClientNumber());
        context.setVariable("vendorName", payment.getVendorName());
        context.setVariable("vendorAddress", payment.getVendorAddress());
        context.setVariable("vendorNumber", payment.getVendorNumber());
        context.setVariable("netAmount", payment.getNetAmount());
        context.setVariable("tdsAmount", payment.getTdsAmount());
        context.setVariable("totalAmount", payment.getTotalAmount());

        context.setVariable("items", payment.getItems());

        // Render the HTML
        String htmlContent = templateEngine.process("invoiceTemplate", context);

        // Convert HTML to PDF
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        }
    }
}
