package com.monitoratec.victor.models.dto;

import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.IVersioned;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BookDTO implements IVersioned {
    private final int id;
    private final String title;
    private final String publisherName;
    private final LocalDate publishDate;
    private final Book.BookLanguage language;
    private final Set<AuthorDTO> authors = new HashSet<>();

    @Override
    public IVersioned toVersion(int version) {
        return this;
    }

    public static BookDTO fromBook(Book book, boolean includeChildren) {
        BookDTO dto = new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getPublisherName(),
                book.getPublishDate(),
                book.getLanguage()
        );

        if (includeChildren) {
            dto.getAuthors().addAll(AuthorDTO.fromAuthors(book.getAuthors(), false));
        }

        return dto;
    }

    public static Collection<BookDTO> fromBooks(Collection<Book> books, boolean includeChildren) {
        return books
                .stream()
                .map((Book book) -> BookDTO.fromBook(book, includeChildren))
                .collect(Collectors.toList());
    }

    public static Book toBook(BookDTO dto, boolean includeChildren) {
        Book book = Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .publisherName(dto.getPublisherName())
                .publishDate(dto.getPublishDate())
                .language(dto.getLanguage())
                .build();

        if (includeChildren) {
            book.getAuthors().addAll(AuthorDTO.toAuthors(dto.getAuthors(), false));
        }

        return book;
    }

    public static Collection<Book> toBooks(Collection<BookDTO> books, boolean includeChildren) {
        return books
                .stream()
                .map((BookDTO author) -> BookDTO.toBook(author, includeChildren))
                .collect(Collectors.toList());
    }
}
