//package com.monitoratec.victor.services.repositories;
//
//import com.monitoratec.victor.models.dto.old.Author;
//import com.monitoratec.victor.models.dto.old.AuthorV1;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.validation.ConstraintViolationException;
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class AuthorRepositoryTest {
//    @Autowired
//    private AuthorRepository repository;
//    @Autowired
//    private TestEntityManager entityManager;
//
//    private AuthorV1 author;
//    private LocalDate defaultBirth;
//
//    @Before
//    public void setup() {
//        LocalDate defaultBirth = LocalDate.of(1995, 9, 2);
//        AuthorV1 author = new AuthorV1();
//        author.setFirstName("Victor");
//        author.setLastName("Juliani");
//        author.setDistinguished(true);
//        author.setBirthdate(defaultBirth);
//
//        this.author = author;
//        this.defaultBirth = defaultBirth;
//    }
//
//    @Test
//    public void save_Should_PersistAuthor() {
//        this.repository.save(author);
//
//        assertThat(author.getId()).isNotNull();
//        assertThat(author.getFirstName()).isEqualTo("Victor");
//        assertThat(author.getLastName()).isEqualTo("Juliani");
//        assertThat(author.isDistinguished()).isTrue();
//        assertThat(author.getBirthdate()).isEqualTo(defaultBirth);
//    }
//
//    @Test
//    public void delete_Should_RemoveAuthor() {
//        this.repository.save(author);
//        this.repository.deleteById(author.getId());
//
//        Optional<AuthorV1> deletedAuthor = this.repository.findById(author.getId());
//        assertThat(deletedAuthor.isPresent()).isFalse();
//    }
//
//    @Test
//    public void update_Should_ChangeAndPersistAuthor() {
//        LocalDate newBirth = LocalDate.of(2000, 1, 10);
//        this.repository.save(author);
//
//        author.setFirstName("John");
//        author.setLastName("Doe");
//        author.setDistinguished(false);
//        author.setBirthdate(newBirth);
//
//        this.repository.save(author);
//
//        //noinspection OptionalGetWithoutIsPresent
//        Author newAuthor = this.repository.findById(author.getId()).get();
//
//        assertThat(newAuthor.getId()).isNotNull();
//        assertThat(newAuthor.getFirstName()).isEqualTo("John");
//        assertThat(newAuthor.getLastName()).isEqualTo("Doe");
//        assertThat(newAuthor.isDistinguished()).isFalse();
//        assertThat(newAuthor.getBirthdate()).isEqualTo(newBirth);
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void save_Should_RequireFirstName() {
//        author.setFirstName(null);
//        this.repository.save(author);
//        this.entityManager.flush();
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void save_Should_RequireLastName() {
//        author.setLastName(null);
//        this.repository.save(author);
//        this.entityManager.flush();
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void save_Should_RequireBirthdate() {
//        author.setBirthdate(null);
//        this.repository.save(author);
//        this.entityManager.flush();
//    }
//}
