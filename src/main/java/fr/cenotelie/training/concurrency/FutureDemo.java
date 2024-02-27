package fr.cenotelie.training.concurrency;

import java.util.concurrent.*;

public class FutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> res = executor.submit(new Operation());
        System.out.println("Requesting operation");
        System.out.println("Response = " + res.get());
        executor.shutdown();
    }

    static class Operation implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("Performing operation");
            Thread.sleep(3000);
            return 42;
        }
    }
}
