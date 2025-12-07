package com.champsoft.onlineshop.business;

import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.dataccess.repository.ProductRepository;
import com.champsoft.onlineshop.presentation.dto.product.ProductRequest;
import com.champsoft.onlineshop.presentation.dto.product.ProductResponse;
import com.champsoft.onlineshop.utilities.DuplicateResourceException;
import com.champsoft.onlineshop.utilities.ProductNotFoundException;
import com.champsoft.onlineshop.presentation.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repository) {
        this.productRepository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest dto) {
        if (productRepository.existsByNameIgnoreCase(dto.name())) {
            throw new DuplicateResourceException("Product name already exists: " + dto.name());
        }

        Product entity = ProductMapper.toEntity(dto);
        Product saved = productRepository.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        boolean nameChanged = !existing.getName().equalsIgnoreCase(dto.name());
        if (nameChanged && productRepository.existsByNameIgnoreCase(dto.name())) {
            throw new DuplicateResourceException("Product name already exists: " + dto.name());
        }

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setStock(dto.stock());

        Product updated = productRepository.save(existing);
        return ProductMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> search(String namePart, Double minPrice, Double maxPrice,
                                           Integer minStock, Integer maxStock) {
        return productRepository.searchAll(namePart, minPrice, maxPrice, minStock, maxStock)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}
