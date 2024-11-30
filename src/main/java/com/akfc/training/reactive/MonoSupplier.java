package com.akfc.training.reactive;

import com.github.javafaker.Faker;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class    MonoSupplier {

    private static Faker faker = new Faker();

    public static Mono<String> getName() {
        System.out.println("Entering getName");
        return Mono.fromSupplier(() -> {
            System.out.println("Generating name...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return faker.name().fullName();
        })
                .map(String::toUpperCase);
    }

    public static void main(String[] args) throws InterruptedException {
        getName().subscribeOn(Schedulers.boundedElastic())
                .subscribe(System.out::println);
    }
}
