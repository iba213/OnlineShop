package com.champsoft.onlineshop.bootstrap;

import com.champsoft.onlineshop.dataccess.entity.Customer;
import com.champsoft.onlineshop.dataccess.entity.Order;
import com.champsoft.onlineshop.dataccess.entity.Product;
import com.champsoft.onlineshop.dataccess.repository.CustomerRepository;
import com.champsoft.onlineshop.dataccess.repository.OrderRepository;
import com.champsoft.onlineshop.dataccess.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public DataSeeder(ProductRepository productRepository,
                      CustomerRepository customerRepository,
                      OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        if (productRepository.count() > 0 || customerRepository.count() > 0 || orderRepository.count() > 0) {
            System.out.println("🟡 Database already seeded — skipping DataSeeder.");
            return;
        }

        System.out.println(" Seeding database with tech shop data...");


        List<Product> products = List.of(
                Product.builder().name("iPhone 15 Pro").description("Apple flagship smartphone").price(1399.99).stock(20).build(),
                Product.builder().name("MacBook Air M3").description("Apple laptop with M3 chip").price(1799.99).stock(10).build(),
                Product.builder().name("Samsung Galaxy S24").description("Samsung premium smartphone").price(1299.99).stock(18).build(),
                Product.builder().name("ASUS ROG Zephyrus").description("Gaming laptop 16GB RAM, RTX 4070").price(2199.99).stock(7).build(),
                Product.builder().name("iPad Pro 13\" M4").description("Apple tablet with M4 chip").price(1649.99).stock(12).build(),
                Product.builder().name("Sony WH-1000XM5").description("Noise cancelling headphones").price(499.99).stock(25).build(),
                Product.builder().name("Google Pixel 9").description("Latest Google smartphone").price(1199.99).stock(15).build(),
                Product.builder().name("Dell XPS 15").description("High-end ultrabook").price(1899.99).stock(9).build(),
                Product.builder().name("Apple Watch Series 10").description("Smartwatch with new sensors").price(599.99).stock(30).build(),
                Product.builder().name("AirPods Pro 2").description("Wireless noise-cancelling earbuds").price(349.99).stock(35).build()
        );
        productRepository.saveAll(products);


        List<Customer> customers = List.of(
                Customer.builder().name("Loukmane Bessam").email("loukmane.bessam@onlineshop.com").address("123 Rue de la Tech, Montreal").build(),
                Customer.builder().name("Elias Ibaliden").email("elias.ibaliden@onlineshop.com").address("22 Avenue du Code, Laval").build(),
                Customer.builder().name("Yanis Achamou").email("yanis.achamou@onlineshop.com").address("47 Boulevard du Web, Longueuil").build(),
                Customer.builder().name("Yacoub Bakayoko").email("yacoub.bakayoko@onlineshop.com").address("66 Rue des Données, Brossard").build(),
                Customer.builder().name("Daryl Gangoué").email("daryl.gangoue@onlineshop.com").address("9 Place des API, Quebec City").build()
        );
        customerRepository.saveAll(customers);


        List<Order> orders = new ArrayList<>();

        orders.add(Order.builder()
                .customer(customers.get(0)) // Loukmane
                .products(List.of(products.get(0), products.get(9), products.get(5)))
                .build());

        orders.add(Order.builder()
                .customer(customers.get(1)) // Elias
                .products(List.of(products.get(1), products.get(3)))
                .build());

        orders.add(Order.builder()
                .customer(customers.get(2)) // Yanis
                .products(List.of(products.get(4), products.get(8), products.get(2)))
                .build());

        orders.add(Order.builder()
                .customer(customers.get(3)) // Yacoub
                .products(List.of(products.get(6)))
                .build());

        orders.add(Order.builder()
                .customer(customers.get(4)) // Daryl
                .products(List.of(products.get(7), products.get(1), products.get(5)))
                .build());

        orderRepository.saveAll(orders);

        System.out.println(" Database seeded successfully with Loukmane, Elias, Yanis, Yacoub, and Daryl!");
    }
}
