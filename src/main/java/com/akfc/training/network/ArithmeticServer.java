package com.akfc.training.network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ArithmeticServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        //Map path to handler

        //Start the server
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class ArithmeticOperationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Parse the query parameters

            // Use JEP to evaluate expression

            // Use the exchange to reply

        }
    }
}

