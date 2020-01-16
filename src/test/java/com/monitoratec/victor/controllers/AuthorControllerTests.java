package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.AuthorDTO;
import com.monitoratec.victor.models.dto.AuthorDTOv1;
import com.monitoratec.victor.models.dto.BookDTO;
import com.monitoratec.victor.services.AuthorService;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private AuthorService authorService;

    private Author author;
    private AuthorDTO dto;
    private AuthorDTOv1 dtoV1;

    @Before
    public void setup() {
        LocalDate date = LocalDate.of(1995, 9, 2);
        this.author = Author
                .builder()
                .id(1)
                .firstName("Victor")
                .lastName("Juliani")
                .birthdate(date)
                .distinguished(true)
                .build();

        this.dto = AuthorDTO.fromAuthor(this.author, true);
        this.dtoV1 = (AuthorDTOv1) this.dto.toVersion(1);
    }

    @Test
    public void list_Should_Return200_When_ValidRequest() {
        List<AuthorDTO> authorDtos = Arrays.asList(
            new AuthorDTO(10, "John", "Doe", LocalDate.of(1980, 10, 10), true),
            new AuthorDTO(11, "Jane", "Lorem", LocalDate.of(1970, 1, 30), false),
            new AuthorDTO(12, "Jack", "Johnson", LocalDate.of(1990, 5, 20), true)
        );
        List<Author> authors = new ArrayList<>(AuthorDTO.toAuthors(authorDtos, true));
        BDDMockito
                .when(this.authorService.list())
                .thenReturn(authors);

        // v2 checking
        ResponseEntity<AuthorDTO[]> response = restTemplate.getForEntity("/v2/authors", AuthorDTO[].class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(authorDtos.toArray(new AuthorDTO[0]));

        // v1 checking
        List<AuthorDTOv1> authorDtosV1 = authorDtos
                .stream()
                .map(dto -> (AuthorDTOv1) dto.toVersion(1))
                .collect(Collectors.toList());
        ResponseEntity<AuthorDTOv1[]> responseV1 = restTemplate.getForEntity("/v1/authors", AuthorDTOv1[].class);
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseV1.getBody()).containsExactly(authorDtosV1.toArray(new AuthorDTOv1[0]));
    }

    @Test
    public void one_Should_Return200_When_ValidRequest() {
        BDDMockito.when(this.authorService.one(this.author.getId())).thenReturn(Optional.of(this.author));

        // v2 checking
        ResponseEntity<AuthorDTO> response = restTemplate.getForEntity("/v2/authors/{id}", AuthorDTO.class, this.author.getId());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(this.dto);

        // v1 checking
        ResponseEntity<AuthorDTOv1> responseV1 = restTemplate.getForEntity("/v1/authors/{id}", AuthorDTOv1.class, this.author.getId());
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseV1.getBody()).isEqualTo(this.dtoV1);
    }

    @Test
    public void one_Should_Return404_When_NotFound() {
        // v2 checking
        ResponseEntity<Void> response = restTemplate.getForEntity("/v2/authors/{id}", Void.class, -1);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);

        // v1 checking
        ResponseEntity<Void> responseV1 = restTemplate.getForEntity("/v1/authors/{id}", Void.class, -1);
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void create_Should_Return201_When_ValidRequest() {
        BDDMockito
                .when(this.authorService.save(this.dto, null))
                .thenReturn(this.author);

        // v2 checking
        ResponseEntity<AuthorDTO> response = restTemplate.postForEntity("/v2/authors/", this.dto, AuthorDTO.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(this.dto);

        // v1 checking
        ResponseEntity<AuthorDTOv1> responseV1 = restTemplate.postForEntity("/v1/authors/", this.dtoV1, AuthorDTOv1.class);
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseV1.getBody()).isEqualTo(this.dtoV1);
    }

    @Test
    public void create_Should_Return400_When_BadPayload() {
        BDDMockito
                .when(this.authorService.save(this.dto, null))
                .thenThrow(new ConstraintViolationException(Collections.emptySet()));

        // v2 checking
        ResponseEntity<Void> response = restTemplate.postForEntity("/v2/authors/", this.dto, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        // v1 checking
        ResponseEntity<Void> responseV1 = restTemplate.postForEntity("/v1/authors/", this.dto, Void.class);
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void update_Should_Return204_When_ValidRequest() {
        AuthorDTO updateDto = new AuthorDTO(
                null,
                "New name",
                "New surname",
                this.dto.getBirthdate(),
                this.dto.isDistinguished()
        );

        BDDMockito
                .when(this.authorService.exists(this.author.getId()))
                .thenReturn(true);

        // v2 checking
        ResponseEntity<Void> response = restTemplate
            .exchange("/v2/authors/{id}", HttpMethod.PUT, new HttpEntity<>(updateDto), Void.class, this.author.getId());
        assertThat(response.getStatusCodeValue()).isEqualTo(204);

        // v2 checking
        ResponseEntity<Void> responseV1 = restTemplate
                .exchange("/v1/authors/{id}", HttpMethod.PUT, new HttpEntity<>(updateDto.toVersion(1)), Void.class, this.author.getId());
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void update_Should_Return400_When_BadPayload() {
        BDDMockito
                .when(this.authorService.exists(this.author.getId()))
                .thenReturn(true);
        BDDMockito
                .when(this.authorService.save(this.dto, this.author.getId()))
                .thenThrow(new ConstraintViolationException(Collections.emptySet()));

        // v2 checking
        ResponseEntity<Void> response = restTemplate
                .exchange("/v2/authors/{id}", HttpMethod.PUT, new HttpEntity<>(this.dto), Void.class, this.author.getId());
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        // v2 checking
        ResponseEntity<Void> responseV1 = restTemplate
                .exchange("/v1/authors/{id}", HttpMethod.PUT, new HttpEntity<>(this.dto), Void.class, this.author.getId());
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void update_Should_Return404_When_NotFound() {
        // v2 checking
        ResponseEntity<Void> response = restTemplate
                .exchange("/v2/authors/{id}", HttpMethod.PUT, new HttpEntity<>(this.dto), Void.class, -1);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);

        // v1 checking
        ResponseEntity<Void> responseV1 = restTemplate
                .exchange("/v1/authors/{id}", HttpMethod.PUT, new HttpEntity<>(this.dto), Void.class, -1);
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void delete_Should_Return204() {
        // v2 checking
        ResponseEntity<Void> response = restTemplate
                .exchange("/v2/authors/{id}", HttpMethod.DELETE, null, Void.class, this.author.getId());
        assertThat(response.getStatusCodeValue()).isEqualTo(204);

        // v1 checking
        ResponseEntity<Void> responseV1 = restTemplate
                .exchange("/v1/authors/{id}", HttpMethod.DELETE, null, Void.class, this.author.getId());
        assertThat(responseV1.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void getBooks_Should_Return200_When_ValidRequest() {
        Book book = Book
                .builder()
                .id(10)
                .title("Author book")
                .publisherName("By someone")
                .publishDate(LocalDate.of(1950, 10, 10))
                .language(Book.BookLanguage.ENGLISH)
                .build();

        this.author.getBooks().add(book);
        AuthorDTO authorDtoWithBook = AuthorDTO.fromAuthor(this.author, true);

        BDDMockito
                .when(this.authorService.one(this.author.getId()))
                .thenReturn(Optional.of(this.author));

        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("/v2/authors/{id}/books", BookDTO[].class, authorDtoWithBook.getId());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(authorDtoWithBook.getBooks().toArray(new BookDTO[0]));
    }

    @Test
    public void getBooks_Should_Return404_When_NotFound() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/v2/authors/{id}/books", Void.class, -1);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);

    }
}
