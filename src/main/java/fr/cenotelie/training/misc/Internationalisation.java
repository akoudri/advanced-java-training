package fr.cenotelie.training.misc;

import java.io.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Internationalisation {

    private static String readFile(String fileName) {
        StringBuilder content = new StringBuilder();
        InputStream inputStream = Internationalisation.class.getResourceAsStream("/" + fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
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

    public static void main(String[] args) {
        Locale userLocale = Locale.getDefault(); //preferred locale
        ResourceBundle messages = ResourceBundle.getBundle("Message", userLocale);
        String fileContent = readFile("hello_template.txt");
        String greeting = messages.getString("greeting");
        String formattedGreeting = MessageFormat.format(greeting, System.getenv("USER"));
        String replacedContent = fileContent.replace("{greeting}", formattedGreeting);
        writeToFile("hello.txt", replacedContent);
    }
}
