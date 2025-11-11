package com.champsoft.onlineshop.business;

import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.dataccess.repository.ProductRepository;
import com.champsoft.onlineshop.utilities.DuplicateResourceException;
import com.champsoft.onlineshop.utilities.ProductNotFoundException;
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
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product create(Product product) {
        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new DuplicateResourceException("Product name already exists: " + product.getName());
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        boolean nameChanged = !existing.getName().equalsIgnoreCase(updatedProduct.getName());
        if (nameChanged && productRepository.existsByNameIgnoreCase(updatedProduct.getName())) {
            throw new DuplicateResourceException("Product name already exists: " + updatedProduct.getName());
        }

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());

        return productRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<Product> search(String namePart, Double minPrice, Double maxPrice, Integer minStock, Integer maxStock) {
        String nameNorm = normalize(namePart);
        return productRepository.searchAll(nameNorm, minPrice, maxPrice, minStock, maxStock);
    }

    private static String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
