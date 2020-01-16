package com.monitoratec.victor.models.dto;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.IVersioned;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AuthorDTO implements IVersioned {
    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthdate;
    private final boolean distinguished;
    private final Set<BookDTO> books = new HashSet<>();

    @Override
    public IVersioned toVersion(int version) {
        if (version <= 1) {
            return new AuthorDTOv1(
                id, firstName, lastName, birthdate, distinguished
            ).toVersion(version);
        }

        return this;
    }

    public static AuthorDTO fromAuthor(Author author, boolean includeChildren) {
        AuthorDTO dto = new AuthorDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthdate(),
                author.isDistinguished()
        );

        if (includeChildren) {
            Collection<BookDTO> bookDtos = BookDTO.fromBooks(author.getBooks(), false);
            dto.getBooks().addAll(bookDtos);
        }

        return dto;
    }

    public static Collection<AuthorDTO> fromAuthors(Collection<Author> authors, boolean includeChildren) {
        return authors
                .stream()
                .map((Author author) -> AuthorDTO.fromAuthor(author, includeChildren))
                .collect(Collectors.toList());
    }

    public static Author toAuthor(AuthorDTO dto, boolean includeChildren) {
        Author author = Author.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthdate(dto.getBirthdate())
                .distinguished(dto.isDistinguished())
                .build();

        if (includeChildren) {
            author.getBooks().addAll(BookDTO.toBooks(dto.getBooks(), false));
        }

        return author;
    }

    public static Collection<Author> toAuthors(Collection<AuthorDTO> authors, boolean includeChildren) {
        return authors
                .stream()
                .map((AuthorDTO author) -> AuthorDTO.toAuthor(author, includeChildren))
                .collect(Collectors.toList());
    }
}
