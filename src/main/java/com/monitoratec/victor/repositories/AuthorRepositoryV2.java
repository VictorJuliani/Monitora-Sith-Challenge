package com.monitoratec.victor.repositories;

import com.monitoratec.victor.models.AuthorV2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("v2")
public interface AuthorRepositoryV2 extends IAuthorRepository<AuthorV2> {
}
