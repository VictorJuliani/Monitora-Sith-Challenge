package com.monitoratec.victor.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@MappedSuperclass
public abstract class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @NotEmpty
    protected String firstName;
    @NotEmpty
    protected String lastName;
    @NotNull @Past
    protected LocalDate birthdate;
    @NotNull
    protected boolean distinguished;
}
