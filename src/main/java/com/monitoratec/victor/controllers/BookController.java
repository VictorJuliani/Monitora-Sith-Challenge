package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@SuppressWarnings("unused")
public class BookController {
    @Autowired
    private BookRepository bookDAO;

    @GetMapping
    public ResponseEntity<?> listAll() {
        log.info("[Request] List of books");
        return new ResponseEntity<>(bookDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> one(@PathVariable Integer id) {
        log.info("[Request] Find book by id: " + id);
        Optional<Book> book = bookDAO.findById(id);
        if (!book.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Book book) {
        log.info("[Request] Create book:" + book);
        bookDAO.save(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Book book) {
        if (!bookDAO.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("[Request] Update book: " + id + " with: " + book);

        bookDAO.save(book);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("[Request] Delete book:" + id);
        bookDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
