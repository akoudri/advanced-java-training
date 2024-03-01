package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxDelay {

    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .subscribe(System.out::println);
        Thread.sleep(11000);
    }
}
