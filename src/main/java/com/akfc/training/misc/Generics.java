package com.akfc.training.misc;

import java.util.List;

public class Generics {

    //Créer une méthode retournant le maximum de trois objets comparables.
    public static <T extends Comparable<T>> T max(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) max = b;
        if (c.compareTo(max) > 0) max = c;
        return max;
    }

    //Afficher les sons d'une liste d'animaux en utilisant un paramètre générique.
    public static void afficherListe(List<? extends Animal> animaux) {
        animaux.forEach(Animal::makeSound);
    }

    //Ajouter des nombres à une liste générique sans connaître son type exact.
    public static void ajouterNumbers(List<? super Number> liste) {
        liste.add(10);      // Integer
        liste.add(3.14);    // Double
    }

    static abstract class Animal {
        public abstract void makeSound();
    }

    static class Dog extends Animal {
        @Override
        public void makeSound() {
            System.out.println("Woof!");
        }
    }

    static class Cat extends Animal {
        @Override
        public void makeSound() {
            System.out.println("Meow!");
        }
    }
}

