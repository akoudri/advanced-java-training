package fr.cenotelie.training;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class MonoTest {

    @Test
    public void monoSubscriber() {
        Mono<String> mono = Mono.just("Hello World").log();
        mono.subscribe();
        log.info("-------------------------------");
        StepVerifier.create(mono).expectNext("Hello World").verifyComplete();
    }

    @Test
    public void monoSubscriberError() {
        Mono<String> mono = Mono.just("Hello World").map(s -> { throw new RuntimeException("Error occurred"); });
        mono.subscribe(s -> log.info("Received {}", s), s -> log.error("Something bad happened"));
        log.info("-------------------------------");
        StepVerifier.create(mono).expectError(RuntimeException.class).verify();
    }

    @Test
    public void monoSubscriberComplete() {
        Mono<String> mono = Mono.just("Hello World").map(String::toUpperCase);
        mono.subscribe(s -> log.info("Received {}", s), s -> log.error("Something bad happened"), () -> log.info("FINISHED"));
        log.info("-------------------------------");
        StepVerifier.create(mono).expectNext("HELLO WORLD").verifyComplete();
    }

    @Test
    public void monoSubscriberSubscription() {
        Mono<String> mono = Mono.just("Hello World").map(String::toUpperCase);
        mono.subscribe(s -> log.info("Received {}", s), s -> log.error("Something bad happened"), () -> log.info("FINISHED"), Subscription::cancel);
        log.info("-------------------------------");
        StepVerifier.create(mono).expectNext("HELLO WORLD").verifyComplete();
    }

    @Test
    public void monoSubscriberSubscriptionRequest() {
        Mono<String> mono = Mono.just("Hello World").map(String::toUpperCase).log();
        mono.subscribe(s -> log.info("Received {}", s), s -> log.error("Something bad happened"), () -> log.info("FINISHED"), subscription -> subscription.request(5));
        log.info("-------------------------------");
        StepVerifier.create(mono).expectNext("HELLO WORLD").verifyComplete();
    }

    @Test
    public void monoDoOnMethods() {
        Mono<Object> mono = Mono.just("Hello World")
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("Subscribed"))
                .doOnRequest(number -> log.info("Request received"))
                .doOnNext(s -> log.info("Consuming {}", s))
                .flatMap(s -> Mono.empty())
                .doOnNext(s -> log.info("Consuming {}", s))
                .doOnSuccess(s -> log.info("Successfully completes stream"));
        mono.subscribe(s -> log.info("Received {}", s), s -> log.error("Something bad happened"), () -> log.info("FINISHED"));
    }

    @Test
    public void monoDoOnErrors() {
        Mono<Object> mono = Mono.just("Hello World")
                .map(s -> { throw new IllegalArgumentException(); })
                .onErrorReturn("Hellooo")
                .doOnError(e -> log.error("Error message : {}", e.getMessage()))
                .onErrorResume(s -> Mono.just("Hello"))
                .log();
        //StepVerifier.create(mono).expectError(IllegalArgumentException.class).verify();
        StepVerifier.create(mono).expectNext("Hellooo").verifyComplete();
    }

}