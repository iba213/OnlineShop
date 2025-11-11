package com.champsoft.onlineshop.presentation.controller;

import com.champsoft.onlineshop.business.CustomerService;
import com.champsoft.onlineshop.business.OrderService;
import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.entity.Order;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    public CustomerController(CustomerService customers, OrderService orders) {
        this.customerService = customers;
        this.orderService = orders;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAll();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer saved = customerService.create(customer);
        return ResponseEntity.created(URI.create("/api/customers/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                   @Valid @RequestBody Customer updatedCustomer) {
        Customer updated = customerService.update(id, updatedCustomer);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok("Customer " + id + " deleted successfully.");
    }

    // Search customers by filters
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(
            @RequestParam(required = false) String namePart,
            @RequestParam(required = false) String emailPart,
            @RequestParam(required = false) Instant minCreated,
            @RequestParam(required = false) Instant maxCreated) {

        List<Customer> results = customerService.search(namePart, emailPart, minCreated, maxCreated);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    // Nested endpoint: get all orders by customer
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long id) {
        List<Order> orders = orderService.getByCustomer(id);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }
}
