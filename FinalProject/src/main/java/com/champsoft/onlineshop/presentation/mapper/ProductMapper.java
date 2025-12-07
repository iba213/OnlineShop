package com.champsoft.onlineshop.presentation.mapper;

import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.presentation.dto.product.ProductRequest;
import com.champsoft.onlineshop.presentation.dto.product.ProductResponse;

public final class ProductMapper {

    private ProductMapper() { }

    public static Product toEntity(ProductRequest dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stock(dto.stock())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}