package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

public class FluxToMono {

    public static void main(String[] args) {
        Flux<Integer> f = Flux.range(0, 10);
        f.subscribe(System.out::println, System.err::println, () -> System.out.println("Processing completed"));
    }
}
