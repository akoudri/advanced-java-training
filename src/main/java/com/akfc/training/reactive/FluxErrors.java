package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

public class FluxErrors {

    public static void main(String[] args) {
        Flux.range(1, 10)
                .map(i -> 10 / (5 - i))
                .subscribe(System.out::println);
    }
}
