package com.monitoratec.victor.repositories;

import com.monitoratec.victor.models.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface IAuthorRepository<A extends Author> extends CrudRepository<A, Integer> {
}