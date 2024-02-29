package com.akfc.training.streams;

import java.util.Collections;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Titanic {

    private List<Customer> customers;

    public Titanic() {
        customers = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/titanic.csv"))) {
            String line;
            Scanner scan;
            int pClass;
            Sex sex;
            boolean survived;
            String name;
            double age;
            while ((line = br.readLine()) != null) {
                scan = new Scanner(line);
                scan.useDelimiter(";");
                scan.useLocale(Locale.US);
                pClass = scan.nextInt();
                survived = scan.nextInt() != 0;
                name = scan.next();
                //sex = (scan.next().equalsIgnoreCase("male"))?Sex.MALE:Sex.FEMALE;
                sex = switch (scan.next()) {
                    case "male" -> Sex.MALE;
                    default -> Sex.FEMALE;
                };
                if (scan.hasNextDouble()) {
                    age = scan.nextDouble();
                } else if (scan.hasNextInt()) {
                    age = (double) scan.nextInt();
                } else {
                    age = -1;
                }
                customers.add(new Customer(pClass, survived, name, sex, age));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println(customers.size() + " entries loaded");
        }
    }

    public void head(int n) {
        /*if (n > customers.size()) {
            n = customers.size();
        }
        for (int i = 0; i < n; i++) {
            System.out.println(customers.get(i));
        }*/
        customers.stream().limit(n).forEach(System.out::println);
    }

    public void tail(int n) {
        /*if (n > customers.size()) {
            n = customers.size();
        }
        for (int i = customers.size() - n; i < customers.size(); i++) {
            System.out.println(customers.get(i));
        }*/
        customers.stream().skip(customers.size() - n).forEach(System.out::println);
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public static void main(String[] argv) {
        Titanic t = new Titanic();
        t.head(5);
        System.out.println("-----------------");
        t.tail(5);
        System.out.println("-----------------");
        t.getCustomers().stream().limit(5).forEach(e -> System.out.println(String.join(" | ", e.fullName())));
        //DoubleStream s = t.getCustomers().stream().filter( e -> e.age() > 0 && e.sex() == Sex.MALE).mapToDouble(Customer::age);
        double avg = t.getCustomers().stream().filter( e -> e.age() > 0 && e.sex() == Sex.MALE).mapToDouble(Customer::age).average().orElse(0.0);
        System.out.println(avg);
        t.getCustomers().subList(0, 5).stream().map(e -> String.join(" ", e.fullName())).forEach(System.out::println);
        Map<Sex, Double> agePerSex = t.getCustomers().stream()
                .filter(e -> e.age() > 0)
                .collect(Collectors.groupingBy(Customer::sex, Collectors.averagingDouble(Customer::age)));
        /*ConcurrentMap<Sex, Double> agePerSex = t.getCustomers().parallelStream()
                .filter(e -> e.age() > 0)
                .collect(Collectors.groupingByConcurrent(Customer::sex, Collectors.averagingDouble(Customer::age)));*/
        System.out.println(agePerSex);
        t.getCustomers().stream().filter(e -> e.age() > 0)
                .collect(Collectors.groupingBy(Customer::category, Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " : " + v));
/*        t.getCustomers().parallelStream()
                .filter(e -> (e.age() >= 18) && "Miss".equalsIgnoreCase(e.fullName()[0]) && e.survived() && (e.pClass() == 1))
                .sorted(Comparator.comparingDouble(Customer::age))
                .forEach(System.out::println);*/
    }

}
