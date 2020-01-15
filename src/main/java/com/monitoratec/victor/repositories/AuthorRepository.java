package com.monitoratec.victor.repositories;

import com.monitoratec.victor.models.AuthorV1;
import org.springframework.stereotype.Repository;

@Repository("v1")
public interface AuthorRepository extends IAuthorRepository<AuthorV1> {
}
