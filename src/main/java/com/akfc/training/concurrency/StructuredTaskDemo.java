package com.akfc.training.concurrency;

import java.util.concurrent.StructuredTaskScope;

public class StructuredTaskDemo {
    public static void main(String[] args) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var userTask = scope.fork(() -> findUser());
            var orderTask = scope.fork(() -> fetchOrder());

            scope.join(); // Attend que toutes les tâches soient terminées ou qu'une échoue
            scope.throwIfFailed(); // Propage l'exception si une tâche a échoué

            // Les deux tâches ont réussi, on peut composer les résultats
            System.out.println("User: " + userTask.get());
            System.out.println("Order: " + orderTask.get());
        }
    }

    static String findUser() { return "Alice"; }
    static int fetchOrder() { return 42; }
}

