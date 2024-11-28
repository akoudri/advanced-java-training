package com.akfc.training.test;

public class Test {

    public static void main(String[] args) {
        int a  = 10;
        int b = 0;
        int c = 0;
        try {
            c = a/b;
            System.out.println(c);
        } catch (ArithmeticException e) {
            System.out.println("Division by zero");
        }
        System.out.println(c);
    }

}
