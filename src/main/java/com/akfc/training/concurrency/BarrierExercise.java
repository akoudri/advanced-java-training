package com.akfc.training.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Operation extends Thread {

    public static int num = 1;
    private static Lock lock = new ReentrantLock();

    public Operation(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        //TODO
    }
}
public class BarrierExercise {

    public static void main(String[] args) throws InterruptedException {
        Operation[] ops = new Operation[9];
        for (int i = 0; i < 9; i++) {
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
