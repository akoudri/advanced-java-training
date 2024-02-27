package fr.cenotelie.training.collections;

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
            System.out.println(students.get(i).name);
        }
        System.out.println("##### Iteration using iterator #####");
        for (Student s : students) {
            System.out.println(s.name);
        }
        System.out.println("##### Iteration using iterable #####");
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("##### Iteration using foreach loop #####");
        students.forEach(s -> System.out.println(s.name));
        System.out.println("##### Iteration using stream #####");
        students.stream().forEach(s -> System.out.println(s.name));
    }

    record Student(String name) {}
}
