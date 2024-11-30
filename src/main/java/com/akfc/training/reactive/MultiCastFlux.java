package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

public class MultiCastFlux {

    public static void main(String[] args) throws InterruptedException {
        Flux<Integer> f1 = Flux.range(1, 5);
        Flux<Integer> f2 = Flux.range(6, 10);
    }

}
