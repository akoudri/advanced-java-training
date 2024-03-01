package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Titanic {

    private Flux<Customer> customers;

    public Titanic() {
        customers = loadData();
    }

    private Flux<Customer> loadData() {
        return Flux.using(
                () -> new BufferedReader(new InputStreamReader(Titanic.class.getResourceAsStream("/titanic.csv"))),
                reader -> Flux.fromStream(reader.lines()),
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        )
                .map(l -> {
                    Scanner scan = new Scanner(l);
                    scan.useDelimiter(";");
                    scan.useLocale(Locale.US);
                    int pClass = scan.nextInt();
                    boolean survived = scan.nextInt() != 0;
                    String name = scan.next();
                    Sex sex = (scan.next().equalsIgnoreCase("male"))? Sex.MAN: Sex.WOMAN;
                    double age = -1;
                    if (scan.hasNextDouble()) {
                        age = scan.nextDouble();
                    }
                    return new Customer(pClass, survived, name, sex, age);
                });
    }

    public Flux<Customer> getCustomers() {
        return customers;
    }

    public static void main(String[] args) {
        Titanic t = new Titanic();
        t.customers.take(5).subscribe(System.out::println);
        t.customers.filter(e -> e.sex == Sex.MAN && e.age > 0).collectList()
                .map(l -> l.stream().map(e -> e.age).mapToDouble(e -> e * 1.0).average())
                .subscribe(System.out::println);
        /*Titanic t = new Titanic();
        double avg = t.getCustomers().stream().filter(e -> e.age() >= 0 && e.sex() == Sex.MAN).mapToDouble(Customer::age).average().orElse(0.0);
        t.getCustomers().stream().limit(5).map(Customer::fullName).forEach(e -> System.out.format("%s\t%s\t%s\n", e[0], e[1], e[2]));
        System.out.println(avg);
        t.customers.parallelStream()
                .filter(e -> e.age() >= 0)
                .collect(Collectors.groupingByConcurrent(Customer::sex, Collectors.averagingDouble(Customer::age)))
                .forEach((k, v) -> System.out.println(k + " : " + v));*/
    }

    record Customer(int pClass, boolean survived, String name, Sex sex, double age) {

        public String[] fullName() {
            Pattern p = Pattern.compile("(\\w+), (\\w+)\\. (.*)");
            Matcher m = p.matcher(name);
            if (m.find()) {
                return new String[] { m.group(2), m.group(1), m.group(3) };
            }
            return null;
        }
        @Override
        public String toString() {
            return String.format("%d\t%b\t%s\t%s\t%.2f", pClass, survived, name, sex.name(), age);
        }
    }

    enum Sex {
        MAN, WOMAN;
    }

}
