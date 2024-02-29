package com.akfc.training.concurrency;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

class EndSubscriber<T> implements Flow.Subscriber<T> {

    private Flow.Subscription subscription;
    public List<T> consumedElements = new LinkedList<>();

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        System.out.println("Subscriber - Received one data " + item.toString());
        consumedElements.add(item);
        try {
            Thread.sleep(500);
            subscription.request(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Subscriber done");
        consumedElements.forEach(System.out::println);
    }
}

class TransformerProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

    private Flow.Subscription subscription;
    private Function<T,R> function;

    public TransformerProcessor(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        System.out.println("Transformer - Received one data " + item.toString());
        submit(function.apply(item));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        close();
    }
}

public class ReactiveDemo {
    public static void main(String[] argv) throws InterruptedException {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        TransformerProcessor<Integer, Integer> transformerProcessor = new TransformerProcessor<>(e -> e * e);
        EndSubscriber<Integer> subscriber = new EndSubscriber<>();
        publisher.subscribe(transformerProcessor);
        transformerProcessor.subscribe(subscriber);
        for (Integer i : List.of(1, 2, 3, 4, 5)) {
            Thread.sleep(500);
            publisher.submit(i);
        }
        publisher.close();
        Thread.sleep(5000);
    }
}
