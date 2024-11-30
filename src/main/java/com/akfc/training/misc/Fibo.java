package com.akfc.training.misc;

import java.util.List;

public class Fibo {

    long recursive(int n) {
        //TODO
        return -1;
    }

    void displayEvenNumbers(int n) {
        //TODO
    }

    List<Long> getEvenNumbers(int n) {
        //TODO
        return null;
    }

    public static void main(String[] args) {
        Fibo fibo = new Fibo();
        int n = 50;
        //TODO: measure performance with other methods
        long start = System.currentTimeMillis();
        long res = fibo.recursive(n);
        long duration = System.currentTimeMillis() - start;
        System.out.format("Computed %d in %d ms\n", res, duration);
        fibo.displayEvenNumbers(200);
    }

}
