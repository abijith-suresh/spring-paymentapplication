package com.example.payments.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
@Builder
public class Payment {
    @Id
    private String id;
    private Double totalAmount;
    private String currency;
    private String username;
    private String poNumber;
    private String invoiceNumber;
    private String targetBankAccount;
    private String sourceBankAccount;
    private int tds;
    private String status;
    private String paymentDate;
    private double tdsAmount;
    private double netAmount;

    // Vendor details
    private String vendorName;
    private String vendorAddress;
    private String vendorNumber;

    // Client details
    private String clientName;
    private String clientAddress;
    private String clientNumber;

    // List of items
    private List<Item> items;
}
