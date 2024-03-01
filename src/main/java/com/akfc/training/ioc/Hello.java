package com.akfc.training.ioc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Hello {
    @Value("#{(environment['spring.profiles.active'] == 'dev')?'Hello Dev':'Hello Prod'}")
    private String message;

    @Loggable
    public String  getMessage() {
        return message;
    }
}
