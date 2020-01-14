package com.monitoratec.victor.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "authors")
public class AuthorV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull @Past
    private LocalDate birthdate;
    @NotNull
    private boolean distinguished;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "author_book",
            joinColumns = { @JoinColumn(name = "author_id") },
            inverseJoinColumns = { @JoinColumn(name = "book_id") }
    )
    private Set<Book> books = new HashSet<>();

    public AuthorV2() {}

    public AuthorV2(@NotEmpty String firstName, @NotEmpty String lastName, @NotNull @Past LocalDate birthdate, @NotNull boolean distinguished) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.distinguished = distinguished;
    }

    public AuthorV2(int id, @NotEmpty String firstName, @NotEmpty String lastName, @NotNull @Past LocalDate birthdate, @NotNull boolean distinguished) {
        this(firstName, lastName, birthdate, distinguished);
        this.id = id;
    }
}
