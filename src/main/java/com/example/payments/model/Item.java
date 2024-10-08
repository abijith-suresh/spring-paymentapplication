package com.example.payments.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String name;
    private int quantity;
    private double price;

    public double getTotal() {
        return quantity * price;
    }
}
