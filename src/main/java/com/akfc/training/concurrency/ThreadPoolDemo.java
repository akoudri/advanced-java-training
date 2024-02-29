package com.akfc.training.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {

    public static void main(String[] args) {
        int numProcs = Runtime.getRuntime().availableProcessors();
        System.out.println(numProcs);
        ExecutorService pool = Executors.newFixedThreadPool(numProcs);
        for (int i = 0; i < 100; i++) {
            pool.submit(new Display());
        }
        pool.shutdown();
    }

    static class Display extends Thread {
        @Override
        public void run() {
            System.out.println(getName() + " executing");
        }
    }
}
