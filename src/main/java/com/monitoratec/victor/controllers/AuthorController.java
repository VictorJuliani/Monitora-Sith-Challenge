package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.AuthorV1;
import com.monitoratec.victor.models.AuthorV2;
import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.repositories.AuthorRepository;
import com.monitoratec.victor.repositories.IAuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SuppressWarnings("unused")
public class AuthorController {
    @RestController()
    @RequestMapping("/api/v1/authors")
    private static class V1<A extends Author> {
        protected IAuthorRepository<A> dao;

        @Autowired
        public V1(@Qualifier("v1") IAuthorRepository<A> dao) {
            this.dao = dao;
        }

        public void log(String msg) {
            log.info("[Authors][Request][" + getClass().getSimpleName() + "]: " + msg);
        }

        @GetMapping
        public ResponseEntity<?> listAll() {
            this.log("List all");
            return ResponseEntity.ok(this.dao.findAll());
        }

        @GetMapping(path = "/{id}")
        public ResponseEntity<?> one(@PathVariable Integer id) {
            this.log("Find by id: " + id);
            Optional<A> author = this.dao.findById(id);
            if (!author.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok(author.get());
        }

        @PostMapping
        public ResponseEntity<?> create(@Valid @RequestBody A author) {
            this.log("Create:" + author);
            this.dao.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(author);
        }

        @PutMapping(path = "/{id}")
        public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody A author) {
            if (!this.dao.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            this.log("Update: " + id + " with: " + author);
            this.dao.save(author);
            return ResponseEntity.noContent().build();
        }

        @DeleteMapping(path = "/{id}")
        public ResponseEntity<?> delete(@PathVariable Integer id) {
            this.log("Delete:" + id);
            this.dao.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping({ "/api/v2/authors", "/api/authors" })
    @RestController
    private static class V2 extends V1<AuthorV2> {
        @Autowired
        public V2(@Qualifier("v2") IAuthorRepository<AuthorV2> dao) {
            super(dao);
        }

        @GetMapping("/{id}/books")
        public ResponseEntity<?> getBooks(@PathVariable Integer id) {
            this.log("Find books by author " + id);
            Optional<AuthorV2> author = this.dao.findById(id);
            if (!author.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            Set<Book> books = author.get().getBooks();
            return ResponseEntity.ok(books);
        }
    }
}
