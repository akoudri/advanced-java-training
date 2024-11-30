package com.akfc.training.jpa;

import java.time.LocalDate;
import java.util.List;

public class Movie {

    private Long id;
    private String title;
    private String director;
    private Genre genre;
    private LocalDate productionDate;
    private LocalDate releaseDate;
    //@Column(columnDefinition = "TEXT") //Specific to postgresql
    private String description;
    private Double rating;
    private String fromBook;
    private List<Actor> actors;
}
