package com.champsoft.onlineshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
        System.out.println(" Online Shop backend is running on http://localhost:8080");
        System.out.println(" H2 console available at http://localhost:8080/h2-console");
    }
}
