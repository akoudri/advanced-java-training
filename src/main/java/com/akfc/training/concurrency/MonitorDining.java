package com.akfc.training.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorDining {

    private static final int TOTAL_SWEETS = 20;
    private static final Lock lock = new ReentrantLock();
    private static final Condition canEat = lock.newCondition();
    private static volatile int sweetsLeft = TOTAL_SWEETS; // Make sweetsLeft volatile for visibility

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
            while (true) { // Change to an infinite loop to rely on internal break
                lock.lock();
                try {
                    while (sweetsLeft == 0) {
                        canEat.await();
                        // Upon waking, immediately check if we should exit
                        if (sweetsLeft == 0) {
                            return; // Exit thread if there are no sweets left
                        }
                    }
                    sweetsLeft--;
                    System.out.println(Thread.currentThread().getName() + " ate a sweet. Sweets left: " + sweetsLeft);

                    if (sweetsLeft > 0) {
                        canEat.signal(); // Wake up one philosopher
                    } else {
                        canEat.signalAll(); // Wake up all philosophers to exit
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName() + " was interrupted.");
                    return; // Exit thread upon interruption
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
