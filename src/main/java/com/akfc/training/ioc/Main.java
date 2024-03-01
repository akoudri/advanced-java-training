package com.akfc.training.ioc;

public class Main {
    public static void main(String[] args) {
        Hello hello = new Hello("Hello World");
        Display display = new Display(hello);
        display.displayMessage();
    }
}
