package com.champsoft.onlineshop.presentation.mapper;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerRequest;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerResponse;

public final class CustomerMapper {

    private CustomerMapper() { }

    public static Customer toEntity(CustomerRequest dto) {
        return Customer.builder()
                .name(dto.name())
                .email(dto.email())
                .address(dto.address())
                .build();
    }

    public static CustomerResponse toResponse(Customer customer) {
        long orderCount = (customer.getOrders() == null) ? 0 : customer.getOrders().size();

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                orderCount
        );
    }
}
