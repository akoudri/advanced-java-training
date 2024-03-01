package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

public class FluxToMono {

    public static void main(String[] args) {
        Flux f = Flux.range(0, 10);
        f.collectList().subscribe(System.out::println);
    }
}
