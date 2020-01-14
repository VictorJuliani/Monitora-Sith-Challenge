package com.monitoratec.victor.repositories;

import com.monitoratec.victor.models.Author;
import org.springframework.stereotype.Repository;

@Repository("v1")
public interface AuthorRepository extends IAuthorRepository<Author> {
}
