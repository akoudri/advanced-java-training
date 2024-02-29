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
        //Iteration using index

        //Iteration using iterator

        //Iteration using iterable

        //Iteration using foreach

        //Iteration using stream

    }

    record Student(String name) {}
}
