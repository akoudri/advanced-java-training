package com.akfc.training.misc;

import java.util.*;
import java.lang.StringTemplate.*;

public class PhoneProcessor implements StringTemplate.Processor<String, IllegalArgumentException> {
    @Override
    public String process(StringTemplate st) throws IllegalArgumentException {
        // On récupère les valeurs à interpoler
        List<Object> values = new ArrayList<>(st.values());
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Aucun numéro de téléphone fourni");
        }
        
        String phone = String.valueOf(values.get(0));
        
        // Validation basique du numéro
        if (!phone.matches("\\d{9,10}")) {
            throw new IllegalArgumentException("Format de numéro invalide: " + phone);
        }
        
        // Formatage français avec +33
        String cleanPhone = phone.replaceAll("\\D", ""); // Supprime tout sauf les chiffres
        if (cleanPhone.startsWith("0")) {
            cleanPhone = cleanPhone.substring(1); // Supprime le 0 initial
        }
        
        // Formatage par paires: +33 1 23 45 67 89
        String formatted = "+33 " + cleanPhone.replaceAll("(\\d{1})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1 $2 $3 $4 $5");
        return formatted;
    }

    public static void main(String[] args) {
        PhoneProcessor PHONE = new PhoneProcessor();
        
        // Exemples d'utilisation
        String[] testNumbers = {"0123456789", "123456789", "01 23 45 67 89"};
        
        for (String number : testNumbers) {
            try {
                // Utilisation du processeur avec un template
                String formatted = PHONE."\{number}";
                System.out.println("Original: " + number + " -> Formaté: " + formatted);
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur pour " + number + ": " + e.getMessage());
            }
        }
        
        // Exemple avec template plus complexe
        String name = "Jean Dupont";
        String phone = "0123456789";
        
        // Utilisation du processeur STR standard pour comparaison
        String contact = STR."Contact: \{name}, Téléphone: \{PHONE."\{phone}"}";
        System.out.println("\n" + contact);
    }
}