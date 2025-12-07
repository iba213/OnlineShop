package com.champsoft.onlineshop.presentation.controller;

import com.champsoft.onlineshop.business.ProductService;
import com.champsoft.onlineshop.presentation.dto.product.ProductRequest;
import com.champsoft.onlineshop.presentation.dto.product.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService service) {
        this.productService = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAll();
        if (products.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest dto) {
        ProductResponse saved = productService.create(dto);
        return ResponseEntity.created(URI.create("/api/products/" + saved.id())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                            @Valid @RequestBody ProductRequest dto) {
        ProductResponse updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Product " + id + " deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String namePart,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock) {

        List<ProductResponse> results = productService.search(namePart, minPrice, maxPrice, minStock, maxStock);
        if (results.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(results);
    }
}
