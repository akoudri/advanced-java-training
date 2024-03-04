package com.akfc.training.reactive;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

public class FluxFibo {

    public static void main(String[] args) {
        Flux<Long> fibonacciFlux = Flux.generate(
                () -> Tuples.of(0L, 1L), // The initial state (0, 1)
                (state, sink) -> {
                    sink.next(state.getT1()); // Emit the first number of the state pair
                    return Tuples.of(state.getT2(), state.getT1() + state.getT2()); // Generate the next state
                }
        );
        fibonacciFlux.take(10).subscribe(System.out::println); // Take the first 10 elements of the Fibonacci series
    }
}
