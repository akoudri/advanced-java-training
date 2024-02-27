package fr.cenotelie.training.concurrency;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataRace {

    public static void main(String[] args) throws InterruptedException {
        //Simple Counter
        /*SimpleCounter[] t = new SimpleCounter[2];
        for (int i = 0; i < 2; i++) {
            t[i] = new SimpleCounter();
            t[i].start();
        }
        for (int i = 0; i < 2; i++) {
            t[i].join();
        }
        System.out.println(SimpleCounter.counter);*/
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
        SynchronizedCounter4[] t = new SynchronizedCounter4[2];
        for (int i = 0; i < 2; i++) {
            t[i] = new SynchronizedCounter4();
            t[i].start();
        }
        for (int i = 0; i < 2; i++) {
            t[i].join();
        }
        System.out.println(SynchronizedCounter4.counter);
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
        static AtomicInteger counter = new AtomicInteger(0);

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                counter.incrementAndGet();
            }
        }
    }

    static class SynchronizedCounter1 extends Thread {
        static int counter = 0;

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (SynchronizedCounter1.class) {
                    counter ++;
                }
            }
        }
    }

    static class SynchronizedCounter2 extends Thread {
        static int counter = 0;

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (this) {
                    counter ++;
                }
            }
        }
    }

    static class SynchronizedCounter3 extends Thread {
        static int counter = 0;

        @Override
        public synchronized void run() {
            for (int i = 0; i < 1000000; i++) {
                counter ++;
            }
        }
    }

    static class SynchronizedCounter4 extends Thread {
        static Integer counter = 0;

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                increment();
            }
        }

        private synchronized void increment() {
            counter ++;
        }
    }

    static class ReentrantCounter extends Thread {
        static int counter = 0;
        static ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            //TODO: lock outside vs inside loop
            lock.lock();
            System.out.format("%s taking lock", this.getName());
            for (int i = 0; i < 10; i++) {
                counter ++;
            }
            System.out.format("%s releasing lock", this.getName());
            lock.unlock();
        }
    }

    static class NonBlockingReentrantCounter extends Thread {
        static int counter = 0;
        static ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                if (lock.tryLock()) {
                    counter ++;
                    lock.unlock();
                } else {
                    System.out.println("Lock not acquired");
                }
            }
        }
    }

    static class RWCounterThread extends Thread {
        static int READER = 0;
        static int WRITER = 1;
        int type = READER;
        Random random = new Random();
        static int counter = 0;
        static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        static Lock readLock = lock.readLock();
        static Lock writeLock = lock.writeLock();

        public RWCounterThread(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            if (type == READER) {
                try {
                    Thread.sleep(random.nextInt(2000));
                    readLock.lock();
                    System.out.printf("%s reading counter with value %d\n", getName(), counter);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    readLock.unlock();
                }
            } else {
                try {
                    Thread.sleep(random.nextInt(2000));
                    writeLock.lock();
                    counter ++;
                    System.out.printf("%s incrementing counter to %d\n", getName(), counter);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    writeLock.unlock();
                }
            }
        }
    }
}
