package com.champsoft.onlineshop.presentation.controller;

import com.champsoft.onlineshop.business.CustomerService;
import com.champsoft.onlineshop.business.OrderService;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerRequest;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerResponse;
import com.champsoft.onlineshop.presentation.dto.order.OrderResponse;
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
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAll();
        if (customers.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse customer = customerService.getById(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest dto) {
        CustomerResponse saved = customerService.create(dto);
        return ResponseEntity.created(URI.create("/api/customers/" + saved.id())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
                                                              @Valid @RequestBody CustomerRequest dto) {
        CustomerResponse updated = customerService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok("Customer " + id + " deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(
            @RequestParam(required = false) String namePart,
            @RequestParam(required = false) String emailPart,
            @RequestParam(required = false) Instant minCreated,
            @RequestParam(required = false) Instant maxCreated) {

        List<CustomerResponse> results = customerService.search(namePart, emailPart, minCreated, maxCreated);
        if (results.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long id) {
        List<OrderResponse> orders = orderService.getByCustomer(id);
        if (orders.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(orders);
    }
}
