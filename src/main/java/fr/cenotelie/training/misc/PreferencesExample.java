package fr.cenotelie.training.misc;

import java.util.prefs.Preferences;

public class PreferencesExample {
    public static void main(String[] args) {
        // Get the user preferences node for this class
        Preferences preferences = Preferences.userNodeForPackage(PreferencesExample.class);

        // Set preferences
        preferences.put("username", "john.doe");
        preferences.put("email", "john.doe@example.com");
        preferences.putInt("age", 30);
        preferences.putBoolean("notifications", true);

        // Retrieve preferences
        String username = preferences.get("username", "");
        String email = preferences.get("email", "");
        int age = preferences.getInt("age", 0);
        boolean notifications = preferences.getBoolean("notifications", false);

        // Display preferences
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Age: " + age);
        System.out.println("Notifications: " + notifications);
    }
}

