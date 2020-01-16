package com.monitoratec.victor.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AService<M, R extends JpaRepository<M, Integer>> {
    protected abstract R repository();

    public List<M> list() {
        return this.repository().findAll();
    }

    public Optional<M> one(Integer id) {
        return this.repository().findById(id);
    }

    public M save(M model) {
        return this.repository().save(model);
    }

    public void delete(Integer id) {
        this.repository().deleteById(id);
    }

    public boolean exists(Integer id) {
        return this.repository().existsById(id);
    }
}
