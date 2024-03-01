package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

public class FluxDelay {

    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 10)
                .subscribe(System.out::println);
    }
}
