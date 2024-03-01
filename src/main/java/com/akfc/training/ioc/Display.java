package com.akfc.training.ioc;

public class Display {

    private Hello hello;

    public Display(Hello hello) {
        this.hello = hello;
    }

    public void displayMessage() {
        System.out.println(hello.getMessage());
    }

    public Hello getHello() {
        return hello;
    }

    public void setHello(Hello hello) {
        this.hello = hello;
    }
}
