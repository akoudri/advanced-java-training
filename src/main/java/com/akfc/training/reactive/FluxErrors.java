package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxErrors {

    public static void main(String[] args) {
        Flux.range(1, 10)
                .map(i -> 10 / (5 - i))
                //.onErrorReturn(-1)
                //.onErrorContinue((err, obj) -> System.out.println("Error: " + err.getMessage() + " for " + obj))
                //.onErrorResume(err -> Flux.range(1, 5))
                //.onErrorComplete()
                .subscribe(System.out::println);
    }
}
