package com.champsoft.onlineshop.business;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.entity.Order;
import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.dataccess.repository.OrderRepository;
import com.champsoft.onlineshop.dataccess.repository.ProductRepository;
import com.champsoft.onlineshop.utilities.CustomerNotFoundException;
import com.champsoft.onlineshop.utilities.OrderNotFoundException;
import com.champsoft.onlineshop.utilities.ProductNotFoundException;
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
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public Order create(Long customerId, List<Long> productIds) {
        Customer customer = customerService.getEntityById(customerId);
        List<Product> products = productRepository.findAllById(productIds);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No valid products found for this order.");
        }

        Order order = Order.builder()
                .customer(customer)
                .products(products)
                .build();

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getByCustomer(Long customerId) {
        // Check customer existence
        customerService.getEntityById(customerId);
        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Order> search(Long customerId, Instant minCreated, Instant maxCreated) {
        return orderRepository.searchAll(customerId, minCreated, maxCreated);
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.delete(order);
    }
}
