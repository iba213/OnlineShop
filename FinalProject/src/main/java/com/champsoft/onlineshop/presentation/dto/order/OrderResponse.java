package com.champsoft.onlineshop.presentation.dto.order;

import com.champsoft.onlineshop.presentation.dto.customer.CustomerResponse;
import com.champsoft.onlineshop.presentation.dto.product.ProductResponse;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        CustomerResponse customer,
        List<ProductResponse> products,
        Instant createdAt,
        Instant updatedAt
) { }
