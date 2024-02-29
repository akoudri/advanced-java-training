package com.akfc.training.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorDining {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Philosopher(i).start();
        }
    }

    static class Philosopher extends Thread {

        private int id;
        static Lock fork = new ReentrantLock();
        static int sweets = 20;
        private static Condition sweetTaken = fork.newCondition();

        public Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            //TODO: use conditions as notification mechanism
        }
    }
}
