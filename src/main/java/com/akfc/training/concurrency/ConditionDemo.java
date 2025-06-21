package com.akfc.training.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean flag = false;

    public void await() {
        lock.lock();
        try {
            while (!flag) {
                condition.await(); // Wait until flag becomes true
            }
            // Proceed with the operation
            System.out.println("Flag is true. Proceeding...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            flag = true;
            condition.signal(); // Signal one waiting thread
            // condition.signalAll(); // Signal all waiting threads
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionDemo example = new ConditionDemo();

        Thread t1 = new Thread(example::await);
        Thread t2 = new Thread(example::signal);

        t1.start();
        t2.start();
    }
}

