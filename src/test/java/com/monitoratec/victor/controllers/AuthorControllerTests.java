package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.repositories.AuthorRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private AuthorRepository authorRepository;

    private Author author;

    @Before
    public void setup() {
        LocalDate defaultBirth = LocalDate.of(1995, 9, 2);
        author = new Author();
        author.setId(1);
        author.setFirstName("Victor");
        author.setLastName("Juliani");
        author.setDistinguished(true);
        author.setBirthdate(defaultBirth);
    }

    @Test
    public void list_Should_Return200_When_ValidRequest() {
        Author[] authors = {
            new Author(10, "John", "Doe", LocalDate.of(1980, 10, 10), true),
            new Author(11, "Jane", "Lorem", LocalDate.of(1970, 1, 30), false),
            new Author(12, "Jack", "Johnson", LocalDate.of(1990, 5, 20), true)
        };

        BDDMockito.when(this.authorRepository.findAll()).thenReturn(Arrays.asList(authors));
        ResponseEntity<Author[]> response = restTemplate.getForEntity("/authors/", Author[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(authors).containsExactly(response.getBody());
    }

    @Test
    public void one_Should_Return200_When_ValidRequest() {
        BDDMockito.when(this.authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        ResponseEntity<Author> response = restTemplate.getForEntity("/authors/{id}", Author.class, this.author.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(this.author).isEqualTo(response.getBody());
    }

    @Test
    public void one_Should_Return404_When_NotFound() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/authors/{id}", Void.class, 100);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void create_Should_Return201_When_ValidRequest() {
        BDDMockito.when(this.authorRepository.save(this.author)).thenReturn(this.author);
        ResponseEntity<Author> response = restTemplate.postForEntity("/authors/", this.author, Author.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(this.author);
    }

    @Test
    public void create_Should_Return400_When_BadPayload() {
        Author badAuthor = new Author();
        ResponseEntity<Void> response = restTemplate.postForEntity("/authors/", badAuthor, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void update_Should_Return204_When_ValidRequest() {
        this.author.setFirstName("New name");
        this.author.setLastName("New surname");
        BDDMockito.when(this.authorRepository.existsById(this.author.getId())).thenReturn(true);
        ResponseEntity<Void> response = restTemplate
            .exchange("/authors/{id}", HttpMethod.PUT, new HttpEntity<>(author), Void.class, this.author.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void update_Should_Return400_When_BadPayload() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/authors/{id}", HttpMethod.PUT, new HttpEntity<>(new Author()), Void.class, this.author.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void update_Should_Return404_When_NotFound() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/authors/{id}", HttpMethod.PUT, new HttpEntity<>(this.author), Void.class, -1);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void delete_Should_Return204() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/authors/{id}", HttpMethod.DELETE, null, Void.class, this.author.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}
