package com.monitoratec.victor.repositories;

import com.monitoratec.victor.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface BookRepository extends CrudRepository<Book, Integer> {
}