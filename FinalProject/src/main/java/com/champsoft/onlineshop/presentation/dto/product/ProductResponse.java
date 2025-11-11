package com.champsoft.onlineshop.presentation.dto.product;

import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        double price,
        int stock,
        Instant createdAt,
        Instant updatedAt
) { }