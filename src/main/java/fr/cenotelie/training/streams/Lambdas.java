package fr.cenotelie.training.streams;

import java.util.function.Function;

public class Lambdas {
    public static void main(String[] args) {
        Function<Integer, Integer> f = x -> x * x;
        Function<Integer, Integer> g = x -> x + 5;
        Function<Integer, Integer> c = f.compose(g);
        System.out.println(f.compose(g).apply(2));
        System.out.println(f.andThen(g).apply(2));
        System.out.println(g.apply(f.apply(2)));
    }
}
