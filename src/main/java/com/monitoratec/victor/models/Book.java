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
@Entity(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String publisherName;
    @Past
    @NotNull
    private LocalDate publishDate;
    @Column(columnDefinition = "ENUM('ENGLISH', 'PORTUGUESE', 'ITALIAN', 'RUSSIAN')")
    @Enumerated(EnumType.STRING)
    @NotNull
    private BookLanguage language;
    @ManyToMany(mappedBy = "books")
    private Set<AuthorV2> authors = new HashSet<>();

    public Book() {}

    public Book(@NotEmpty String title, @NotEmpty String publisherName, @NotNull @Past LocalDate publishDate, @NotNull BookLanguage language) {
        this.title = title;
        this.publisherName = publisherName;
        this.publishDate = publishDate;
        this.language = language;
    }

    public Book(int id, @NotEmpty String title, @NotEmpty String publisherName, @NotNull @Past LocalDate publishDate, @NotNull BookLanguage language) {
        this(title, publisherName, publishDate, language);
        this.id = id;
    }

    public enum BookLanguage {
        ENGLISH,
        PORTUGUESE,
        ITALIAN,
        RUSSIAN
    }
}
