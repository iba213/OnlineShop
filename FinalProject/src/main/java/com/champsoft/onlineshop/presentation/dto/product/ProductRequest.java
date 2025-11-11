package com.champsoft.onlineshop.presentation.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "Product name is required")
        String name,

        String description,

        @Positive(message = "Price must be greater than 0")
        double price,

        @Min(value = 0, message = "Stock cannot be negative")
        int stock
) { }

