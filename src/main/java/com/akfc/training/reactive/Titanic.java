package com.akfc.training.reactive;

import reactor.core.publisher.Flux;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Titanic {

    private Flux<Customer> customers;

    public Titanic() {
        customers = loadData();
    }

    private Flux<Customer> loadData() {
        return Flux.empty();
    }

    public Flux<Customer> getCustomers() {
        return customers;
    }

    public static void main(String[] args) {
        Titanic t = new Titanic();
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
