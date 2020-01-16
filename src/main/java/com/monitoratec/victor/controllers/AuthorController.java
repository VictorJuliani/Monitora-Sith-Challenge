package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.AuthorDTO;
import com.monitoratec.victor.models.dto.BookDTO;
import com.monitoratec.victor.services.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController()
@SuppressWarnings("unused")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    public void log(String msg) {
        log.info("[Authors][Request]: " + msg);
    }

    @GetMapping({
        "/v2/authors",
        "/v1/authors"
    })
    @ResponseStatus(HttpStatus.OK)
    public Collection<AuthorDTO> listAll() {
        this.log("List all");
        List<Author> authors = this.authorService.list();
        return AuthorDTO.fromAuthors(authors, true);
    }

    @GetMapping({
        "/v2/authors/{id}",
        "/v1/authors/{id}"
    })
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO one(@PathVariable Integer id) {
        this.log("Find by id: " + id);
        Optional<Author> author = this.authorService.one(id);
        if (!author.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return AuthorDTO.fromAuthor(author.get(), true);
    }

    @PostMapping({
        "/v2/authors",
        "/v1/authors"
    })
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO create(@Valid @RequestBody AuthorDTO authorDto) {
        this.log("Create:" + authorDto);
        Author author = this.authorService.save(authorDto, null);

        return AuthorDTO.fromAuthor(author, true);
    }

    @PutMapping({
        "/v2/authors/{id}",
        "/v1/authors/{id}"
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody AuthorDTO authorDto) {
        if (!this.authorService.exists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        this.log("Update: " + id + " with: " + authorDto);
        this.authorService.save(authorDto, id);
    }

    @DeleteMapping({
        "/v2/authors/{id}",
        "/v1/authors/{id}"
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        this.log("Delete:" + id);
        this.authorService.delete(id);
    }

    @GetMapping({
        "/v2/authors/{id}/books"
    })
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookDTO> getBooks(@PathVariable Integer id) {
        this.log("Find books by author " + id);
        Optional<Author> author = this.authorService.one(id);
        if (!author.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<Book> books = author.get().getBooks();
        return BookDTO.fromBooks(books, true);
    }
}
