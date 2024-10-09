package com.example.payments.repository;

import com.example.payments.model.Payment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    // 1. Find by status
    List<Payment> findByStatus(String status);

    // 2. Sum all amounts
    @Aggregation(pipeline = { "{ '$group': { '_id': null, 'totalAmount': { '$sum': '$totalAmount' } } } }" })
    Double sumAllAmounts();

    // 3. Find by invoice number
    Payment findByInvoiceNumber(String invoiceNumber);

    // 4. Find by payment date and status
    List<Payment> findByPaymentDateAndStatus(String paymentDate, String status);
}
