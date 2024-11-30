package com.akfc.training.reactive;

import com.github.javafaker.Faker;
import reactor.core.publisher.Flux;

import java.util.Random;

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

        Flux<Integer> flux = Flux.generate(
                () -> 0, // The initial state
                (state, sink) -> {
                    sink.next(state); // Emit the current state
                    if (state == 3) {
                        sink.complete(); // Complete the flux after emitting 3
                    }
                    return state + 1; // Increment the state
                }
        );

        flux.subscribe(
                System.out::println, // Consumer for the next signal
                error -> System.err.println("Error: " + error), // Consumer for the error signal
                () -> System.out.println("Completed") // Runnable for the complete signal
        );
        Random random = new Random();
        Flux.generate(
                        synchronousSink -> {
                            synchronousSink.next(random.nextInt(100));
                        })
                .take(5)
                .subscribe(System.out::println);
    }
}
