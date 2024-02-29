package com.akfc.training.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorDining {

    public static void main(String[] args) throws InterruptedException {
        Thread[] philosophers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i);
            philosophers[i].start();
        }
        for (int i = 0; i < 5; i++) {
            philosophers[i].join();
        }
    }

    static class Philosopher extends Thread {
        private int id;
        static Lock fork = new ReentrantLock();
        static int sweets = 20;
        private static Condition sweetTaken = fork.newCondition();
        private static int totalSweetsConsumed = 0;

        public Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (totalSweetsConsumed < 100) {
                fork.lock();
                try {
                    while (sweets == 0) {
                        try {
                            sweetTaken.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sweets--;
                    totalSweetsConsumed++;
                    System.out.format("%s has eaten one sweet\n", this.getName());
                } finally {
                    sweetTaken.signal();  // Move the signal inside the finally block
                    fork.unlock();
                }
            }
        }
    }
}