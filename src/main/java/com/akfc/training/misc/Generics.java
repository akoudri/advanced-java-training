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
        // animaux.add(new Cat());   // Ajout d'un animal dans la liste (non autorisé si l'argument est une sous-classe de Animal)
        animaux.forEach(Animal::makeSound);
    }

    //Ajouter des nombres à une liste générique sans connaître son type exact.
    public static void ajouterNumbers(List<? super Number> liste) {
        liste.add(10);      // Integer
        liste.add(3.14);    // Double
    }

    static class Animal {
        public void makeSound() {
            System.out.println("Hello");
        }
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

    public static void main(String[] args) {
        List<Cat> cats = List.of(new Cat(), new Cat());
        afficherListe(cats);
        List<Animal> animals = List.of(new Dog(), new Cat(), new Animal());
        afficherListe(animals);
    }
}

