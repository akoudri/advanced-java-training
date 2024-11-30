package com.akfc.training.jpa;

import java.time.LocalDate;
import java.util.List;


public class Actor {

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private LocalDate birthdate;

    private List<Movie> movies;
}
