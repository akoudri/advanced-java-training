package com.akfc.training.misc;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Exemple complet d'utilisation de Lombok avec Java 21
 * Démontre les principales annotations et leurs usages
 */
@Slf4j
public class LombokExample {

    // 1. Classe simple avec @Data (génère getters, setters, equals, hashCode, toString)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {
        private String name;
        private int age;
        private String email;
        
        // Lombok génère automatiquement :
        // - getName(), getAge(), getEmail()
        // - setName(), setAge(), setEmail()
        // - equals() et hashCode()
        // - toString()
        // - Constructeur avec tous les arguments
        // - Constructeur sans arguments
    }

    // 2. Classe avec @Builder pour le pattern Builder
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
        
        // Lombok génère automatiquement :
        // - ProductBuilder avec méthodes fluides
        // - build() method
        // - Valeur par défaut pour createdAt
    }

    // 3. Classe avec @Value (immutable)
    @Value
    @Builder
    public static class Address {
        String street;
        String city;
        String zipCode;
        String country;
        
        // @Value génère :
        // - Tous les champs final
        // - Seulement des getters (pas de setters)
        // - equals(), hashCode(), toString()
        // - Constructeur avec tous les arguments
    }

    // 4. Classe avec validation et @NonNull
    @Data
    @RequiredArgsConstructor
    public static class Customer {
        @NonNull
        private String name;
        
        @NonNull
        private String email;
        
        private String phone; // Optionnel
        
        @Setter(AccessLevel.PRIVATE) // Setter privé
        private LocalDateTime lastLogin;
        
        // @RequiredArgsConstructor génère un constructeur
        // avec tous les champs @NonNull et final
        
        public void updateLastLogin() {
            this.lastLogin = LocalDateTime.now();
        }
    }

    // 5. Classe avec @EqualsAndHashCode personnalisé
    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class User {
        @EqualsAndHashCode.Include
        private String username; // Seul ce champ est utilisé pour equals/hashCode
        
        private String password;
        private String fullName;
        private List<String> roles;
    }

    // 6. Utilisation de @SneakyThrows pour gérer les exceptions
    @SneakyThrows
    public static void riskyOperation() {
        // Simule une opération qui peut lever une exception
        Thread.sleep(100); // InterruptedException automatiquement gérée
        System.out.println("Opération risquée terminée");
    }

    // 7. Utilisation de @Cleanup pour la gestion des ressources
    public static void fileOperation() {
        // Dans un vrai cas, on utiliserait des ressources comme FileInputStream
        System.out.println("Simulation d'opération sur fichier");
        // @Cleanup fermerait automatiquement les ressources
    }

    public static void main(String[] args) {
        System.out.println("=== Exemples Lombok avec Java 21 ===\n");

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
        // address.setCity("Lyon"); // Erreur de compilation - pas de setter

        // 4. Test de @RequiredArgsConstructor et @NonNull
        try {
            Customer customer = new Customer("Jean Martin", "jean@example.com");
            customer.setPhone("0123456789");
            customer.updateLastLogin();
            System.out.println("\n4. @RequiredArgsConstructor - Customer: " + customer);
            
            // Test @NonNull
            // Customer invalidCustomer = new Customer(null, "test@example.com"); // Lèverait NullPointerException
        } catch (Exception e) {
            System.out.println("   Erreur @NonNull: " + e.getMessage());
        }

        // 5. Test de @EqualsAndHashCode personnalisé
        User user1 = new User();
        user1.setUsername("alice");
        user1.setPassword("secret123");
        user1.setFullName("Alice Dupont");

        User user2 = new User();
        user2.setUsername("alice");
        user2.setPassword("different_password");
        user2.setFullName("Alice Martin");

        System.out.println("\n5. @EqualsAndHashCode - Users égaux (même username): " + user1.equals(user2));

        // 6. Test de @SneakyThrows
        System.out.println("\n6. @SneakyThrows:");
        riskyOperation();

        // 7. Test de logging avec @Slf4j
        System.out.println("\n7. @Slf4j - Logging:");
        log.info("Ceci est un message d'information");
        log.warn("Ceci est un avertissement");
        log.debug("Ceci est un message de debug");

        // 8. Démonstration des méthodes générées automatiquement
        System.out.println("\n8. Méthodes générées automatiquement:");
        Person person1 = new Person("Bob", 25, "bob@example.com");
        Person person2 = new Person("Bob", 25, "bob@example.com");
        
        System.out.println("   person1.equals(person2): " + person1.equals(person2));
        System.out.println("   person1.hashCode(): " + person1.hashCode());
        System.out.println("   person2.hashCode(): " + person2.hashCode());

        // 9. Builder avec valeurs par défaut
        System.out.println("\n9. Builder avec valeurs par défaut:");
        Product simpleProduct = Product.builder()
                .name("Souris")
                .price(29.99)
                .category("Accessoires")
                .available(true)
                // createdAt utilise la valeur par défaut
                .build();
        
        System.out.println("   Produit avec date par défaut: " + simpleProduct);
    }
}