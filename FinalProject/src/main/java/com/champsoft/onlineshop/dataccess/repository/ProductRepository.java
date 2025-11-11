package com.champsoft.onlineshop.dataccess.repository;

import com.champsoft.onlineshop.dataccess.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByNameIgnoreCase(String name);


    @Query("""
        SELECT p
        FROM Product p
        WHERE (:namePart IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :namePart, '%')))
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:minStock IS NULL OR p.stock >= :minStock)
          AND (:maxStock IS NULL OR p.stock <= :maxStock)
        """)
    List<Product> searchAll(
            @Param("namePart") String namePart,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minStock") Integer minStock,
            @Param("maxStock") Integer maxStock
    );
}
