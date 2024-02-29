package com.akfc.training.concurrency;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer extends Thread {

    @Override
    public void run() {
        //TODO: produce data
    }
}

class Consumer extends Thread {

    @Override
    public void run() {
        //TODO: consume data
    }
}
public class QueueExercise {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        //TODO: instanciate and start producer and consumer
    }
}
