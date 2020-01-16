package com.monitoratec.victor.services;

import com.monitoratec.victor.models.Author;
import com.monitoratec.victor.models.dto.AuthorDTO;
import com.monitoratec.victor.services.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService extends AService<Author, AuthorRepository> {
    @Autowired
    private AuthorRepository repository;

    @Override
    protected AuthorRepository repository() {
        return this.repository;
    }

    public Author save(AuthorDTO authorDto, Integer id) {
        Author author = AuthorDTO.toAuthor(authorDto, true);
        // ensure the id is correct to avoid creation on update and vice-versa
        author.setId(id);

        return this.repository().save(author);
    }
}
