package com.akfc.training.misc;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Exemple simple d'utilisation de Lombok avec Java 21
 * Sans dépendances externes pour faciliter les tests
 */
public class LombokSimpleExample {

    // 1. Classe simple avec @Data
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {
        private String name;
        private int age;
        private String email;
    }

    // 2. Classe avec @Builder
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private String name;
        private double price;
        private String category;
        private boolean available;
        
        @Builder.Default
        private LocalDateTime createdAt = LocalDateTime.now();
    }

    // 3. Classe immutable avec @Value
    @Value
    @Builder
    public static class Address {
        String street;
        String city;
        String zipCode;
        String country;
    }

    // 4. Classe avec @RequiredArgsConstructor et @NonNull
    @Data
    @RequiredArgsConstructor
    public static class Customer {
        @NonNull
        private String name;
        
        @NonNull
        private String email;
        
        private String phone;
        
        @Setter(AccessLevel.PRIVATE)
        private LocalDateTime lastLogin;
        
        public void updateLastLogin() {
            this.lastLogin = LocalDateTime.now();
        }
    }

    // 5. Utilisation de @SneakyThrows
    @SneakyThrows
    public static void riskyOperation() {
        Thread.sleep(100);
        System.out.println("Opération risquée terminée");
    }

    public static void main(String[] args) {
        System.out.println("=== Exemples Lombok Simple ===\n");

        // 1. Test de @Data
        Person person = new Person("Alice Dupont", 30, "alice@example.com");
        System.out.println("1. @Data - Person: " + person);
        
        person.setAge(31);
        System.out.println("   Après modification: " + person);

        // 2. Test de @Builder
        Product product = Product.builder()
                .name("Laptop Dell")
                .price(899.99)
                .category("Informatique")
                .available(true)
                .build();
        
        System.out.println("\n2. @Builder - Product: " + product);

        // 3. Test de @Value (immutable)
        Address address = Address.builder()
                .street("123 Rue de la Paix")
                .city("Paris")
                .zipCode("75001")
                .country("France")
                .build();
        
        System.out.println("\n3. @Value - Address: " + address);

        // 4. Test de @RequiredArgsConstructor et @NonNull
        Customer customer = new Customer("Jean Martin", "jean@example.com");
        customer.setPhone("0123456789");
        customer.updateLastLogin();
        System.out.println("\n4. @RequiredArgsConstructor - Customer: " + customer);

        // 5. Test de @SneakyThrows
        System.out.println("\n5. @SneakyThrows:");
        riskyOperation();

        // 6. Démonstration des méthodes générées automatiquement
        System.out.println("\n6. Méthodes générées automatiquement:");
        Person person1 = new Person("Bob", 25, "bob@example.com");
        Person person2 = new Person("Bob", 25, "bob@example.com");
        
        System.out.println("   person1.equals(person2): " + person1.equals(person2));
        System.out.println("   person1.hashCode(): " + person1.hashCode());
        System.out.println("   person2.hashCode(): " + person2.hashCode());

        // 7. Test de validation @NonNull
        System.out.println("\n7. Test @NonNull:");
        try {
            Customer invalidCustomer = new Customer(null, "test@example.com");
        } catch (NullPointerException e) {
            System.out.println("   @NonNull a détecté une valeur null: " + e.getMessage());
        }

        // 8. Builder avec valeurs par défaut
        System.out.println("\n8. Builder avec valeurs par défaut:");
        Product simpleProduct = Product.builder()
                .name("Souris")
                .price(29.99)
                .category("Accessoires")
                .available(true)
                .build();
        
        System.out.println("   Produit avec date par défaut: " + simpleProduct);
        
        System.out.println("\n=== Lombok fonctionne parfaitement! ===");
    }
}