package com.example.payments.controller;

import com.example.payments.dto.PaymentDto;
import com.example.payments.model.Payment;
import com.example.payments.repository.PaymentRepository;
import com.example.payments.service.PaymentService;
import com.example.payments.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/initiate")
    public ResponseEntity<Payment> initiatePayment(@RequestBody @Valid PaymentDto payment) {
        return new ResponseEntity<>(paymentService.initiatePayment(payment), HttpStatus.OK);
    }
    @PostMapping("/bulk-initiate")
    public ResponseEntity<List<Payment>> initiatePayments(@RequestBody @Valid List<PaymentDto> payments) {
        return new ResponseEntity<>(paymentService.initiatePayments(payments), HttpStatus.OK);
    }
    // 1. Endpoint to find pending payments
    @GetMapping("/pending")
    public ResponseEntity<List<Payment>> findPendingPayments() {
        return new ResponseEntity<>(paymentService.findPendingPayments(), HttpStatus.OK);
    }

    // 2. Endpoint to find total amount
    @GetMapping("/total-amount")
    public ResponseEntity<Double> getTotalAmount() {
        return new ResponseEntity<>(paymentService.getTotalAmount(), HttpStatus.OK);
    }

    // 3. Endpoint to find amount by invoice number
    @GetMapping("/amount/{invoiceNumber}")
    public ResponseEntity<Double> getAmountByInvoiceNumber(@PathVariable String invoiceNumber) {
        return new ResponseEntity<>(paymentService.getAmountByInvoiceNumber(invoiceNumber), HttpStatus.OK);
    }

    // 4. Endpoint to find complete and pending payments by date
    @GetMapping("/status-by-date/{paymentDate}")
    public ResponseEntity<Map<String, List<Payment>>> getPaymentsByStatusAndDate(@PathVariable String paymentDate) {
        return new ResponseEntity<>(paymentService.getPaymentsByStatusAndDate(paymentDate), HttpStatus.OK);
    }

    // 5. Endpoint to edit payment
    @PutMapping("/edit/{id}")
    public ResponseEntity<Payment> editPayment(@PathVariable String id, @RequestBody PaymentDto paymentdto) {
        return new ResponseEntity<>(paymentService.editPayment(id, paymentdto), HttpStatus.OK);
    }

    // 6. Endpoint to delete payment
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable String invoiceNumber) {
        // Fetch payment details using the invoice number
        Payment payment = paymentRepository.findByInvoiceNumber(invoiceNumber);
        if (payment == null) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }

        try {
            // Generate PDF
            byte[] pdfBytes = pdfService.generateInvoicePdf(payment);

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + invoiceNumber + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
