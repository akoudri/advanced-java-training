package fr.cenotelie.training.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BinarySearch {

    public static void main(String[] args) {
        List<Man> mens = new ArrayList<>();
        mens.add(new Man("Michel", 32));
        mens.add(new Man("Paul", 41));
        mens.add(new Man("Gaston", 28));
        mens.add(new Man("Michel", 45));
        mens.add(new Man("Paul", 53));
        mens.add(new Man("Joseph", 41));
        Collections.sort(mens, Comparator.comparingInt(Man::getAge));
        //TODO: search for man having 41 years old
    }

    static class Man {
        String name;
        int age;

        Man(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return this.name;
        }

        int getAge() {
            return this.age;
        }
    }

}
