package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.BookDTO;
import com.monitoratec.victor.services.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private BookService bookService;

    private Book book;
    private BookDTO dto;

    @Before
    public void setup() {
        LocalDate date = LocalDate.of(1995, 9, 2);
        this.book = Book.builder()
                .id(100)
                .title("Some book")
                .publisherName("Of someone")
                .publishDate(date)
                .language(Book.BookLanguage.RUSSIAN)
                .build();

        this.dto = BookDTO.fromBook(this.book, true);
    }

    @Test
    public void list_Should_Return200_When_ValidRequest() {
        BookDTO[] bookDtos = {
            new BookDTO(10, "B1", "Publisher 01", LocalDate.of(1980, 10, 10), Book.BookLanguage.ITALIAN),
            new BookDTO(11, "B2", "Publisher 02", LocalDate.of(1970, 1, 30), Book.BookLanguage.ENGLISH),
            new BookDTO(12, "B3", "Publisher 03", LocalDate.of(1990, 5, 20), Book.BookLanguage.PORTUGUESE)
        };
        List<Book> books = new ArrayList<>(BookDTO.toBooks(Arrays.asList(bookDtos), true));

        BDDMockito
                .when(this.bookService.list())
                .thenReturn(books);
        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("/v1/books", BookDTO[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(bookDtos);
    }

    @Test
    public void one_Should_Return200_When_ValidRequest() {
        BDDMockito
                .when(this.bookService.one(this.book.getId()))
                .thenReturn(Optional.of(this.book));
        ResponseEntity<BookDTO> response = restTemplate.getForEntity("/v1/books/{id}", BookDTO.class, this.book.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(this.dto);
    }

    @Test
    public void one_Should_Return404_When_NotFound() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/v1/books/{id}", Void.class, -1);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void create_Should_Return201_When_ValidRequest() {
        BDDMockito
                .when(this.bookService.save(this.dto, null))
                .thenReturn(this.book);
        ResponseEntity<BookDTO> response = restTemplate.postForEntity("/v1/books", this.dto, BookDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(this.dto);
    }

    @Test
    public void create_Should_Return400_When_BadPayload() {
        BDDMockito
                .when(this.bookService.save(this.dto, null))
                .thenThrow(new ConstraintViolationException(Collections.emptySet()));
        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/books", this.dto, Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void update_Should_Return204_When_ValidRequest() {
        BookDTO updatedDto = new BookDTO(
                null,
                "New book title",
                "Updated publisher",
                this.dto.getPublishDate(),
                this.dto.getLanguage()
        );

        BDDMockito
                .when(this.bookService.exists(this.book.getId()))
                .thenReturn(true);
        ResponseEntity<Void> response = restTemplate
                .exchange("/v1/books/{id}", HttpMethod.PUT, new HttpEntity<>(updatedDto), Void.class, this.book.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void update_Should_Return400_When_BadPayload() {
        BDDMockito
                .when(this.bookService.exists(this.book.getId()))
                .thenReturn(true);
        BDDMockito
                .when(this.bookService.save(this.dto, this.book.getId()))
                .thenThrow(new ConstraintViolationException(Collections.emptySet()));
        ResponseEntity<Void> response = restTemplate
                .exchange("/v1/books/{id}", HttpMethod.PUT, new HttpEntity<>(this.dto), Void.class, this.book.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void update_Should_Return404_When_NotFound() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/v1/books/{id}", HttpMethod.PUT, new HttpEntity<>(this.dto), Void.class, -1);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void delete_Should_Return204() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/v1/books/{id}", HttpMethod.DELETE, null, Void.class, this.book.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}
