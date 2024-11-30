package com.akfc.training.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorDining {

    private static final int TOTAL_SWEETS = 100;
    private static final Lock lock = new ReentrantLock();
    private static final Condition canEat = lock.newCondition();
    private static int sweetsLeft = TOTAL_SWEETS;

    public static void main(String[] args) throws InterruptedException {
        Thread[] philosophers = new Thread[5];
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Thread(new Philosopher(i), "Philosopher " + i);
            philosophers[i].start();
        }
        for (Thread philosopher : philosophers) {
            philosopher.join();
        }
    }

    static class Philosopher implements Runnable {

        private final int id;

        public Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            //TODO: use conditions as notification mechanism
        }
    }
}
