package com.champsoft.onlineshop.dataccess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = { @UniqueConstraint(name = "uk_customer_email", columnNames = "email") }
)
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=80)
    private String name;

    @Column(nullable=false, length=255)
    private String email;

    @Column(length=255)
    private String address;

    @CreatedDate
    @Column(nullable=false, updatable=false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable=false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
