package com.champsoft.onlineshop.utilities;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer not found with ID: " + id);
    }
}
