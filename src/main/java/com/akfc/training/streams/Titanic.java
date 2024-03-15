package com.akfc.training.streams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Titanic {

    private List<Customer> customers;

    public Titanic() {
        customers = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        try(BufferedReader br =
                    Files.newBufferedReader(Path.of("src/main/resources/titanic.csv"))) {
            String line;
            Scanner scan;
            int pClass;
            boolean survived;
            String name;
            Sex sex;
            double age;
            while ((line = br.readLine()) != null) {
                scan = new Scanner(line);
                scan.useDelimiter(";");
                scan.useLocale(Locale.US);
                pClass = scan.nextInt();
                survived = scan.nextInt() != 0;
                name = scan.next();
                sex = switch (scan.next()) {
                    case "male" -> Sex.MALE;
                    default -> Sex.FEMALE;
                };
                if (scan.hasNextDouble()) {
                    age = scan.nextDouble();
                } else if (scan.hasNextInt()) {
                    age = (double) scan.nextInt();
                } else {
                    age = -1.0;
                }
                customers.add(new Customer(pClass, survived, name, sex, age));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void head(int n) {
        customers.stream().limit(n).forEach(System.out::println);
    }

    public void tail(int n) {
        customers.stream().skip(customers.size() - n).forEach(System.out::println);
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public static void main(String[] argv) {
        Titanic t = new Titanic();
        t.head(5);
        System.out.println("-------------------");
        t.tail(5);
        System.out.println("-------------------");
        double avg = t.getCustomers()
                .stream()
                .filter(e -> e.sex() == Sex.MALE && e.age() > 0)
                .mapToDouble(Customer::age)
                .average()
                .orElse(0.0);
        System.out.println("Age moyen des hommes : " + avg);
        System.out.println("-------------------");
        t.getCustomers().stream()
                .limit(5)
                .map(Customer::fullName)
                .filter(Objects::nonNull)
                .map(a -> String.join(" ", a))
                .forEach(System.out::println);
        System.out.println("-------------------");
        Map<Sex, Double> agePerSex = t.getCustomers().stream()
                .filter(e -> e.age() > 0)
                .collect(Collectors.groupingBy(Customer::sex, Collectors.averagingDouble(Customer::age)));
        System.out.println("Age moyen femmes: " + agePerSex.get(Sex.FEMALE));
        System.out.println("Age moyen hommes: " + agePerSex.get(Sex.MALE));
        System.out.println("-------------------");
        Map<String, Double> agePerCategory = t.getCustomers().parallelStream()
                .filter(e -> e.age() > 0)
                .collect(Collectors.groupingByConcurrent(Customer::category, Collectors.averagingDouble(Customer::age)));
        agePerCategory.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-------------------");
        Map<String, Long> countPerCategory = t.getCustomers().stream()
                .filter(e -> e.age() > 0)
                .collect(Collectors.groupingBy(Customer::category, Collectors.counting()));
        countPerCategory.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-------------------");
        t.getCustomers().parallelStream()
                .filter(e -> e.age() >= 18 &&
                        e.pClass() == 1 &&
                        e.survived() &&
                        "Miss".equalsIgnoreCase(e.fullName()[0]))
                .sorted(Comparator.comparingDouble(Customer::age))
                .forEach(System.out::println);
    }

}
