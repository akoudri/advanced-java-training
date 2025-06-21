package com.akfc.training.misc;

import java.lang.StringTemplate.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Exemples complets d'utilisation des String Templates en Java 21
 * Démontre les processeurs personnalisés et les cas d'usage avancés
 */
public class StringTemplateExamples {
    
    // Processeur pour formater des montants en euros
    public static final StringTemplate.Processor<String, RuntimeException> EURO = 
        st -> {
            List<Object> values = st.values();
            if (values.isEmpty()) return "0,00 €";
            
            double amount = Double.parseDouble(String.valueOf(values.get(0)));
            return String.format("%.2f €", amount).replace('.', ',');
        };
    
    // Processeur pour créer des requêtes SQL sécurisées (exemple simplifié)
    public static final StringTemplate.Processor<String, RuntimeException> SQL = 
        st -> {
            StringBuilder query = new StringBuilder();
            List<String> fragments = st.fragments();
            List<Object> values = st.values();
            
            for (int i = 0; i < fragments.size(); i++) {
                query.append(fragments.get(i));
                if (i < values.size()) {
                    // Dans un vrai cas, on utiliserait des PreparedStatement
                    Object value = values.get(i);
                    if (value instanceof String) {
                        query.append("'").append(value.toString().replace("'", "''")).append("'");
                    } else {
                        query.append(value);
                    }
                }
            }
            return query.toString();
        };
    
    // Processeur pour formater des logs avec timestamp
    public static final StringTemplate.Processor<String, RuntimeException> LOG = 
        st -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String message = STR.process(st); // Utilise le processeur STR standard
            return STR."[\{timestamp}] \{message}";
        };

    public static void main(String[] args) {
        System.out.println("=== Exemples de String Templates Java 21 ===\n");
        
        // 1. Processeur STR standard (le plus courant)
        String name = "Alice";
        int age = 30;
        String greeting = STR."Bonjour \{name}, vous avez \{age} ans.";
        System.out.println("1. STR standard: " + greeting);
        
        // 2. Processeur EURO personnalisé
        double price = 29.99;
        String priceFormatted = EURO."Le prix est de \{price}";
        System.out.println("2. EURO: " + priceFormatted);
        
        // 3. Processeur SQL personnalisé
        String tableName = "users";
        String userName = "O'Connor"; // Teste l'échappement des apostrophes
        int userId = 123;
        String sqlQuery = SQL."SELECT * FROM \{tableName} WHERE name = \{userName} AND id = \{userId}";
        System.out.println("3. SQL: " + sqlQuery);
        
        // 4. Processeur LOG personnalisé
        String logMessage = LOG."Utilisateur \{name} connecté avec succès";
        System.out.println("4. LOG: " + logMessage);
        
        // 5. Processeur PHONE (de votre exemple)
        PhoneProcessor PHONE = new PhoneProcessor();
        String phoneNumber = "0123456789";
        String formattedPhone = PHONE."\{phoneNumber}";
        System.out.println("5. PHONE: " + formattedPhone);
        
        // 6. Composition de processeurs
        String complexMessage = STR."Client: \{name}, Téléphone: \{PHONE."\{phoneNumber}"}, Facture: \{EURO."\{price}"}";
        System.out.println("6. Composition: " + complexMessage);
        
        // 7. Templates multi-lignes
        String report = STR."""
            === RAPPORT CLIENT ===
            Nom: \{name}
            Âge: \{age} ans
            Téléphone: \{PHONE."\{phoneNumber}"}
            Montant dû: \{EURO."\{price}"}
            Date: \{LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}
            """;
        System.out.println("7. Multi-lignes:\n" + report);
        
        // 8. Gestion des erreurs
        try {
            PhoneProcessor phoneProcessor = new PhoneProcessor();
            String invalidPhone = phoneProcessor."\{"invalid"}";
        } catch (IllegalArgumentException e) {
            System.out.println("8. Gestion d'erreur: " + e.getMessage());
        }
        
        // 9. Templates conditionnels
        boolean isVip = true;
        String status = isVip ? "VIP" : "Standard";
        String customerInfo = STR."Client \{status}: \{name} (\{PHONE."\{phoneNumber}"})";
        System.out.println("9. Conditionnel: " + customerInfo);
        
        // 10. Boucles avec templates
        System.out.println("10. Boucles:");
        String[] phones = {"0123456789", "0987654321", "0555123456"};
        for (int i = 0; i < phones.length; i++) {
            String entry = STR."  Contact \{i + 1}: \{PHONE."\{phones[i]}"}";
            System.out.println(entry);
        }
    }
}