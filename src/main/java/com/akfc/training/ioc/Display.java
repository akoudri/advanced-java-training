package com.akfc.training.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Display {
    @Autowired
    private Hello hello;

    @Loggable()
    public void displayMessage() {
        System.out.println(hello.getMessage());
    }
}
