package com.champsoft.onlineshop.presentation.dto.customer;

import java.time.Instant;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String address,
        Instant createdAt,
        Instant updatedAt,
        long orderCount
) { }