package com.champsoft.onlineshop.utilities;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found with ID: " + id);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
