package com.akfc.training.reactive;

import com.github.javafaker.Faker;

public class MonoCreate {

    private static Faker faker = new Faker();

    public static String getFullName() {
        System.out.println("Generating name...");
        return faker.name().fullName();
    }


    public static void main(String[] args) throws InterruptedException {
        //TODO: create a Mono that emits the full name of a person
    }

}
