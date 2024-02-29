package com.akfc.training.misc;

import java.util.Map;
import static java.lang.Math.*;

public class Fibo {

    long recursive(int n) {
        assert n > 0 : "N shall be greater than 0";
        if (n <= 2) return 1;
        return recursive(n-1) + recursive(n-2);
    }

    long cache(int n, Map<Integer, Long> map) {
        assert n > 0;
        if (n <= 2) return 1;
        if (map.containsKey(n)) return map.get(n);
        long res = cache(n-1, map) + cache(n-2, map);
        map.put(n, res);
        return res;
    }

    long slide(int n) {
        assert n > 0;
        if (n <= 2) return 1;
        long a, b, somme;
        b = 1;
        somme = 2;
        for (int i = 3; i < n; i++) {
            a = b;
            b = somme;
            somme = a + b;
        }
        return somme;
    }

    void displayEvenNumbers(int n) {
        assert n > 2;
        double or3 = pow((sqrt(5) + 1)/2, 3);
        long num = 2;
        while (num <= n) {
            System.out.format("%d ", num);
            num = round(num * or3);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Fibo fibo = new Fibo();
        int n = 50;
        long start = System.currentTimeMillis();
        long res = fibo.slide(n);
        long duration = System.currentTimeMillis() - start;
        System.out.format("Computed %d in %d ms\n", res, duration);
        fibo.displayEvenNumbers(200);
    }

}
