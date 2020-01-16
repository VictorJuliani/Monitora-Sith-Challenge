package com.monitoratec.victor.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Book.BookLanguage language;
    @ManyToMany(mappedBy = "books")
    private final Set<Author> authors = new HashSet<>();

    public enum BookLanguage {
        ENGLISH,
        PORTUGUESE,
        ITALIAN,
        RUSSIAN
    }
}
