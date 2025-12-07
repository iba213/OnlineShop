package com.champsoft.onlineshop.presentation.controller;

import com.champsoft.onlineshop.business.OrderService;
import com.champsoft.onlineshop.presentation.dto.order.OrderRequest;
import com.champsoft.onlineshop.presentation.dto.order.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService service) {
        this.orderService = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAll();
        if (orders.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest dto) {
        OrderResponse saved = orderService.create(dto);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.id())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok("Order " + id + " deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Instant minCreated,
            @RequestParam(required = false) Instant maxCreated) {

        List<OrderResponse> results = orderService.search(customerId, minCreated, maxCreated);
        if (results.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(results);
    }
}
