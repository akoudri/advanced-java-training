package fr.cenotelie.training.concurrency;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer extends Thread {

    private BlockingQueue<Integer> queue;

    public Producer(String name, BlockingQueue<Integer> queue) {
        this.setName(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            queue.add(1);
            System.out.format("Remaining capacity = %d\n", queue.remainingCapacity());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.add(0);
    }
}

class Consumer extends Thread {

    private BlockingQueue<Integer> queue;

    public Consumer(String name, BlockingQueue<Integer> queue) {
        this.setName(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true) {
            try {
                int n = queue.take();
                if (n == 0) break;
                System.out.format("Consumer %s has consumed one data\n", getName());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class QueueExercise {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        new Consumer("C1", queue).start();
        new Producer("P1", queue).start();
    }
}
