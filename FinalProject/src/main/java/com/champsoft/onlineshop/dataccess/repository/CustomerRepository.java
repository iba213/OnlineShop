package com.champsoft.onlineshop.dataccess.repository;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
        SELECT c
        FROM Customer c
        WHERE (:namePart IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :namePart, '%')))
          AND (:emailPart IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :emailPart, '%')))
          AND (:minCreated IS NULL OR c.createdAt >= :minCreated)
          AND (:maxCreated IS NULL OR c.createdAt <= :maxCreated)
        """)
    List<Customer> searchAll(
            @Param("namePart") String namePart,
            @Param("emailPart") String emailPart,
            @Param("minCreated") Instant minCreated,
            @Param("maxCreated") Instant maxCreated
    );
}
