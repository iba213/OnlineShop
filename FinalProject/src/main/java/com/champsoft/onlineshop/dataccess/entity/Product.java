package com.champsoft.onlineshop.dataccess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.champsoft.onlineshop.dataccess.entity.Order;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_name", columnNames = "name")
        },
        indexes = {
                @Index(name = "idx_product_price", columnList = "price"),
                @Index(name = "idx_product_stock", columnList = "stock")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String name;

    @Column(length=255)
    private String description;

    @Column(nullable=false)
    private double price;

    @Column(nullable=false)
    private int stock;

    @CreatedDate
    @Column(nullable=false, updatable=false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable=false)
    private Instant updatedAt;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();
}