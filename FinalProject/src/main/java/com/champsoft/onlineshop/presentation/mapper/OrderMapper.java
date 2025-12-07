package com.champsoft.onlineshop.presentation.mapper;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.entity.Order;
import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerResponse;
import com.champsoft.onlineshop.presentation.dto.order.OrderRequest;
import com.champsoft.onlineshop.presentation.dto.order.OrderResponse;
import com.champsoft.onlineshop.presentation.dto.product.ProductResponse;

import java.util.List;

public final class OrderMapper {

    private OrderMapper() { }

    public static Order toEntity(OrderRequest dto, Customer customer, List<Product> products) {
        return Order.builder()
                .customer(customer)
                .products(products)
                .build();
    }

    public static OrderResponse toResponse(Order order) {
        Customer c = order.getCustomer();
        List<Product> products = order.getProducts();

        CustomerResponse customerDTO = (c == null) ? null : CustomerMapper.toResponse(c);
        List<ProductResponse> productDTOs = (products == null)
                ? List.of()
                : products.stream().map(ProductMapper::toResponse).toList();

        return new OrderResponse(
                order.getId(),
                customerDTO,
                productDTOs,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
