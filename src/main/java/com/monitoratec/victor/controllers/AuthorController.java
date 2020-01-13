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
@RequestMapping("/authors")
@SuppressWarnings("unused")
public class AuthorController {
    @Autowired
    private AuthorRepository authorDAO;

    @GetMapping
    public ResponseEntity<?> listAll() {
        log.info("[Request] List authors");
        return new ResponseEntity<>(authorDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> one(@PathVariable Integer id) {
        log.info("[Request] Find author by id: " + id);
        Optional<Author> author = authorDAO.findById(id);
        if (author.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(author.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Author author) {
        log.info("[Request] Create author:" + author);
        authorDAO.save(author);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Author author) {
        if (!authorDAO.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("[Request] Update author: " + id + " with: " + author);

        authorDAO.save(author);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("[Request] Delete author:" + id);
        authorDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
