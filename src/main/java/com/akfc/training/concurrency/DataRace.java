package com.akfc.training.concurrency;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataRace {

    public static void main(String[] args) throws InterruptedException {
        //Simple Counter
        SimpleCounter[] t = new SimpleCounter[2];
        for (int i = 0; i < 2; i++) {
            t[i] = new SimpleCounter();
            t[i].start();
        }
        for (int i = 0; i < 2; i++) {
            t[i].join();
        }
        System.out.println(SimpleCounter.counter);
        //Atomic Counter
        /*AtomicCounter[] t = new AtomicCounter[2];
        for (int i = 0; i < 2; i++) {
            t[i] = new AtomicCounter();
            t[i].start();
        }
        for (int i = 0; i < 2; i++) {
            t[i].join();
        }
        System.out.println(AtomicCounter.counter);*/
        /*SynchronizedCounter4[] t = new SynchronizedCounter4[2];
        for (int i = 0; i < 2; i++) {
            t[i] = new SynchronizedCounter4();
            t[i].start();
        }
        for (int i = 0; i < 2; i++) {
            t[i].join();
        }
        System.out.println(SynchronizedCounter4.counter);*/
        /*CounterThread[] t = new CounterThread[10];
        for (int i = 0; i < 10; i++) {
            if (i < 8) t[i] = new CounterThread(CounterThread.READER);
            else t[i] = new CounterThread(CounterThread.WRITER);
            t[i].start();
        }
        for (int i = 0; i < 10; i++) {
            t[i].join();
        }
        System.out.println(CounterThread.counter);*/
    }

    static class SimpleCounter extends Thread {
        static int counter = 0;

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                counter ++;
            }
        }
    }

    static class AtomicCounter extends Thread {

        @Override
        public void run() {
            //TODO
        }
    }

    static class SynchronizedCounter1 extends Thread {

        @Override
        public void run() {
            //TODO
        }
    }

    static class SynchronizedCounter2 extends Thread {

        @Override
        public void run() {
            //TODO
        }
    }

    static class SynchronizedCounter3 extends Thread {

        @Override
        public synchronized void run() {
            //TODO
        }
    }

    static class SynchronizedCounter4 extends Thread {
        static Integer counter = 0;

        @Override
        public void run() {
            //TODO
        }

    }

    static class ReentrantCounter extends Thread {
        static int counter = 0;
        static ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            //TODO use
        }
    }

    static class NonBlockingReentrantCounter extends Thread {
        static int counter = 0;
        static ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            //TODO: non-blocking lock
        }
    }

    static class RWCounterThread extends Thread {
        Random random = new Random();
        static int counter = 0;
        static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        @Override
        public void run() {
            //TODO: Illustrate read / write lock
        }
    }
}
