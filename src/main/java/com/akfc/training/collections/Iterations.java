package com.akfc.training.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Iterations {

    public static void main(String[] argv) {
        List<Student> students = new ArrayList<>(List.of(
                new Student("Pauline"),
                new Student("Alain"),
                new Student("Michel"),
                new Student("Marie")
        ));
        System.out.println("##### Iteration using index #####");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.println(s.name);
            if ("Michel".equals(s.name)) {
                students.remove(i);
                i--;
            }

        }
        System.out.println("##### Iteration using iterator #####");
        for (Student s : students) {
            System.out.println(s.name);
            /*if ("Michel".equals(s.name)) {
                students.remove(s);
            }*/
        }
        System.out.println("##### Iteration using iterable #####");
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            Student s = it.next();
            System.out.println(s);
            /*if ("Michel".equals(s.name)) {
                it.remove();
            }*/
        }
        System.out.println("##### Iteration using foreach loop #####");
        students.forEach(s -> {
            System.out.println(s.name);
            //students.remove(s);
        });
        System.out.println("##### Iteration using stream #####");
        students.stream().forEach(s -> {
            System.out.println(s.name);
            //students.remove(s);
        });
    }

    record Student(String name) {}
}
