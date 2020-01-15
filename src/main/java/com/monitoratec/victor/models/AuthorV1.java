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
public class AuthorV1 extends Author {
    public AuthorV1() {}

    public AuthorV1(@NotEmpty String firstName, @NotEmpty String lastName, @NotNull @Past LocalDate birthdate, @NotNull boolean distinguished) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.distinguished = distinguished;
    }

    public AuthorV1(int id, @NotEmpty String firstName, @NotEmpty String lastName, @NotNull @Past LocalDate birthdate, @NotNull boolean distinguished) {
        this(firstName, lastName, birthdate, distinguished);
        this.id = id;
    }
}
