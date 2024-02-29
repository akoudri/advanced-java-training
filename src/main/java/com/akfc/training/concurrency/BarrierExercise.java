package com.akfc.training.concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Operation extends Thread {

    public static int num = 2;
    private static Lock lock = new ReentrantLock();
    private static CyclicBarrier barrier = new CyclicBarrier(10);

    public Operation(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        if ("mult".equalsIgnoreCase(getName())) {
            lock.lock();
            num *= 2;
            lock.unlock();
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        } else {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            lock.lock();
            num += 5;
            lock.unlock();
        }
    }
}
public class BarrierExercise {

    public static void main(String[] args) throws InterruptedException {
        Operation[] ops = new Operation[10];
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                ops[i] = new Operation("mult");
            } else {
                ops[i] = new Operation("add");
            }
        }
        for (Operation op : ops) {
            op.start();
        }
        for (Operation op : ops) {
            op.join();
        }
        System.out.println(Operation.num);
    }

}
