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
        //Get the user's preferred locale

        //Get the messages for the user's locale

        //Get the template file content

        //Format the greeting message

        //Replace the placeholder with the greeting message

        //Write the result to a file

    }
}
