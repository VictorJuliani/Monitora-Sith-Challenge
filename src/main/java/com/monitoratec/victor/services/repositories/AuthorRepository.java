package com.monitoratec.victor.services.repositories;

import com.monitoratec.victor.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface AuthorRepository extends JpaRepository<Author, Integer> {}
