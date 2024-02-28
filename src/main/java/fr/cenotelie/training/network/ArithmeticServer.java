package fr.cenotelie.training.network;

import com.sun.net.httpserver.*;
import org.nfunk.jep.JEP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ArithmeticServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/arithmetics", new ArithmeticOperationHandler());
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class ArithmeticOperationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Parse the query parameters
            String query = exchange.getRequestURI().getQuery();
            String[] params = query.split("&");
            String op = "add";
            int op1 = 0;
            int op2 = 0;

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    if (key.equals("op1")) {
                        op1 = Integer.parseInt(value);
                    } else if (key.equals("op2")) {
                        op2 = Integer.parseInt(value);
                    } else {
                        op = value;
                    }
                }
            }
            JEP jep = new JEP();
            String operator = switch (op) {
                case "mult" -> "*";
                case "div" -> "/";
                case "minus" -> "-";
                default -> "+";
            };
            String expression = op1 + operator + op2;
            jep.parseExpression(expression);
            double res = jep.getValue();
            // Prepare the response
            String response = "Result: " + res;
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}

