package com.akfc.training.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorDining {

    private static final int TOTAL_SWEETS = 20;
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
            while (sweetsLeft >= 0) {
                lock.lock();
                try {
                    while (sweetsLeft == 0) {
                        canEat.await(); // Philosophers wait here if no sweets are left
                    }
                    if (sweetsLeft <= 0) {
                        break;
                    }
                    // Exit condition: Check after being awakened in case all sweets are done.
                    sweetsLeft--; // A sweet is consumed
                    System.out.println(Thread.currentThread().getName() + " ate a sweet. Sweets left: " + sweetsLeft);

                    // After consuming a sweet, signal others that they might be able to eat now.
                    canEat.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName() + " was interrupted.");
                    // Exit the loop and end the thread upon interruption.
                    return;
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
