package com.example.payments.service;

import com.example.payments.dto.ItemDto;
import com.example.payments.dto.PaymentDto;
import com.example.payments.model.Item;
import com.example.payments.model.Payment;
import com.example.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment initiatePayment(PaymentDto paymentDto) {
        Payment payment = Payment.builder()
                .amount(paymentDto.getAmount())
                .currency(paymentDto.getCurrency())
                .username(paymentDto.getUsername())
                .poNumber(paymentDto.getPoNumber())
                .invoiceNumber(paymentDto.getInvoiceNumber())
                .targetBankAccount(paymentDto.getTargetBankAccount())
                .sourceBankAccount(paymentDto.getSourceBankAccount())
                .tds(paymentDto.getTds())
                .status(paymentDto.getStatus())
                .paymentDate(paymentDto.getPaymentDate())
                .vendorName(paymentDto.getVendorName())
                .vendorAddress(paymentDto.getVendorAddress())
                .vendorNumber(paymentDto.getVendorNumber())
                .clientName(paymentDto.getClientName())
                .clientAddress(paymentDto.getClientAddress())
                .clientNumber(paymentDto.getClientNumber())
                .items(convertToItems(paymentDto.getItems())) // Convert ItemDTOs to Items
                .build();

        // Save the payment instance
        return paymentRepository.save(payment);
    }

    private List<Item> convertToItems(List<ItemDto> itemDTOs) {
        // Convert the list of ItemDTOs to a list of Item objects
        return itemDTOs.stream()
                .map(itemDTO -> new Item(itemDTO.getName(), itemDTO.getQuantity(), itemDTO.getPrice()))
                .collect(Collectors.toList());
    }

    // Method to initiate a list of payments
    public List<Payment> initiatePayments(List<PaymentDto> paymentsDto) {
        List<Payment> paymentList = paymentsDto.stream().map(paymentDTO -> Payment.builder()
                .amount(paymentDTO.getAmount())
                .currency(paymentDTO.getCurrency())
                .username(paymentDTO.getUsername())
                .poNumber(paymentDTO.getPoNumber())
                .invoiceNumber(paymentDTO.getInvoiceNumber())
                .targetBankAccount(paymentDTO.getTargetBankAccount())
                .tds(paymentDTO.getTds())
                .sourceBankAccount(paymentDTO.getSourceBankAccount())
                .status(paymentDTO.getStatus())
                .paymentDate(paymentDTO.getPaymentDate())
                .vendorName(paymentDTO.getVendorName())
                .vendorAddress(paymentDTO.getVendorAddress())
                .vendorNumber(paymentDTO.getVendorNumber())
                .clientName(paymentDTO.getClientName())
                .clientAddress(paymentDTO.getClientAddress())
                .clientNumber(paymentDTO.getClientNumber())
                .items(convertToItems(paymentDTO.getItems())) // Convert ItemDTOs to Items
                .build()).collect(Collectors.toList());

        return paymentRepository.saveAll(paymentList);
    }

    // 1. Find pending payments
    public List<Payment> findPendingPayments() {
        return paymentRepository.findByStatus("PENDING");
    }

    // 2. Find total amount
    public Double getTotalAmount() {
        return paymentRepository.sumAllAmounts();
    }

    // 3. Find amount by invoice number
    public Double getAmountByInvoiceNumber(String invoiceNumber) {
        Payment payment = paymentRepository.findByInvoiceNumber(invoiceNumber);
        return payment != null ? payment.getAmount() : 0.0;
    }

    // 4. Find complete and pending payments by payment date
    public Map<String, List<Payment>> getPaymentsByStatusAndDate(String paymentDate) {
        Map<String, List<Payment>> paymentsByStatus = new HashMap<>();
        paymentsByStatus.put("completed", paymentRepository.findByPaymentDateAndStatus(paymentDate, "PAID"));
        paymentsByStatus.put("pending", paymentRepository.findByPaymentDateAndStatus(paymentDate, "PENDING"));
        return paymentsByStatus;
    }

    // 5. Edit payment
    public Payment editPayment(String id, PaymentDto paymentdto) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setAmount(paymentdto.getAmount());
            payment.setCurrency(paymentdto.getCurrency());
            payment.setUsername(paymentdto.getUsername());
            payment.setPoNumber(paymentdto.getPoNumber());
            payment.setInvoiceNumber(paymentdto.getInvoiceNumber());
            payment.setTargetBankAccount(paymentdto.getTargetBankAccount());
            payment.setSourceBankAccount(paymentdto.getSourceBankAccount());
            payment.setTds(paymentdto.getTds());
            payment.setStatus(paymentdto.getStatus());
            payment.setPaymentDate(paymentdto.getPaymentDate());
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment not found");
    }

    // 6. Delete payment
    public void deletePayment(String id) {
        paymentRepository.deleteById(id);
    }
}
