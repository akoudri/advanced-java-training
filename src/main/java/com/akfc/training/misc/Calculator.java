package com.akfc.training.misc;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    public int pgcd(int a, int b) {
        int r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    public int ppcm(int a, int c) {
        return a * c / pgcd(a, c);
    }

    public List<Integer> primeFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
           if (n % i == 0) {
               factors.add(i);
               n /= i;
               i--;
           }
        }
        return factors;
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(calculator.pgcd(12, 18));
        System.out.println(calculator.ppcm(12, 18));
        System.out.println(calculator.primeFactors(625));
    }

}
