package com.akfc.training.concurrency;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

class EndSubscriber<T> implements Flow.Subscriber<T> {


    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        //TODO
    }

    @Override
    public void onNext(T item) {
        //TODO
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Subscriber done");
    }
}

class TransformerProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {


    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        //TODO
    }

    @Override
    public void onNext(T item) {
        //TODO
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        //TODO
    }
}

public class ReactiveStream {
    public static void main(String[] argv) throws InterruptedException {
        //TODO: instanciate publisher, transformer and subscriber
        //Connect them and run the chain
    }
}
