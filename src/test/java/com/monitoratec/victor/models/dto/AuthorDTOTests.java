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

public class AuthorDTOTests {
    private final LocalDate date = LocalDate.of(2000, 10, 10);
    private Author.AuthorBuilder authorBuilder;
    private Book.BookBuilder bookBuilder;

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
    public void toVersion2_Should_ReturnSelf() {
        Author author = this.authorBuilder.build();
        AuthorDTO dto = AuthorDTO.fromAuthor(author, false);
        IVersioned v2Dto = dto.toVersion(2);

        assertThat(v2Dto).isEqualTo(dto);
    }

    @Test
    public void toVersion1_Should_ReturnV1() {
        Author author = this.authorBuilder.build();
        AuthorDTO dto = AuthorDTO.fromAuthor(author, false);
        IVersioned v1Dto = dto.toVersion(1);
        AuthorDTOv1 expectedDto = new AuthorDTOv1(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthdate(),
                author.isDistinguished()
        );

        assertThat(v1Dto).isEqualTo(expectedDto);
    }

    @Test
    public void fromAuthor_Should_MapToDTO() {
        Author author = this.authorBuilder.build();
        Book book = this.bookBuilder.build();
        author.getBooks().add(book);

        AuthorDTO dto = AuthorDTO.fromAuthor(author, true);

        assertThat(dto.getFirstName()).isEqualTo("Author");
        assertThat(dto.getLastName()).isEqualTo("Surname");
        assertThat(dto.getBirthdate()).isEqualTo(date);
        assertThat(dto.isDistinguished()).isEqualTo(true);
        assertThat(dto.getBooks().size()).isEqualTo(1);

        dto = AuthorDTO.fromAuthor(author, false);
        assertThat(dto.getBooks().size()).isZero();
    }

    @Test
    public void fromAuthors_Should_MapToDTOCollection() {
        Book book = this.bookBuilder.build();
        Author author1 = this.authorBuilder.build();
        Author author2 = this.authorBuilder.build();

        author1.getBooks().add(book);

        Collection<AuthorDTO> dtos = AuthorDTO.fromAuthors(Arrays.asList(author1, author2), true);
        assertThat(dtos.size()).isEqualTo(2);

        Iterator<AuthorDTO> it = dtos.iterator();
        assertThat(it.next().getBooks().size()).isEqualTo(1);
        assertThat(it.next().getBooks().size()).isZero();

        dtos = AuthorDTO.fromAuthors(Arrays.asList(author1, author2), false);
        assertThat(dtos.size()).isEqualTo(2);

        it = dtos.iterator();
        assertThat(it.next().getBooks().size()).isZero();
        assertThat(it.next().getBooks().size()).isZero();
    }

    @Test
    public void toAuthor_Should_MapFromDTO() {
        Author author = this.authorBuilder.build();
        Book book = this.bookBuilder.build();
        author.getBooks().add(book);

        AuthorDTO dto = AuthorDTO.fromAuthor(author, true);
        Author authorFromDto = AuthorDTO.toAuthor(dto, true);

        assertThat(authorFromDto).isEqualTo(author);

        dto = AuthorDTO.fromAuthor(author, false);
        authorFromDto = AuthorDTO.toAuthor(dto, false);
        author.getBooks().clear();

        assertThat(authorFromDto).isEqualTo(author);
    }

    @Test
    public void toAuthors_Should_MapFromDTOCollection() {
        Author author1 = this.authorBuilder.build();
        Author author2 = this.authorBuilder.build();
        Book book = this.bookBuilder.build();
        author1.getBooks().add(book);

        Collection<AuthorDTO> dtos = AuthorDTO.fromAuthors(Arrays.asList(author1, author2), true);
        Collection<Author> authorsFromDtos = AuthorDTO.toAuthors(dtos, true);

        assertThat(authorsFromDtos).isEqualTo(Arrays.asList(author1, author2));

        dtos = AuthorDTO.fromAuthors(Arrays.asList(author1, author2), false);
        authorsFromDtos = AuthorDTO.toAuthors(dtos, false);
        author1.getBooks().clear();

        assertThat(authorsFromDtos).isEqualTo(Arrays.asList(author1, author2));
    }
}
