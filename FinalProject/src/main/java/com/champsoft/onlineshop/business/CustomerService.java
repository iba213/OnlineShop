package com.champsoft.onlineshop.business;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.repository.CustomerRepository;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerRequest;
import com.champsoft.onlineshop.presentation.dto.customer.CustomerResponse;
import com.champsoft.onlineshop.utilities.CustomerNotFoundException;
import com.champsoft.onlineshop.utilities.DuplicateResourceException;
import com.champsoft.onlineshop.presentation.mapper.CustomerMapper;
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
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return CustomerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse create(CustomerRequest dto) {
        if (customerRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new DuplicateResourceException("Email already exists: " + dto.email());
        }

        Customer entity = CustomerMapper.toEntity(dto);
        Customer saved = customerRepository.save(entity);
        return CustomerMapper.toResponse(saved);
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        boolean emailChanged = !existing.getEmail().equalsIgnoreCase(dto.email());
        if (emailChanged && customerRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new DuplicateResourceException("Email already exists: " + dto.email());
        }

        existing.setName(dto.name());
        existing.setEmail(dto.email());
        existing.setAddress(dto.address());

        Customer updated = customerRepository.save(existing);
        return CustomerMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (existing.getOrders() != null && !existing.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete customer with active orders.");
        }

        customerRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> search(String namePart, String emailPart,
                                            Instant minCreated, Instant maxCreated) {
        return customerRepository.searchAll(namePart, emailPart, minCreated, maxCreated)
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Customer getEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
