package com.akfc.training.misc;

import java.util.ArrayList;
import java.util.List;
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
        a = 1;
        b = 1;
        somme = 0;
        for (int i = 1; i < n; i++) {
            somme = a + b;
            a = b;
            b = somme;
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

    public List<Long> getEvenNumbers(int n) {
        assert n > 2;
        List<Long> res = new ArrayList<>();
        double or3 = pow((sqrt(5) + 1)/2, 3);
        long num = 2;
        while (num <= n) {
            res.add(num);
            num = round(num * or3);
        }
        return res;
    }

    public static void main(String[] args) {
        Fibo fibo = new Fibo();
        /*long start = System.currentTimeMillis();
        long res = fibo.slide(n);
        long duration = System.currentTimeMillis() - start;
        System.out.format("Computed %d in %d ms\n", res, duration);
        fibo.displayEvenNumbers(200);*/
        long start = System.currentTimeMillis();
        List<Long> res = fibo.getEvenNumbers(200);
        long duration = System.currentTimeMillis() - start;
        System.out.format("Computed %d in %d ms\n", res.size(), duration);
        res.forEach(System.out::println);
    }

}
