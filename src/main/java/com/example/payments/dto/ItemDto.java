package com.example.payments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    @NotBlank(message = "Item name cannot be blank")
    private String name;

    @NotBlank(message = "Item name cannot be blank")
    private int quantity;

    @NotBlank(message = "Item name cannot be blank")
    private double price;
}
