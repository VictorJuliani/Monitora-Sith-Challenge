package com.monitoratec.victor.controllers;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.repositories.AuthorRepository;
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
@RequestMapping("/api/v1/authors")
@SuppressWarnings("unused")
public class AuthorController {
    @Autowired
    private AuthorRepository authorDAO;

    @GetMapping
    public ResponseEntity<?> listAll() {
        log.info("[Request] List authors");
        return ResponseEntity.ok(authorDAO.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> one(@PathVariable Integer id) {
        log.info("[Request] Find author by id: " + id);
        Optional<Author> author = authorDAO.findById(id);
        if (!author.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(author.get());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Author author) {
        log.info("[Request] Create author:" + author);
        authorDAO.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(author);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Author author) {
        if (!authorDAO.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("[Request] Update author: " + id + " with: " + author);

        authorDAO.save(author);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("[Request] Delete author:" + id);
        authorDAO.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
