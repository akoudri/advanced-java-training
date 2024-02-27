package fr.cenotelie.training.misc;

import com.sun.net.httpserver.*;
import org.nfunk.jep.JEP;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

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

