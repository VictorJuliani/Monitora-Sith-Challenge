package com.monitoratec.victor.models.dto;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.IVersioned;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class BookDTOTest {
    private final LocalDate date = LocalDate.of(2000, 10, 10);
    private Book.BookBuilder bookBuilder;
    private Author.AuthorBuilder authorBuilder;

    @Before
    public void setup() {
        this.authorBuilder = Author.builder()
                .firstName("Author")
                .lastName("Surname")
                .birthdate(date)
                .distinguished(true);

        this.bookBuilder = Book.builder()
                .title("My book")
                .publisherName("The publisher")
                .publishDate(date)
                .language(Book.BookLanguage.PORTUGUESE);
    }

    @Test
    public void toVersion_Should_ReturnSelf() {
        Book book = this.bookBuilder.build();
        BookDTO dto = BookDTO.fromBook(book, false);
        IVersioned v1Dto = dto.toVersion(1);

        assertThat(v1Dto).isEqualTo(dto);
    }

    @Test
    public void fromBook_Should_MapToDTO() {
        Author author = this.authorBuilder.build();
        Book book = this.bookBuilder.build();
        book.getAuthors().add(author);

        BookDTO dto = BookDTO.fromBook(book, true);

        assertThat(dto.getTitle()).isEqualTo("My book");
        assertThat(dto.getPublisherName()).isEqualTo("The publisher");
        assertThat(dto.getPublishDate()).isEqualTo(date);
        assertThat(dto.getLanguage()).isEqualTo(Book.BookLanguage.PORTUGUESE);
        assertThat(dto.getAuthors().size()).isEqualTo(1);

        dto = BookDTO.fromBook(book, false);
        assertThat(dto.getAuthors().size()).isZero();
    }

    @Test
    public void fromBooks_Should_MapToDTOCollection() {
        Author author = this.authorBuilder.build();
        Book book1 = this.bookBuilder.build();
        Book book2 = this.bookBuilder.build();

        book1.getAuthors().add(author);

        Collection<BookDTO> dtos = BookDTO.fromBooks(Arrays.asList(book1, book2), true);
        assertThat(dtos.size()).isEqualTo(2);

        Iterator<BookDTO> it = dtos.iterator();
        assertThat(it.next().getAuthors().size()).isEqualTo(1);
        assertThat(it.next().getAuthors().size()).isZero();

        dtos = BookDTO.fromBooks(Arrays.asList(book1, book2), false);
        assertThat(dtos.size()).isEqualTo(2);

        it = dtos.iterator();
        assertThat(it.next().getAuthors().size()).isZero();
        assertThat(it.next().getAuthors().size()).isZero();
    }

    @Test
    public void toBook_Should_MapFromDTO() {
        Book book = this.bookBuilder.build();
        Author author = this.authorBuilder.build();
        book.getAuthors().add(author);

        BookDTO dto = BookDTO.fromBook(book, true);
        Book bookFromDto = BookDTO.toBook(dto, true);

        assertThat(bookFromDto).isEqualTo(book);

        dto = BookDTO.fromBook(book, false);
        bookFromDto = BookDTO.toBook(dto, false);
        book.getAuthors().clear();

        assertThat(bookFromDto).isEqualTo(book);
    }

    @Test
    public void toBooks_Should_MapFromDTOCollection() {
        Book book1 = this.bookBuilder.build();
        Book book2 = this.bookBuilder.build();
        Author author = this.authorBuilder.build();
        book1.getAuthors().add(author);

        Collection<BookDTO> dtos = BookDTO.fromBooks(Arrays.asList(book1, book2), true);
        Collection<Book> booksFromDtos = BookDTO.toBooks(dtos, true);

        assertThat(booksFromDtos).isEqualTo(Arrays.asList(book1, book2));

        dtos = BookDTO.fromBooks(Arrays.asList(book1, book2), false);
        booksFromDtos = BookDTO.toBooks(dtos, false);
        book1.getAuthors().clear();

        assertThat(booksFromDtos).isEqualTo(Arrays.asList(book1, book2));
    }
}
