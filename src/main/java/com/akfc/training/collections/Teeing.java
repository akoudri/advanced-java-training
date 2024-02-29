package com.akfc.training.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Teeing {

    public static void example() {
        List<Employee> employeeList = Arrays.asList(
                new Employee(1, "A", 100),
                new Employee(2, "B", 200),
                new Employee(3, "C", 300),
                new Employee(4, "D", 400));

        HashMap<String, Employee> result = employeeList.stream().collect(
                Collectors.teeing(
                        Collectors.maxBy(Comparator.comparing(Employee::salary)),
                        Collectors.minBy(Comparator.comparing(Employee::salary)),
                        (e1, e2) -> {
                            HashMap<String, Employee> map = new HashMap();
                            map.put("MAX", e1.get());
                            map.put("MIN", e2.get());
                            return map;
                        }
                ));
        System.out.println(result);
    }

    public static void exercise() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //TODO: Complete
        Result result = null;
    }

    public static void main(String[] args) {
        //example();
        exercise();
    }

    public record Employee(
            Integer id,
            String name,
            Integer salary
    ){}

    static class Result {
        private final double evenAverage;
        private final double oddAverage;

        public Result(double evenAverage, double oddAverage) {
            this.evenAverage = evenAverage;
            this.oddAverage = oddAverage;
        }

        public double getEvenAverage() {
            return evenAverage;
        }

        public double getOddAverage() {
            return oddAverage;
        }
    }
}
