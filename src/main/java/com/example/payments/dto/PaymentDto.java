package com.example.payments.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Username is required")
    private String username;

    @Pattern(regexp = "^[A-Z0-9]+$", message = "PO number should contain only uppercase letters and digits")
    private String poNumber;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "Invoice number must be alphanumeric and between 6 to 10 characters.")
    private String invoiceNumber;

    @NotBlank(message = "Target bank account is required")
    private String targetBankAccount;

    @NotBlank(message = "Source bank account is required")
    private String sourceBankAccount;

    @Min(value = 5, message = "TDS should be greater than or equal to 5")
    private int tds;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PAID|PENDING)$", message = "Status should be either 'PAID' or 'PENDING'")
    private String status;

    @NotBlank(message = "Payment date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Payment date should be in the format YYYY-MM-DD")
    private String paymentDate;

    @NotBlank(message = "Vendor name cannot be null or empty")
    private String vendorName;

    @NotBlank(message = "Vendor address cannot be null or empty")
    private String vendorAddress;

    @NotBlank(message = "Vendor number cannot be null or empty")
    private String vendorNumber;

    // Client details
    @NotBlank(message = "Client name cannot be null or empty")
    private String clientName;

    @NotBlank(message = "Client address cannot be null or empty")
    private String clientAddress;

    @NotBlank(message = "Client number cannot be null or empty")
    private String clientNumber;

    // List of items
    @NotNull(message = "Item list cannot be null")
    private List<ItemDto> items;
}
