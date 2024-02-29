package com.akfc.training.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    public static void main(String[] args) {
        Lock fourchette1 = new ReentrantLock();
        Lock fourchette2 = new ReentrantLock();
        Lock fourchette3 = new ReentrantLock();
        new Philosopher("Socrate", fourchette1, fourchette2).start();
        new Philosopher("Platon", fourchette2, fourchette3).start();
        //new Philosopher("Kant", fourchette3, fourchette1).start(); //First case of DEADLOCK !!!
        new Philosopher("Kant", fourchette1, fourchette3).start(); //NO DEADLOCK
    }

    static class Philosopher extends Thread {

        private Lock firstFork, secondFork;
        private static int sweetCount = 5_000;

        public Philosopher(String name, Lock firstFork, Lock secondFork) {
            this.setName(name);
            this.firstFork = firstFork;
            this.secondFork = secondFork;
        }

        @Override
        public void run() {
            //Get forks, eat one sweet and release the fork
            //Do it while there is any food left
            while (sweetCount > 0) {
                firstFork.lock();
                secondFork.lock();
                if (sweetCount > 0) {
                    sweetCount--;
                    System.out.format("%s has eaten one sweet\n", this.getName());
                    if (sweetCount == 10) {
                        int a = 10/0; //2nd case of deadlock
                        //can be solved using try catch finally (to release the lock)
                    }
                }
                secondFork.unlock();
                firstFork.unlock();
            }
        }
    }
}
