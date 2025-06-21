package com.akfc.training.misc;

import java.io.*;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class Internationalisation {

    private static String readFile(String fileName) {
        StringBuilder content = new StringBuilder();
        InputStream inputStream = Internationalisation.class.getResourceAsStream("/" + fileName);
        
        if (inputStream == null) {
            System.err.println("Could not find resource file: " + fileName);
            return "";
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
        return content.toString();
    }

    private static void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            System.out.println("File written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLocalizedDateTime(Locale locale) {
        // Use ZonedDateTime instead of LocalDateTime for FULL format support
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                .withLocale(locale);
        return now.format(formatter);
    }

    public static void main(String[] args) {
        try {
            Locale userLocale = Locale.getDefault(); //preferred locale
            System.out.println("Using locale: " + userLocale);
            
            // Load resource bundle with better error handling
            ResourceBundle messages;
            try {
                messages = ResourceBundle.getBundle("Message", userLocale);
                System.out.println("Successfully loaded resource bundle for locale: " + userLocale);
            } catch (Exception e) {
                System.err.println("Failed to load resource bundle for locale: " + userLocale);
                System.err.println("Falling back to default locale...");
                messages = ResourceBundle.getBundle("Message", Locale.getDefault());
            }
            
            // Get localized date/time
            String localizedDateTime = getLocalizedDateTime(userLocale);
            
            // Display localized date/time to console
            System.out.println("Current date/time in " + userLocale.getDisplayName() + ": " + localizedDateTime);
            
            String fileContent = readFile("hello_template.txt");
            if (fileContent.trim().isEmpty()) {
                System.err.println("Warning: Template file is empty or could not be read");
                return;
            }
            
            String greeting = messages.getString("greeting");
            String userName = System.getenv("USER");
            if (userName == null || userName.trim().isEmpty()) {
                userName = "User"; // Default fallback
            }
            String formattedGreeting = MessageFormat.format(greeting, userName);
            
            // Replace both greeting and datetime placeholders
            String replacedContent = fileContent.replace("{greeting}", formattedGreeting)
                                               .replace("{datetime}", localizedDateTime);
            
            writeToFile("hello.txt", replacedContent);
            
        } catch (Exception e) {
            System.err.println("An error occurred during execution:");
            e.printStackTrace();
        }
    }
}
