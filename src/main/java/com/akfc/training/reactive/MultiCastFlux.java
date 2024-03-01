package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

public class MultiCastFlux {

    public static void main(String[] args) throws InterruptedException {
        /*Flux f = Flux.range(0, 10).delayElements(Duration.ofSeconds(1)).share();
        f.subscribe(System.out::println);
        Thread.sleep(3000);
        f.subscribe(e -> System.out.println("\t" + e));
        Thread.sleep(20000);*/
        Flux<Integer> f1 = Flux.range(1, 5);
        Flux<Integer> f2 = Flux.range(6, 10);
        //f1.concatWith(f2).subscribe(System.out::println);
        //f1.zipWith(f2, (e1, e2) -> e1 * e2).subscribe(tuple -> System.out.println(tuple));
        //Flux.zip(f1, f2, f1).subscribe(tuple -> System.out.println(tuple));
        f1.doOnNext(System.out::println).materialize().doOnNext(System.out::println).dematerialize().subscribe(System.out::println);
    }

}
