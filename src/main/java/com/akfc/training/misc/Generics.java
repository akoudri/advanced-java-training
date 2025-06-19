package com.akfc.training.misc;

import java.util.List;

public class Generics {

    //Créer une méthode retournant le maximum de trois objets comparables.

    //Afficher les sons d'une liste d'animaux en utilisant un paramètre générique.

    //Ajouter des nombres à une liste générique sans connaître son type exact.

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

