package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.BookDTO;
import com.monitoratec.victor.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/books")
@SuppressWarnings("unused")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookDTO> listAll() {
        log.info("[Request] List of books");
        List<Book> books = this.bookService.list();

        return BookDTO.fromBooks(books, true);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO one(@PathVariable Integer id) {
        log.info("[Request] Find book by id: " + id);
        Optional<Book> book = this.bookService.one(id);
        if (!book.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return BookDTO.fromBook(book.get(), true);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@Valid @RequestBody BookDTO bookDto) {
        log.info("[Request] Create book:" + bookDto);
        Book book = this.bookService.save(bookDto, null);

        return BookDTO.fromBook(book, true);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody BookDTO bookDto) {
        if (!this.bookService.exists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("[Request] Update book: " + id + " with: " + bookDto);
        this.bookService.save(bookDto, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("[Request] Delete book:" + id);
        this.bookService.delete(id);
    }
}
