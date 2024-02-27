package fr.cenotelie.training;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Slf4j
public class FluxTest {

    @Test
    public void fluxSubscriber() {
        Flux<String> flux = Flux.just("Hello", "World", "How", "Are", "You", "?").log();
        StepVerifier.create(flux).expectNext("Hello", "World", "How", "Are", "You", "?").verifyComplete();
    }

    @Test
    public void fluxSubscriberNumbers() {
        //Flux<Integer> flux = Flux.range(1, 10).log();
        //Following line to compare with fluxSubscriberBP (to do before)
        Flux<Integer> flux = Flux.range(1, 10).log().limitRate(3);
        StepVerifier.create(flux).expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).verifyComplete();
    }

    @Test
    public void fluxSubscriberInterval() throws InterruptedException {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(100)).take(10);
        //flux.subscribe(i -> log.info("Number {}", i));
        //Thread.sleep(3000);
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(100)))
                .expectSubscription()
                .thenAwait(Duration.ofMillis(3000))
                .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                .thenCancel()
                .verify();
    }

    @Test
    public void fluxSubscriberNumbersError() {
        Flux<Integer> flux = Flux.range(1, 5).log()
                        .map(i -> {
                            if (i == 4) {
                                throw new IndexOutOfBoundsException("index error");
                            }
                            return i;
                        });
        //flux.subscribe(i -> log.info("Number {}", i), Throwable::printStackTrace, () -> log.info("Done !"));
        StepVerifier.create(flux).expectNext(1, 2, 3).expectError(IndexOutOfBoundsException.class).verify();
    }

    @Test
    public void fluxSubscriberBP() {
        Flux<Integer> flux = Flux.range(1, 10).log();
        flux.subscribe(new Subscriber<Integer>() {
            private Subscription subscription;
            final int rNum = 2;
            int count = 0;
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(rNum);
            }

            @Override
            public void onNext(Integer integer) {
                count ++;
                if (count >= rNum) {
                    count = 0;
                    subscription.request(rNum);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        StepVerifier.create(flux).expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).verifyComplete();
    }

    @Test
    public void connectableFlux() throws InterruptedException {
        //Illustrates hot streams
        ConnectableFlux<Integer> flux = Flux.range(1, 10).log().delayElements(Duration.ofMillis(100)).publish();
        flux.connect();
        Thread.sleep(300);
        flux.subscribe(i -> log.info("Sub 1 Consuming (subscribe) {}", i));
        Thread.sleep(200);
        flux.subscribe(i -> log.info("Sub 2 Consuming (subscribe) {}", i));
        /*StepVerifier.create(flux)
                .then(() -> flux.connect())
                .thenConsumeWhile(i -> i <= 5)
                .expectNext(6, 7, 8, 9, 10)
                .expectComplete()
                .verify();*/
    }
}
