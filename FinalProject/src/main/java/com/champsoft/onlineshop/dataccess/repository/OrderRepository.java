package com.champsoft.onlineshop.dataccess.repository;

import com.champsoft.onlineshop.dataccess.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @Query("""
        SELECT o
        FROM Order o
        WHERE (:customerId IS NULL OR o.customer.id = :customerId)
          AND (:minCreated IS NULL OR o.createdAt >= :minCreated)
          AND (:maxCreated IS NULL OR o.createdAt <= :maxCreated)
        """)
    List<Order> searchAll(
            @Param("customerId") Long customerId,
            @Param("minCreated") Instant minCreated,
            @Param("maxCreated") Instant maxCreated
    );
}
