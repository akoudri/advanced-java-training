package com.akfc.training.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "director"})
})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String title;
    @Column(nullable = false)
    @NotBlank
    private String director;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Genre genre;
    @Column(nullable = false)
    @Past
    private LocalDate productionDate;
    @Column(nullable = false)
    @Past
    private LocalDate releaseDate;
    //@Column(columnDefinition = "TEXT") //Specific to postgresql
    private String description;
    @Min(0)
    @Max(5)
    private Double rating;
    private String fromBook;
    @ManyToMany
    @JoinTable(
            name = "Acting",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<Actor> actors;
}
