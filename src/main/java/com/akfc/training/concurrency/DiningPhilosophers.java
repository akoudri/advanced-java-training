package com.akfc.training.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    public static void main(String[] args) {
        Lock fourchette1 = new ReentrantLock();
        Lock fourchette2 = new ReentrantLock();
        Lock fourchette3 = new ReentrantLock();
        //TODO: Instanciate 3 philosophers and illustrate deadlock, livelock and starvation
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

        }
    }
}
