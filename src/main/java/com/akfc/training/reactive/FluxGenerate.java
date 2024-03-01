package com.akfc.training.reactive;

import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

public class FluxGenerate {

    private static Faker faker = new Faker();

    public static void main(String[] args) throws InterruptedException {
        Flux.create(sink -> {
            int i = 0;
            while (i < 17) {
                sink.next(faker.name().fullName());
                i++;
            }
            sink.complete();
        })
                .buffer(5)
                .subscribe(System.out::println);

    }
}
