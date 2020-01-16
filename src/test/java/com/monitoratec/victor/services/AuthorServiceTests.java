package com.monitoratec.victor.services;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.AuthorDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureTestEntityManager
public class AuthorServiceTest {
    @Autowired
    private AuthorService service;
    @Autowired
    private TestEntityManager entityManager;

    private Author author;
    private LocalDate defaultDate;

    @Before
    public void setup() {
        LocalDate defaultDate = LocalDate.of(1995, 9, 2);
        this.defaultDate = defaultDate;
        this.author = Author.builder()
                .firstName("Victor")
                .lastName("Juliani")
                .birthdate(defaultDate)
                .distinguished(true)
                .build();
    }

    @Test
    public void save_Should_PersistAuthor() {
        this.service.save(author);

        assertThat(author.getId()).isNotNull();
        assertThat(author.getFirstName()).isEqualTo("Victor");
        assertThat(author.getLastName()).isEqualTo("Juliani");
        assertThat(author.isDistinguished()).isTrue();
        assertThat(author.getBirthdate()).isEqualTo(defaultDate);
    }

    @Test
    public void save_Should_PersistAuthorDTO() {
        AuthorDTO dto = new AuthorDTO(
                0,
                "Victor",
                "Juliani",
                defaultDate,
                true
        );

        Author newAuthor = this.service.save(dto, 0);
        assertThat(newAuthor.getId()).isNotNull();
        assertThat(newAuthor.getFirstName()).isEqualTo("Victor");
        assertThat(newAuthor.getLastName()).isEqualTo("Juliani");
        assertThat(newAuthor.isDistinguished()).isTrue();
        assertThat(newAuthor.getBirthdate()).isEqualTo(defaultDate);
    }

    @Test
    public void save_Should_PersistBooks() {
        Book book = Book.builder()
                .title("Sample")
                .publisherName("Publisher")
                .publishDate(defaultDate)
                .language(Book.BookLanguage.RUSSIAN)
                .build();

        author.getBooks().add(book);
        this.service.save(author);

        //noinspection OptionalGetWithoutIsPresent
        Author authorWithBooks = this.service.one(author.getId()).get();
        assertThat(authorWithBooks.getBooks()).hasSize(1);

        Book authorsBook = authorWithBooks.getBooks().iterator().next();
        assertThat(authorsBook.getId()).isNotNull();
        assertThat(authorsBook.getTitle()).isEqualTo("Sample");
        assertThat(authorsBook.getPublisherName()).isEqualTo("Publisher");
        assertThat(authorsBook.getPublishDate()).isEqualTo(defaultDate);
        assertThat(authorsBook.getLanguage()).isEqualTo(Book.BookLanguage.RUSSIAN);
    }

    @Test
    public void one_Should_RecoverAuthor() {
        this.service.save(author);

        //noinspection OptionalGetWithoutIsPresent
        Author oneAuthor = this.service.one(author.getId()).get();
        assertThat(oneAuthor.getId()).isEqualTo(author.getId());
        assertThat(oneAuthor.getFirstName()).isEqualTo(author.getFirstName());
        assertThat(oneAuthor.getLastName()).isEqualTo(author.getLastName());
        assertThat(oneAuthor.isDistinguished()).isEqualTo(author.isDistinguished());
        assertThat(oneAuthor.getBirthdate()).isEqualTo(author.getBirthdate());
    }

    @Test
    public void update_Should_ChangeAndPersistAuthor() {
        LocalDate newBirth = LocalDate.of(2000, 1, 10);
        this.service.save(author);

        author.setFirstName("John");
        author.setLastName("Doe");
        author.setDistinguished(false);
        author.setBirthdate(newBirth);

        this.service.save(author);

        //noinspection OptionalGetWithoutIsPresent
        Author newAuthor = this.service.one(author.getId()).get();

        assertThat(newAuthor.getId()).isNotNull();
        assertThat(newAuthor.getFirstName()).isEqualTo("John");
        assertThat(newAuthor.getLastName()).isEqualTo("Doe");
        assertThat(newAuthor.isDistinguished()).isFalse();
        assertThat(newAuthor.getBirthdate()).isEqualTo(newBirth);
    }

    @Test
    public void delete_Should_RemoveAuthor() {
        this.service.save(author);
        this.service.delete(author.getId());

        Optional<Author> deletedAuthor = this.service.one(author.getId());
        assertThat(deletedAuthor.isPresent()).isFalse();
    }

    @Test
    public void exists_Should_ReturnTrue_When_EntityExists() {
        author.setId(1000);
        assertThat(this.service.exists(author.getId())).isFalse();

        author = this.service.save(author);
        assertThat(this.service.exists(author.getId())).isTrue();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequireFirstName() {
        author.setFirstName(null);
        this.service.save(author);
        this.entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequireLastName() {
        author.setLastName(null);
        this.service.save(author);
        this.entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequireBirthdate() {
        author.setBirthdate(null);
        this.service.save(author);
        this.entityManager.flush();
    }
}
