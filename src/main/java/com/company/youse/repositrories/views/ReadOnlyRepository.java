package com.company.youse.repositrories.views;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Kely
 * on 16, July 2022
 * http://stackoverflow.com/u/14795945
 */

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
    List<T> findAll();

    Iterable<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

    T getById(ID id);

    <S extends T> List<S> findAll(Example<S> example);

    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    Optional<T> findById(ID id);

    long count();

    long count(Specification<T> spec);
    List<T> findAll(Specification<T> spec);
    Page<T> findAll(Specification<T> spec, Pageable pageable);
    List<T> findAll(Specification<T> spec, Sort sort);
    T findOne(Specification<T> spec);

}
