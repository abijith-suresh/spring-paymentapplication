package com.example.payments.service;

import com.example.payments.model.Payment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
        context.setVariable("invoiceNumber", payment.getInvoicenumber());
        context.setVariable("paymentDate", payment.getPaymentdate());
        context.setVariable("username", payment.getUsername());
        context.setVariable("ponumber", payment.getPonumber());
        context.setVariable("targetBankAccount", payment.getTargetBankAccount());
        context.setVariable("sourceBankAccount", payment.getSourceBankAccount());
        context.setVariable("tds", payment.getTds());

        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 2, 10.00));
        items.add(new Item("Item 2", 1, 15.00));
        items.add(new Item("Item 3", 3, 5.00));


        context.setVariable("items", items);

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
