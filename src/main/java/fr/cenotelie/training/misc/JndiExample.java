package fr.cenotelie.training.misc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiExample {

    public static void main(String[] args) throws NamingException {
        try {
            // Create a JNDI context
            Context context = new InitialContext();

            // Perform a lookup for a specific resource
            Object resource = context.lookup("java:/fr/cenotelie/training/misc/Fibo");

            // Process the retrieved resource
            System.out.println("Resource: " + resource);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
