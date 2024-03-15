package com.akfc.training.reactive;

import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

public class    FluxFlatMap {

    private static Faker faker = new Faker();

    public static Flux<String> favoriteAnimals(String username) {
        return Flux.range(1, 5)
                .map(i -> faker.animal().name());
    }

    public static Flux<String> users() {
        return Flux.range(1, 5)
                .map(i -> faker.name().fullName());
    }

    public static void main(String[] args) {
        users()
                .doOnNext(u -> System.out.println("Favorite animals of " + u))
                .flatMap(u -> favoriteAnimals(u)).subscribe(System.out::println);
    }

}
