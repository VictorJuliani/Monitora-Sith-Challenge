package com.monitoratec.victor.services;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.BookDTO;
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
public class BookServiceTest {
    @Autowired
    private BookService service;
    @Autowired
    private TestEntityManager entityManager;

    private Book book;
    private LocalDate defaultDate;

    @Before
    public void setup() {
        LocalDate defaultDate = LocalDate.of(1995, 9, 2);
        this.defaultDate = defaultDate;
        this.book = Book.builder()
                .title("My book")
                .publisherName("My publisher")
                .publishDate(defaultDate)
                .language(Book.BookLanguage.ENGLISH)
                .build();
    }

    @Test
    public void save_Should_PersistBook() {
        this.service.save(book);

        assertThat(book.getId()).isNotNull();
        assertThat(book.getTitle()).isEqualTo("My book");
        assertThat(book.getPublisherName()).isEqualTo("My publisher");
        assertThat(book.getPublishDate()).isEqualTo(defaultDate);
        assertThat(book.getLanguage()).isEqualTo(Book.BookLanguage.ENGLISH);
    }

    @Test
    public void save_Should_PersistBookDTO() {
        BookDTO dto = new BookDTO(
                0,
                "New book",
                "Another publisher",
                defaultDate,
                Book.BookLanguage.PORTUGUESE
        );

        Book newBook = this.service.save(dto, 0);
        assertThat(newBook.getId()).isNotEqualTo(0);
        assertThat(newBook.getTitle()).isEqualTo("New book");
        assertThat(newBook.getPublisherName()).isEqualTo("Another publisher");
        assertThat(newBook.getPublishDate()).isEqualTo(defaultDate);
        assertThat(newBook.getLanguage()).isEqualTo(Book.BookLanguage.PORTUGUESE);
    }

    @Test
    public void save_ShouldNot_PersistAuthors() {
        Author author = new Author();
        book.getAuthors().add(author);

        this.service.save(book);

        //noinspection OptionalGetWithoutIsPresent
        Book bookWithAuthors = this.service.one(book.getId()).get();
        assertThat(bookWithAuthors.getAuthors()).hasSize(1);

        Author booksAuthor = bookWithAuthors.getAuthors().iterator().next();
        // Author is in memory, but it's not persisted because books is the inverse side of the relationship
        assertThat(booksAuthor.getId()).isNull();
    }

    @Test
    public void one_Should_RecoverBook() {
        this.service.save(book);

        //noinspection OptionalGetWithoutIsPresent
        Book oneBook = this.service.one(book.getId()).get();
        assertThat(oneBook.getId()).isEqualTo(book.getId());
        assertThat(oneBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(oneBook.getPublisherName()).isEqualTo(book.getPublisherName());
        assertThat(oneBook.getPublishDate()).isEqualTo(book.getPublishDate());
        assertThat(oneBook.getLanguage()).isEqualTo(book.getLanguage());
    }

    @Test
    public void update_Should_ChangeAndPersistBook() {
        LocalDate newBirth = LocalDate.of(2000, 1, 10);
        this.service.save(book);

        book.setTitle("John");
        book.setPublisherName("Doe");
        book.setPublishDate(newBirth);
        book.setLanguage(Book.BookLanguage.ITALIAN);

        this.service.save(book);

        //noinspection OptionalGetWithoutIsPresent
        Book newBook = this.service.one(book.getId()).get();

        assertThat(newBook.getId()).isNotNull();
        assertThat(newBook.getTitle()).isEqualTo("John");
        assertThat(newBook.getPublisherName()).isEqualTo("Doe");
        assertThat(newBook.getPublishDate()).isEqualTo(newBirth);
        assertThat(newBook.getLanguage()).isEqualTo(Book.BookLanguage.ITALIAN);
    }

    @Test
    public void delete_Should_RemoveBook() {
        this.service.save(book);
        this.service.delete(book.getId());

        Optional<Book> deletedBook = this.service.one(book.getId());
        assertThat(deletedBook.isPresent()).isFalse();
    }

    @Test
    public void exists_Should_ReturnTrue_When_EntityExists() {
        book.setId(1000);
        assertThat(this.service.exists(book.getId())).isFalse();

        book = this.service.save(book);
        assertThat(this.service.exists(book.getId())).isTrue();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequireTitle() {
        book.setTitle(null);
        this.service.save(book);
        this.entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequirePublisherName() {
        book.setPublisherName(null);
        this.service.save(book);
        this.entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequirePublishDate() {
        book.setPublishDate(null);
        this.service.save(book);
        this.entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_Should_RequireLanguage() {
        book.setLanguage(null);
        this.service.save(book);
        this.entityManager.flush();
    }
}
