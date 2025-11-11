package com.champsoft.onlineshop.business;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.repository.CustomerRepository;
import com.champsoft.onlineshop.utilities.CustomerNotFoundException;
import com.champsoft.onlineshop.utilities.DuplicateResourceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository repository) {
        this.customerRepository = repository;
    }

    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional
    public Customer create(Customer customer) {
        if (customerRepository.existsByEmailIgnoreCase(customer.getEmail())) {
            throw new DuplicateResourceException("Customer email already exists: " + customer.getEmail());
        }
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer update(Long id, Customer updatedCustomer) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        boolean emailChanged = !existing.getEmail().equalsIgnoreCase(updatedCustomer.getEmail());
        if (emailChanged && customerRepository.existsByEmailIgnoreCase(updatedCustomer.getEmail())) {
            throw new DuplicateResourceException("Customer email already exists: " + updatedCustomer.getEmail());
        }

        existing.setName(updatedCustomer.getName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setAddress(updatedCustomer.getAddress());

        return customerRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (existing.getOrders() != null && !existing.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete customer with existing orders.");
        }

        customerRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<Customer> search(String namePart, String emailPart, Instant minCreated, Instant maxCreated) {
        String nameNorm = normalize(namePart);
        String emailNorm = normalize(emailPart);
        return customerRepository.searchAll(nameNorm, emailNorm, minCreated, maxCreated);
    }

    @Transactional(readOnly = true)
    public Customer getEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private static String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
