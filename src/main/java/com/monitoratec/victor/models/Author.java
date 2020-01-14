package com.monitoratec.victor.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "authors")
public class Author {
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

    public Author() {}

    public Author(@NotEmpty String firstName, @NotEmpty String lastName, @NotNull @Past LocalDate birthdate, @NotNull boolean distinguished) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.distinguished = distinguished;
    }

    public Author(int id, @NotEmpty String firstName, @NotEmpty String lastName, @NotNull @Past LocalDate birthdate, @NotNull boolean distinguished) {
        this(firstName, lastName, birthdate, distinguished);
        this.id = id;
    }
}
