package fr.cenotelie.training.misc;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static void main(String[] args) {
        try {
            // Generate a secret key
            SecretKey secretKey = generateSecretKey();

            // Encrypt the data
            String plainText = "Hello, World!";
            byte[] encryptedData = encryptData(plainText, secretKey);
            System.out.println("Encrypted data: " + byteArrayToHexString(encryptedData));

            // Decrypt the data
            String decryptedText = decryptData(encryptedData, secretKey);
            System.out.println("Decrypted data: " + decryptedText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        // Generate a secret key using AES algorithm
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // Key size: 128 bits
        return keyGenerator.generateKey();
    }

    private static byte[] encryptData(String plainText, SecretKey secretKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create a cipher object for encryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the plain text
        byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return encryptedData;
    }

    private static String decryptData(byte[] encryptedData, SecretKey secretKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create a cipher object for decryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the encrypted data
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
