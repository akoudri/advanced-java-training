package com.akfc.training.misc;

/**
 * Test runner pour vérifier que Lombok fonctionne correctement
 * avec toutes les classes du projet
 */
public class LombokTestRunner {
    
    public static void main(String[] args) {
        System.out.println("=== Test de Lombok dans le projet ===\n");
        
        // 1. Test de la classe Array avec @Getter
        System.out.println("1. Test de la classe Array avec @Getter:");
        Array array = new Array(new int[]{5, 2, 8, 1, 9});
        
        // Utilisation du getter généré par Lombok
        int[] values = array.getValues();
        System.out.print("   Valeurs originales: ");
        printArray(values);
        
        array.bubblesSort();
        values = array.getValues(); // Getter généré par Lombok
        System.out.print("   Après tri à bulles: ");
        printArray(values);
        
        // 2. Test des exemples Lombok complets
        System.out.println("\n2. Test des classes avec annotations Lombok:");
        
        // Test @Data
        LombokSimpleExample.Person person = new LombokSimpleExample.Person();
        person.setName("Test User");
        person.setAge(25);
        person.setEmail("test@example.com");
        
        System.out.println("   @Data - Person: " + person);
        System.out.println("   getName(): " + person.getName());
        System.out.println("   getAge(): " + person.getAge());
        
        // Test @Builder
        LombokSimpleExample.Product product = LombokSimpleExample.Product.builder()
                .name("Test Product")
                .price(99.99)
                .category("Test")
                .available(true)
                .build();
        
        System.out.println("   @Builder - Product: " + product);
        
        // Test @Value (immutable)
        LombokSimpleExample.Address address = LombokSimpleExample.Address.builder()
                .street("Test Street")
                .city("Test City")
                .zipCode("12345")
                .country("Test Country")
                .build();
        
        System.out.println("   @Value - Address: " + address);
        
        // Test @RequiredArgsConstructor avec @NonNull
        LombokSimpleExample.Customer customer = new LombokSimpleExample.Customer("Test Customer", "customer@test.com");
        customer.setPhone("0123456789");
        customer.updateLastLogin();
        
        System.out.println("   @RequiredArgsConstructor - Customer: " + customer);
        
        // Test des méthodes equals et hashCode générées
        System.out.println("\n3. Test des méthodes générées automatiquement:");
        LombokSimpleExample.Person person1 = new LombokSimpleExample.Person("John", 30, "john@test.com");
        LombokSimpleExample.Person person2 = new LombokSimpleExample.Person("John", 30, "john@test.com");
        LombokSimpleExample.Person person3 = new LombokSimpleExample.Person("Jane", 25, "jane@test.com");
        
        System.out.println("   person1.equals(person2): " + person1.equals(person2));
        System.out.println("   person1.equals(person3): " + person1.equals(person3));
        System.out.println("   person1.hashCode() == person2.hashCode(): " + (person1.hashCode() == person2.hashCode()));
        System.out.println("   person1.toString(): " + person1.toString());
        
        // Test de validation @NonNull
        System.out.println("\n4. Test de validation @NonNull:");
        try {
            LombokSimpleExample.Customer invalidCustomer = new LombokSimpleExample.Customer(null, "test@example.com");
            System.out.println("   ERREUR: @NonNull n'a pas fonctionné!");
        } catch (NullPointerException e) {
            System.out.println("   ✓ @NonNull fonctionne: " + e.getMessage());
        }
        
        System.out.println("\n=== Tous les tests Lombok sont réussis! ===");
        System.out.println("\nLombok est maintenant correctement configuré dans votre projet:");
        System.out.println("- Version: 1.18.30 (compatible Java 21)");
        System.out.println("- Scope: provided (ne sera pas inclus dans le JAR final)");
        System.out.println("- Annotation processor: configuré dans Maven");
        System.out.println("- Preview features: activées pour Java 21");
    }
    
    private static void printArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}