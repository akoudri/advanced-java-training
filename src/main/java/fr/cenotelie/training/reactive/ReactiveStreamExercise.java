package fr.cenotelie.training.reactive;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamExercise {

    public static void main(String[] args) {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        //TODO: run one transformer and one subscriber
        // The publisher shall submit a set of integers
        // The transformer squares theme
        // The subscriber stores the results into an array
    }

    static class EnsSubscriber<T> implements Flow.Subscriber<T> {

        @Override
        public void onSubscribe(Flow.Subscription subscription) {

        }

        @Override
        public void onNext(T item) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }

    static class TransformProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

        @Override
        public void onSubscribe(Flow.Subscription subscription) {

        }

        @Override
        public void onNext(T item) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
}
