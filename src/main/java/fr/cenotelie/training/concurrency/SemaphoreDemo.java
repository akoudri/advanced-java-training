package fr.cenotelie.training.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class SemaphoreDemo {

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new SemThread("Thread-" + i).start();
        }
    }

    static class SemThread extends Thread {
        String name;
        private static Semaphore sem = new Semaphore(1); //TODO: change the capacity and observe the results

        SemThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                sem.acquire();
                System.out.format("%s acquiring the semaphore\n", getName());
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.format("%s releasing the semaphore\n", getName());
                sem.release();
            }
        }
    }
}
