package com.champsoft.onlineshop.business;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.entity.Order;
import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.dataccess.repository.OrderRepository;
import com.champsoft.onlineshop.dataccess.repository.ProductRepository;
import com.champsoft.onlineshop.presentation.dto.order.OrderRequest;
import com.champsoft.onlineshop.presentation.dto.order.OrderResponse;
import com.champsoft.onlineshop.utilities.OrderNotFoundException;
import com.champsoft.onlineshop.utilities.ProductNotFoundException;
import com.champsoft.onlineshop.presentation.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orders, CustomerService customers, ProductRepository products) {
        this.orderRepository = orders;
        this.customerService = customers;
        this.productRepository = products;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse create(OrderRequest dto) {
        Customer customer = customerService.getEntityById(dto.customerId());
        List<Product> products = productRepository.findAllById(dto.productIds());

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No valid products found for this order.");
        }

        Order order = OrderMapper.toEntity(dto, customer, products);
        Order saved = orderRepository.save(order);
        return OrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getByCustomer(Long customerId) {
        customerService.getEntityById(customerId);
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> search(Long customerId, Instant minCreated, Instant maxCreated) {
        return orderRepository.searchAll(customerId, minCreated, maxCreated).stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.delete(order);
    }
}
